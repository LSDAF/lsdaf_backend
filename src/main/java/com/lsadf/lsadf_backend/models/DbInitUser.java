package com.lsadf.lsadf_backend.models;

import com.lsadf.lsadf_backend.constants.UserRole;
import lombok.Data;

import java.io.Serial;
import java.util.Set;

@Data
public class DbInitUser implements Model {

    @Serial
    private static final long serialVersionUID = -3389085489535854187L;

    private String email;
    private String name;
    private String password;
    private Set<UserRole> roles;
}
