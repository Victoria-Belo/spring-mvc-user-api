package com.api.demo_user.service;

import com.api.demo_user.dto.UserDTO;
import com.api.demo_user.model.User;
import com.api.demo_user.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.util.List;
import java.util.function.Consumer;

@Service
public class UserService implements UserInterface {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserRepository userRepository;

    private final UserSecurity userSecurity;

    @Autowired
    public UserService(UserRepository userRepository, UserSecurity userSecurity) {
        this.userRepository = userRepository;
        this.userSecurity = userSecurity;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userID) {
        return userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Override
    public User createUser(UserDTO dto) {
        try {

            String hashing = userSecurity.hashingPass(dto.getPass());
            User user = new User(dto.getName(), dto.getCpf(), dto.getEmail(), hashing);
            return userRepository.save(user);
        } catch (Exception error) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, error);
        }
    }

    @Override
    public User update(Long userID, UserDTO dto) {
        try {
            UserSecurity security = new UserSecurity();
            User user = findById(userID);
            if (user.getId().equals(userID)) {
                updateIf(dto.isNamePresent(), dto.getName(), user::setName);
                updateIf(dto.isCpfPresent(), dto.getCpf(), user::setCpf);
                updateIf(dto.isEmailPresent(), dto.getEmail(), user::setEmail);
                if (dto.getPass() != null) {
                    String hashing = security.hashingPass(dto.getPass());
                    user.setPass(hashing);
                }
            }
            return userRepository.save(user);
        } catch (Exception error) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, error);
        }
    }

    @Override
    public ResponseEntity<Void> delete(Long userID) {
        try {
            User user = findById(userID);
            if (user != null) {
                userRepository.delete(user);
                return ResponseEntity.noContent().build();
            } else {
                throw new EntityNotFoundException("ID não encontrado " + userID);
            }
        } catch (Exception error) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, error);
        }
    }

    public String login(UserDTO dto) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        try {
            User user = userRepository.findByEmail(dto.getEmail());
            if (user == null) {
                throw new EntityNotFoundException("Email não encontrado");
            }
            if (Boolean.TRUE.equals(userSecurity.checkPassword(dto.getPass(), user.getPass()))) {
                return Jwts.builder()
                        .setSubject(user.getEmail())
                        .signWith(key)
                        .compact();
            }else{
                throw new AuthenticationException("Credenciais incorretas");

            }

        } catch (Exception error) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, error);
        }
    }

    private <T> void updateIf(boolean condition, T value, Consumer<T> setter) {
        if (condition) {
            setter.accept(value);
        }
    }
}
