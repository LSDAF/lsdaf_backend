package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.services.UserServiceImpl;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {
    private final UserServiceImpl userServiceImpl;

    public UserControllerImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
}
