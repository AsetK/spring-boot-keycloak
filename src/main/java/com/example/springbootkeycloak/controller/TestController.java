package com.example.springbootkeycloak.controller;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/greeting")
    public String greeting() {
        return "Hi :)";
    }

    @GetMapping(path = "/getToken")
    public Object getToken() {
        return getTokenFromKeyCloak();
    }

    private Object getTokenFromKeyCloak() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180/auth")
                .grantType(OAuth2Constants.PASSWORD)
                .realm("SpringBootKeycloak")
                .clientId("key-cloak-client")
                .username("user1")
                .password("password")
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();
        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();
        return accessToken;
    }
}
