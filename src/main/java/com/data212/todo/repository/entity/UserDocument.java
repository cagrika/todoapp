package com.data212.todo.repository.entity;

import com.couchbase.client.java.repository.annotation.Id;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.List;

@Data
@Document
public class UserDocument {

    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private String[] roles;
    private List<TaskDocument> taskList;
}
