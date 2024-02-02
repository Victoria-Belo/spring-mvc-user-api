package com.api.demo_user.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

    public String hashingPass(String pass) {
        return BCrypt.hashpw(pass, BCrypt.gensalt());
    }

    public Boolean checkPassword(String pass, String hashed) {
        return BCrypt.checkpw(pass, hashed);
    }

}
