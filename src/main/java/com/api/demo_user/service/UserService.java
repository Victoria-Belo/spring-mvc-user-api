package com.api.demo_user.service;
import java.util.List;
import com.api.demo_user.model.User;
import com.api.demo_user.dto.UserDTO;
import java.util.function.Consumer;
import org.springframework.http.HttpStatus;
import com.api.demo_user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.ErrorResponseException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService implements UserInterface {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            UserSecurity security = new UserSecurity();
            String hashing = security.hashingPass(dto.getPass());
            User user = new User(dto.getName(), dto.getCpf(),dto.getEmail(),hashing);
            return userRepository.save(user);
        }catch (Exception error){
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST, error);
        }
    }

    @Override
    public User update(Long userID, UserDTO dto) {
        try {
            UserSecurity security = new UserSecurity();
            User user = findById(userID);
            if(user.getId().equals(userID)) {
                updateIf(dto.isNamePresent(), dto.getName(), user::setName);
                updateIf(dto.isCpfPresent(), dto.getCpf(), user::setCpf);
                updateIf(dto.isEmailPresent(), dto.getEmail(), user::setEmail);
                if (dto.getPass() != null) {
                    String hashing = security.hashingPass(dto.getPass());
                    user.setPass(hashing);
                }
            }
            return userRepository.save(user);
        } catch(Exception error){
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

    private <T> void updateIf(boolean condition, T value, Consumer<T> setter) {
        if (condition) {
            setter.accept(value);
        }
    }
}
