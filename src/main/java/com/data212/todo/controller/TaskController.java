package com.data212.todo.controller;

import com.data212.todo.model.Task;
import com.data212.todo.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final UserService userService;

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @ApiOperation(value = "Add task for user")
    public String addTask(Principal principal, Model model, Task task){
        userService.addTask(principal.getName(), task);
        model.addAttribute("tasklist", userService.userTasks(principal.getName()));
        return "/user";
    }

    @RequestMapping(value = "/task", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete task from user")
    public String deleteTask(Principal principal, Model model, @ModelAttribute Task task){
        userService.deleteTask(principal.getName(), task.getId());
        model.addAttribute("tasklist", userService.userTasks(principal.getName()));
        return "/user";
    }

    @RequestMapping(value = "/task", method = RequestMethod.PUT)
    @ApiOperation(value = "Update task of user")
    public String updateTask(Principal principal, Model model, @ModelAttribute Task task){
        userService.updateTask(principal.getName(), task.getId(), task.getName());
        model.addAttribute("tasklist", userService.userTasks(principal.getName()));
        return "/user";
    }
}
