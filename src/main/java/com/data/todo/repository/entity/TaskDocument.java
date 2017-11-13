package com.data.todo.repository.entity;


import com.data.todo.model.enums.TaskStatus;
import lombok.Data;
import java.util.Date;

@Data
public class TaskDocument {

    private String id;
    private String name;
    private Date date;
    private TaskStatus status;

}
