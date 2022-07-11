package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.exception_handling.UserIncorrectData;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRESTController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public MyRESTController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @GetMapping("/users")
    public List<User> showAllUsers () {
        List<User> allUsers = userService.getAllUsers();
        System.out.println(allUsers);
        return allUsers;
    }

    @GetMapping("/user")
    public User getPrincipalUser() {
//        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.findByUsername(auth.getName());
        return thisUser;
    }
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser (@PathVariable Long id) {
        User user = userService.findUserPoId(id);
        if (user == null) {
            throw new NoSuchUserException("No User with ID = " + id);
        }
        return user;
    }
    @PostMapping("/users")
    public User addNewUser (@RequestBody User user) {
        userService.addUser(user);
        return user;
    }
    @PutMapping("/users")
    public User updateUser (@RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser (@PathVariable Long id) {
        User user = userService.findUserPoId(id);
        if (user == null) {
            throw new NoSuchUserException("There is no user with ID = " + id);
        }
        userService.removeUser(id);
        return "user with id = " + id + " was deleted";
    }
}
