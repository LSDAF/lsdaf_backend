package com.lsadf.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLog {
    String username;
    int status;
    String now;
    String method;
    String requestUri;
}
