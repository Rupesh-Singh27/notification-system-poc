package com.app.registration_service.controller;


import com.app.registration_service.model.RegistrationDetails;
import com.app.registration_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<RegistrationDetails> registerUser(@RequestBody RegistrationDetails registrationDetails){
        RegistrationDetails registeredUser = userService.registerUser(registrationDetails);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

}
