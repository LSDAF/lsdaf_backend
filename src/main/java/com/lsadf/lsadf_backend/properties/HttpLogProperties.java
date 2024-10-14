package com.lsadf.lsadf_backend.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpLogProperties {
    private boolean enabled;
    private boolean colorEnabled;
    private List<HttpMethod> loggedMethods;
}
