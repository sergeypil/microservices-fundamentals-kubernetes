package net.serg.storageservice.config;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private static final String PRINCIPAL_CLAIM_NAME = JwtClaimNames.SUB;

    public CustomJwtAuthenticationConverter() {
    }

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(getRolesFromRealmAccess(jwt));
        authorities.addAll(getRolesFromResourceAccess(jwt, "resource-client"));
        String principal = jwt.getClaimAsString(PRINCIPAL_CLAIM_NAME);
        return new JwtAuthenticationToken(jwt, authorities, principal);
    }

    private Collection<GrantedAuthority> getRolesFromRealmAccess(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
        return roles
            .stream()
            .map(roleName -> "ROLE_" + roleName.toUpperCase())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    private Collection<GrantedAuthority> getRolesFromResourceAccess(Jwt jwt, String clientId) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || resourceAccess.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
        if (clientAccess == null || clientAccess.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<String> roles = (Collection<String>) clientAccess.get("roles");
        return roles
            .stream()
            .map(roleName -> "ROLE_" + roleName.toUpperCase())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}