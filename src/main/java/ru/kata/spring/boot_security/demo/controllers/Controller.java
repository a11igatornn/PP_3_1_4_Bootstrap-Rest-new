package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@org.springframework.stereotype.Controller
public class Controller {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public Controller(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/user")
    public String showUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        return "userPage";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.findByUsername(auth.getName());
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("thisUser", thisUser);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "adminPage";
    }

    @PostMapping("/admin/create")
    public String creat(@ModelAttribute("newUser") User user, @RequestParam(value = "role") String[] roles) {
        user.setRoles(getRoles(roles));
        userService.addUser(user);
        return "redirect:/admin";
    }


    @PostMapping("/admin/update")
    public String update(@ModelAttribute("newUser") User user, @RequestParam(value = "role") String[] roles) {
        user.setRoles(getRoles(roles));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    public Set<Role> getRoles(String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            roleSet.add(roleRepository.findByName(role));
        }
        return roleSet;
    }

}