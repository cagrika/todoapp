package com.data212.todo.model;


import com.data212.todo.model.enums.TaskStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
public class Task {

    private String id = UUID.randomUUID().toString();
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;
    private TaskStatus status;

}
