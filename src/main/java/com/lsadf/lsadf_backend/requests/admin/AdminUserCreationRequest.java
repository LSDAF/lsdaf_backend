package com.lsadf.lsadf_backend.requests.admin;


import com.lsadf.lsadf_backend.constants.SocialProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreationRequest {
    private String name;
    private String email;
    private String password;
    private boolean encodePassword;
    private SocialProvider socialProvider;
}
