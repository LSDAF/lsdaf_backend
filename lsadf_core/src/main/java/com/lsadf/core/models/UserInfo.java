package com.lsadf.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.util.Set;

import static com.lsadf.core.constants.JsonAttributes.UserInfo.*;


@Data
@AllArgsConstructor
@JsonPropertyOrder({NAME, EMAIL, VERIFIED, ROLES})
public class UserInfo implements Model {

    @Serial
    private static final long serialVersionUID = -3162522781668155748L;

    @JsonProperty(value = NAME)
    private String name;

    @JsonProperty(value = EMAIL)
    private String email;

    @JsonProperty(value = VERIFIED)
    private boolean verified;

    @JsonProperty(value = ROLES)
    private Set<String> roles;
}