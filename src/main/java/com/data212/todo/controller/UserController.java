package com.data212.todo.controller;

import com.data212.todo.advice.ToDoException;
import com.data212.todo.model.User;
import com.data212.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(Principal principal, Model model) {
        model.addAttribute("tasklist", userService.userTasks(principal.getName()));
        return "/user";
    }

    @RequestMapping(value ="/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute User user) throws ToDoException {
        userService.register(user);
        log.debug("Registered user : {}", user.getId());
        return "/login";
    }



}
