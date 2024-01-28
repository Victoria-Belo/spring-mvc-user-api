package com.api.demo_user.service;
import com.api.demo_user.dto.UserDTO;
import com.api.demo_user.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserInterface {
    List<User> findAll();
    User findById(Long userID);
    User createUser(UserDTO dto);
    User update(Long userID, UserDTO dto);
    ResponseEntity<Void> delete(Long userID);
}
