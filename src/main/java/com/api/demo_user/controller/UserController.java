package com.api.demo_user.controller;
import com.api.demo_user.dto.UserDTO;
import com.api.demo_user.model.User;
import com.api.demo_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PostMapping("")
    public User create(@RequestBody UserDTO dto){
        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User update(@PathVariable Long id, @RequestBody UserDTO dto){
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }

}
