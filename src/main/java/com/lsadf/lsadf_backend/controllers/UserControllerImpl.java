package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the User Controller
 */
@RestController
public class UserControllerImpl implements UserController {
    private final UserService userServiceImpl;

    public UserControllerImpl(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
}
