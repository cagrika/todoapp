package com.data212.todo.repository.entity;


import com.data212.todo.model.enums.TaskStatus;
import lombok.Data;
import java.util.Date;

@Data
public class TaskDocument {

    private String id;
    private String name;
    private Date date;
    private TaskStatus status;

}
