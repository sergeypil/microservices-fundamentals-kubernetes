package net.serg.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ErrorResponse {

    String message;

}
