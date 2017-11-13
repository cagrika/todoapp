package com.data212.todo.model;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class User {

    private String id = UUID.randomUUID().toString();
    private String name;
    private String surname;
    private String email;
    private String password;
    private String[] roles;
    private List<Task> taskList;
}
