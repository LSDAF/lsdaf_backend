package com.lsadf.lsadf_backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lsadf.lsadf_backend.constants.JsonAttributes;
import com.lsadf.lsadf_backend.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.CREATED_AT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.UPDATED_AT;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.UserInfo.*;

@Data
@AllArgsConstructor
@JsonPropertyOrder({ID, NAME, EMAIL, ROLES, CREATED_AT, UPDATED_AT})
public class UserInfo {

    @JsonProperty(value = ID)
    private String id;

    @JsonProperty(value = NAME)
    private String name;

    @JsonProperty(value = EMAIL)
    private String email;

    @JsonProperty(value = ROLES)
    private List<UserRole> roles;

    @JsonProperty(value = CREATED_AT)
    private Date createdAt;

    @JsonProperty(value = UPDATED_AT)
    private Date updatedAt;
}
