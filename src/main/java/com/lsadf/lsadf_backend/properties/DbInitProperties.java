package com.lsadf.lsadf_backend.properties;

import com.lsadf.lsadf_backend.models.DbInitUser;
import lombok.Data;

import java.util.List;

@Data
public class DbInitProperties {
    private boolean enabled;
    private List<DbInitUser> users;
}
