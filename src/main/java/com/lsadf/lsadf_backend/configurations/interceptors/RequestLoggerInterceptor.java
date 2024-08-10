package com.lsadf.lsadf_backend.configurations.interceptors;

import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.HttpLogProperties;
import com.lsadf.lsadf_backend.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j(topic = "[HTTP]")
public class RequestLoggerInterceptor implements HandlerInterceptor {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private final Set<String> loggedMethods;


    public RequestLoggerInterceptor(HttpLogProperties httpLogProperties) {
        this.loggedMethods = httpLogProperties.getLoggedMethods().stream().map(HttpMethod::toString).collect(Collectors.toSet());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (loggedMethods.contains(request.getMethod())) {
            String username = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof LocalUser) {
                username = ((LocalUser) principal).getUsername();
            } else if (principal instanceof String && !principal.equals(ANONYMOUS_USER)) {
                username = (String) principal;
            }
            String now = "date:" + DateUtils.dateTimeToString(LocalDateTime.now());
            log.info("[{}][{}][{}]{} Received incoming HTTP request",
                    request.getMethod(),
                    request.getRequestURI(),
                    now,
                    username != null ? "[user:" + username + "]" : "");
        }
        return true;
    }
}
