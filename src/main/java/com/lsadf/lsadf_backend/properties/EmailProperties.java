package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailProperties {
    private String host;
    private boolean debug;
    private int port;
    private String username;
    private String password;
    private boolean auth;
    private boolean startTlsEnabled;
    private boolean startTlsRequired;
    private int connectionTimeout;
    private int timeout;
    private int writeTimeout;
}
