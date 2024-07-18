package com.app.registration_service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDetails {

    private String firstName;
    private String lastName;
    private String emailId;
    private boolean isAdmin;
}
