### microservices-fundamentals
To launch resources using docker-compose.yml you need to add "127.0.0.1 keycloak-server" in C:\Windows\System32\drivers\etc\hosts file

## Some created resources
- 18 docker containers
- eureka with 5 microservices
- keycloak with registred client, 3 users, and 2 roles. Kate has admin role, Tiffany has user role, Jane doesn't have roles
- logs in Kibana. The source for logs is index in elasticsearch
- zipkin server where we can find traces
- prometheus server and health of microservices
- grafana server with three dashboards, which are created through predefined json file and datasourse which is prometheus

## Urls
http://localhost:8084/resources/14
http://localhost:8084/songs/14
http://localhost:8084/storages

## AWS
aws --endpoint-url=http://localhost:4566 s3 ls --profile localstack
aws --endpoint-url=http://localhost:4566 s3 ls s3://permanent-bucket/ --recursive --profile localstack

## Workflow
1) Save new resource
2) Get this resource data
3) Show resource metadata
4) Get storages with keycloak