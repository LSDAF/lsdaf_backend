package com.lsadf.lsadf_backend.models;

import com.lsadf.lsadf_backend.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class UserInfo {
    private String id;
    private String name;
    private String email;
    private List<UserRole> roles;
    private Date createdAt;
    private Date updatedAt;
}
