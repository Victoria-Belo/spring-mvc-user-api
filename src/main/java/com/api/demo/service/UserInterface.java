package com.api.demo.service;
import com.api.demo.dto.UserDTO;
import com.api.demo.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserInterface {
    List<User> findAll();
    User findById(Long userID);
    User createUser(UserDTO dto);
    User update(Long userID, UserDTO dto);
    ResponseEntity<Void> delete(Long userID);
}
