package com.app.registration_service.service;

import com.app.registration_service.model.RegistrationDetails;

public interface UserService {
    RegistrationDetails registerUser(RegistrationDetails registrationDetails);
}
