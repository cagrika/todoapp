package com.data.todo.service;

import com.data.todo.model.Task;
import com.data.todo.model.User;
import com.data.todo.model.enums.TaskStatus;
import com.data.todo.repository.UserRepository;
import com.data.todo.repository.entity.TaskDocument;
import com.data.todo.repository.entity.UserDocument;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String name = "cagrika";
    private static final String surname = "karaca";
    private static final String email = "cagrika@cagrika.com";
    private static final String password = "1234567";
    private static final String taskId = "TASKID";
    private static final String unknowTaskId = "UNKNOWNTASKID";
    private static final String newTask = "New Task";

    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    MapperFacade mapperFacade;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setup(){
        userService = new UserService(userRepository,mapperFacade, bCryptPasswordEncoder);
    }

    private List<TaskDocument> createTaskDocumentList(){
        List<TaskDocument> taskDocumentList = new ArrayList<>();
        TaskDocument taskDocument1 = new TaskDocument();
        taskDocument1.setStatus(TaskStatus.ACTIVE);
        taskDocument1.setName("1");
        taskDocument1.setDate(new Date());
        TaskDocument taskDocument2 = new TaskDocument();
        taskDocument2.setStatus(TaskStatus.ACTIVE);
        taskDocument2.setName("2");
        taskDocument2.setDate(new Date());
        TaskDocument taskDocument3 = new TaskDocument();
        taskDocument3.setStatus(TaskStatus.ACTIVE);
        taskDocument3.setName("3");
        taskDocument3.setDate(new Date());
        taskDocumentList.add(taskDocument1);
        taskDocumentList.add(taskDocument2);
        taskDocumentList.add(taskDocument3);
        return taskDocumentList;
    }

    private UserDocument createUserDocument(){
        UserDocument userDocument = new UserDocument();
        userDocument.setTaskList(createTaskDocumentList());
        return userDocument;
    }

    @Test
    public void it_should_list_taks_for_given_username() throws Exception {
        UserDocument userDocument = createUserDocument();
        List<TaskDocument> taskDocumentList = createTaskDocumentList();
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        assertThat(userService.userTasks(name)).isEqualTo(taskDocumentList);
    }

    @Test
    public void it_should_sort_tasks_by_date() throws Exception {
        UserDocument userDocument = createUserDocument();
        userDocument.getTaskList().get(0).setDate(new Date(100));
        userDocument.getTaskList().get(1).setDate(new Date(10));
        userDocument.getTaskList().get(2).setDate(new Date(50));
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        assertThat(userService.userTasks(name).get(0).getDate()).isEqualTo(new Date(10));
        assertThat(userService.userTasks(name).get(1).getDate()).isEqualTo(new Date(50));
        assertThat(userService.userTasks(name).get(2).getDate()).isEqualTo(new Date(100));
    }

    @Test
    public void it_should_return_empty_task_list_if_no_active_task_exist(){
        UserDocument userDocument = createUserDocument();
        userDocument.getTaskList().forEach(taskDocument -> taskDocument.setStatus(TaskStatus.DONE));
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        assertThat(userService.userTasks(name).size()).isEqualTo(0);
    }
    @Test
    public void it_should_add_task(){
        Task task = new Task();
        task.setName(newTask);
        task.setDate(new Date());
        task.setStatus(TaskStatus.ACTIVE);
        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setName(newTask);
        taskDocument.setDate(new Date());
        taskDocument.setStatus(TaskStatus.ACTIVE);
        UserDocument userDocument = new UserDocument();
        userDocument.setName(name);
        Mockito.when(mapperFacade.map(task,TaskDocument.class)).thenReturn(taskDocument);
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        Mockito.when(userRepository.save(userDocument)).thenReturn(userDocument);
        assertThat(userService.addTask(name,task).getTaskList().get(0).getName())
                .isEqualToIgnoringCase(taskDocument.getName());
    }

    @Test
    public void it_should_add_task_if_usertasks_not_empty(){
        UserDocument userDocument = createUserDocument();
        int firstCount = userDocument.getTaskList().size();
        Task task = new Task();
        task.setName(newTask);
        task.setDate(new Date());
        task.setStatus(TaskStatus.ACTIVE);
        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setName(newTask);
        taskDocument.setDate(new Date());
        taskDocument.setStatus(TaskStatus.ACTIVE);
        Mockito.when(mapperFacade.map(task,TaskDocument.class)).thenReturn(taskDocument);
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        Mockito.when(userRepository.save(userDocument)).thenReturn(userDocument);
        assertThat(userService.addTask(name,task).getTaskList().size()).isEqualTo(firstCount+1);
    }

    @Test
    public void it_should_delete_task(){

        UserDocument userDocument = createUserDocument();
        userDocument.getTaskList().forEach(taskDocument -> taskDocument.setId(unknowTaskId));
        userDocument.getTaskList().get(0).setId(taskId);
        int firstCount = userDocument.getTaskList().size();
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        Mockito.when(userRepository.save(userDocument)).thenReturn(userDocument);
        assertThat(userService.deleteTask(name, taskId).getTaskList().size()).isEqualTo(firstCount-1);
    }

    @Test
    public void it_should_update_task(){

        UserDocument userDocument = createUserDocument();
        userDocument.getTaskList().forEach(taskDocument -> taskDocument.setId(unknowTaskId));
        userDocument.getTaskList().get(0).setId(taskId);
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        Mockito.when(userRepository.save(userDocument)).thenReturn(userDocument);
        assertThat(
                userService.updateTask(name,taskId,"Updated Task").getTaskList().get(0).getName())
                .isEqualToIgnoringCase("Updated Task");
    }

    @Test
    public void it_should_not_update_task_if_task_id_not_found_for_user(){
        UserDocument userDocument = createUserDocument();
        userDocument.getTaskList().forEach(taskDocument -> taskDocument.setId(taskId));
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        Mockito.when(userRepository.save(userDocument)).thenReturn(userDocument);
        assertThat(userService.updateTask(name,unknowTaskId,"New Task 2").getTaskList())
                .isEqualTo(userDocument.getTaskList());
    }


    @Test(expected = Exception.class)
    public void it_should_throw_exception_when_adding_task_if_username_not_found(){
        Task task = new Task();
        task.setName(newTask);
        task.setDate(new Date());
        task.setStatus(TaskStatus.ACTIVE);
        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setName(newTask);
        taskDocument.setDate(new Date());
        taskDocument.setStatus(TaskStatus.ACTIVE);
        Mockito.when(mapperFacade.map(task,TaskDocument.class)).thenReturn(taskDocument);
        Mockito.when(userRepository.findByName(name)).thenReturn(null);
        userService.addTask(name, task);;
    }

    @Test
    public void it_should_find_by_name() {
        UserDocument userDocument = new UserDocument();
        userDocument.setName(name);
        userDocument.setEmail(email);
        userDocument.setPassword(password);
        userDocument.setSurname(surname);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setSurname(surname);
        Mockito.when(userRepository.findByName(name)).thenReturn(userDocument);
        Mockito.when(mapperFacade.map(userDocument,User.class)).thenReturn(user);
        assertThat(userService.findByName(name)).isEqualTo(user);
    }

    @Test
    public void it_should_register() throws Exception{
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setSurname(surname);
        UserDocument userDocument = new UserDocument();
        userDocument.setName(name);
        userDocument.setEmail(email);
        userDocument.setPassword(password);
        userDocument.setSurname(surname);
        Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn("7654321");
        Mockito.when(mapperFacade.map(user, UserDocument.class)).thenReturn(userDocument);
        Mockito.when(userRepository.save(Mockito.any(UserDocument.class))).thenReturn(userDocument);
        assertThat(userService.register(user)).isEqualTo(userDocument);
    }

    @Test(expected = Exception.class)
    public void it_should_throw_exception_when_registering_with_password_less_than_6_characters() throws Exception{
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("1234");
        user.setSurname(surname);
        userService.register(user);
    }

    @Test(expected = Exception.class)
    public void it_should_throw_exception_when_registering_with_empty_username() throws Exception{
        User user = new User();
        user.setName("");
        user.setEmail(email);
        user.setPassword(password);
        user.setSurname(surname);
        userService.register(user);
    }

    @Test(expected = Exception.class)
    public void it_should_throw_exception_when_registering_with_empty_email() throws Exception{
        User user = new User();
        user.setName(name);
        user.setEmail("");
        user.setPassword(password);
        user.setSurname(surname);
        userService.register(user);
    }

}
