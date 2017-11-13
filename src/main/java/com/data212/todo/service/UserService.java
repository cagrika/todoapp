package com.data212.todo.service;

import com.data212.todo.model.Task;
import com.data212.todo.model.User;
import com.data212.todo.model.enums.TaskStatus;
import com.data212.todo.repository.UserRepository;
import com.data212.todo.repository.entity.TaskDocument;
import com.data212.todo.repository.entity.UserDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MapperFacade mapperFacade;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDocument register(User user) throws Exception {
        log.debug("Registering new user with id : {}", user.getId());
        if(user.getEmail().equalsIgnoreCase("") ||
                user.getName().equalsIgnoreCase("") ||
                user.getPassword().length()<6){
            throw new Exception("Invalid user option");
        }
        UserDocument userDocument = mapperFacade.map(user, UserDocument.class);
        String encodedPass = bCryptPasswordEncoder.encode(user.getPassword());
        userDocument.setPassword(encodedPass);
        String[] roles = new String[1];
        roles[0] = "ROLE_USER";
        userDocument.setRoles(roles);
        userDocument.setTaskList(new ArrayList<>());
        return userRepository.save(userDocument);
    }

    public User findByName(String name){
        log.debug("Finding user : {}", name);
        return mapperFacade.map(userRepository.findByName(name), User.class);
    }

    public UserDocument addTask(String username, Task task){
        log.debug("Adding task for user : {}", username);
        UserDocument userDocument =userRepository.findByName(username);
        log.debug("Preparing task Document...");
        TaskDocument taskDocument = mapperFacade.map(task, TaskDocument.class);
        log.debug("Prepared task Document with id : {}", taskDocument.getId());
        taskDocument.setStatus(TaskStatus.ACTIVE);
        if(userDocument.getTaskList()==null){
            log.debug("Creating first task for user : {}", username);
            List<TaskDocument> taskDocumentList = new ArrayList();
            taskDocumentList.add(mapperFacade.map(task, TaskDocument.class));
            userDocument.setTaskList(taskDocumentList);
            return userRepository.save(userDocument);
        } else {
            userDocument.getTaskList().add(taskDocument);
            return userRepository.save(userDocument);
        }
    }


    public UserDocument deleteTask(String username, String taskId){
        log.debug("Deleting task from user : {} taskId : {}", username, taskId);
        UserDocument userDocument =userRepository.findByName(username);
        List<TaskDocument> taskDocuments = userDocument.getTaskList();
        taskDocuments.removeIf(taskDocument -> taskDocument.getId().equalsIgnoreCase(taskId));
        userRepository.save(userDocument);
        return userDocument;
    }

    public UserDocument updateTask(String username, String taskId, String name){
        log.debug("Updating task of user : {} taskId : {}", username, taskId);
        UserDocument userDocument =userRepository.findByName(username);
        List<TaskDocument> taskDocuments = userDocument.getTaskList();
        taskDocuments.stream().forEach(
                taskDocument -> {
                    if(taskDocument.getId().equalsIgnoreCase(taskId)){
                        taskDocument.setName(name);
                    }
                }
        );
        userRepository.save(userDocument);
        return userDocument;
    }
    public List<TaskDocument> userTasks(String username){
        log.debug("Listing tasks of user : {}", username);
        UserDocument userDocument = userRepository.findByName(username);
        return filterActiveTasksAndSortByDate(userDocument.getTaskList());
    }

    private List<TaskDocument> filterActiveTasksAndSortByDate(List<TaskDocument> taskDocuments){
        return taskDocuments.stream()
                .filter(taskDocument -> taskDocument.getStatus()==TaskStatus.ACTIVE)
                .sorted(Comparator.comparing(TaskDocument::getDate))
                .collect(Collectors.toList());
    }
}
