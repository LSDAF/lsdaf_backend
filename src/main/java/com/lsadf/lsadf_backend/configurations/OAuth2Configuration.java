package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.properties.OAuth2Properties;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.oauth2.*;
import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.lsadf.lsadf_backend.constants.BeanConstants.ClientRegistration.OAUTH2_FACEBOOK_CLIENT_REGISTRATION;
import static com.lsadf.lsadf_backend.constants.BeanConstants.ClientRegistration.OAUTH2_GOOGLE_CLIENT_REGISTRATION;


@Configuration
public class OAuth2Configuration {

    private static final String REDIRECT_URI = "{baseUrl}/oauth2/code/{registrationId}";

    private static final String GOOGLE = "google";
    private static final String FACEBOOK = "facebook";

    @Bean
    public CustomOAuth2UserService customOAuth2UserService(UserService userService) {
        return new CustomOAuth2UserService(userService);
    }

    @Bean
    public CustomOidcUserService customOidcUserService(UserService userService) {
        return new CustomOidcUserService(userService);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler(TokenProvider<JwtTokenEntity> tokenProvider,
                                                                                 HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                                                                 OAuth2Properties oAuth2Properties,
                                                                                 UserDetailsService lsadfUserDetailsService) {
        return new OAuth2AuthenticationSuccessHandler(tokenProvider, httpCookieOAuth2AuthorizationRequestRepository, oAuth2Properties, lsadfUserDetailsService);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        return new OAuth2AuthenticationFailureHandler(httpCookieOAuth2AuthorizationRequestRepository);
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRestOperations(restTemplate);
        return tokenResponseClient;
    }

    @Bean(name = OAUTH2_GOOGLE_CLIENT_REGISTRATION)
    public ClientRegistration googleClientRegistration(OAuth2Properties oAuth2Properties) {
        OAuth2ClientProperties.Registration googleRegistration = oAuth2Properties.getRegistration().get(GOOGLE);
        OAuth2ClientProperties.Provider googleProvider = oAuth2Properties.getProvider().get(GOOGLE);

        return ClientRegistration.withRegistrationId(GOOGLE)
                .clientId(googleRegistration.getClientId())
                .clientSecret(googleRegistration.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(googleProvider.getTokenUri())
                .authorizationUri(googleProvider.getAuthorizationUri())
                .redirectUri(REDIRECT_URI)
                .clientName(GOOGLE)
                .scope("openid", "profile", "email", "address", "phone")
                .userInfoUri(googleProvider.getUserInfoUri())
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(googleProvider.getJwkSetUri())
                .build();
    }

    @Bean(name = OAUTH2_FACEBOOK_CLIENT_REGISTRATION)
    public ClientRegistration facebookClientRegistration(OAuth2Properties oAuth2Properties) {
        OAuth2ClientProperties.Registration facebookRegistration = oAuth2Properties.getRegistration().get(FACEBOOK);
        OAuth2ClientProperties.Provider facebookProvider = oAuth2Properties.getProvider().get(FACEBOOK);

        return ClientRegistration.withRegistrationId(FACEBOOK)
                .clientId(facebookRegistration.getClientId())
                .clientSecret(facebookRegistration.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(facebookProvider.getTokenUri())
                .authorizationUri(facebookProvider.getAuthorizationUri())
                .redirectUri(REDIRECT_URI)
                .clientName(FACEBOOK)
                .scope("openid", "profile", "email", "address", "phone")
                .userInfoUri(facebookProvider.getUserInfoUri())
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(facebookProvider.getJwkSetUri())
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(@Qualifier(OAUTH2_GOOGLE_CLIENT_REGISTRATION) ClientRegistration googleClientRegistration,
                                                                     @Qualifier(OAUTH2_FACEBOOK_CLIENT_REGISTRATION) ClientRegistration facebookClientRegistration) {
        return new InMemoryClientRegistrationRepository(Arrays.asList(googleClientRegistration, facebookClientRegistration));
    }
}
