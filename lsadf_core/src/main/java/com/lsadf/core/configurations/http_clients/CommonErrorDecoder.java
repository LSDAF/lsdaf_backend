package com.lsadf.core.configurations.http_clients;

import com.lsadf.core.exceptions.http.*;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("{} HTTP ERROR {} {}", methodKey, response.status(), response.reason());
        return switch (response.status()) {
            case 400 -> new BadRequestException(response.reason());
            case 401 -> new UnauthorizedException(response.reason());
            case 403 -> new ForbiddenException(response.reason());
            case 404 -> new NotFoundException(response.reason());
            case 500 -> new InternalServerErrorException(response.reason());
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }
}
