package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.util.Date;
import java.util.Set;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.CREATED_AT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.UPDATED_AT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.UserInfo.*;

@Data
@AllArgsConstructor
@JsonPropertyOrder({ID, NAME, EMAIL, VERIFIED, ROLES, CREATED_AT, UPDATED_AT})
public class UserInfo implements Model {

    @Serial
    private static final long serialVersionUID = -3162522781668155748L;

    @JsonProperty(value = ID)
    private String id;

    @JsonProperty(value = NAME)
    private String name;

    @JsonProperty(value = EMAIL)
    private String email;

    @JsonProperty(value = VERIFIED)
    private boolean verified;

    @JsonProperty(value = ROLES)
    private Set<UserRole> roles;

    @JsonProperty(value = CREATED_AT)
    private Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    private Date updatedAt;
}
