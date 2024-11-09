package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerProperties {
    private String hostName;
    private String port;
    private boolean https;
}
