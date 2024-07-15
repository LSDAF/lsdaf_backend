package com.lsadf.lsadf_backend.models;

import com.lsadf.lsadf_backend.constants.UserRole;
import lombok.Data;

import java.util.Set;

@Data
public class DbInitUser {
    private String email;
    private String name;
    private String password;
    private Set<UserRole> roles;
}
