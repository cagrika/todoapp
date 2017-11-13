package com.data212.todo.service;

import com.data212.todo.model.Task;
import com.data212.todo.model.User;
import com.data212.todo.model.enums.TaskStatus;
import com.data212.todo.repository.UserRepository;
import com.data212.todo.repository.entity.TaskDocument;
import com.data212.todo.repository.entity.UserDocument;
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

    @Test
    public void shouldListUserTasksForGivenUsername() throws Exception {
        UserDocument userDocument = new UserDocument();
        TaskDocument taskDocument1 = new TaskDocument();
        taskDocument1.setStatus(TaskStatus.ACTIVE);
        taskDocument1.setName("1");
        taskDocument1.setDate(new Date());
        TaskDocument taskDocument2 = new TaskDocument();
        taskDocument2.setStatus(TaskStatus.ACTIVE);
        taskDocument2.setDate(new Date());
        taskDocument2.setName("2");
        TaskDocument taskDocument3 = new TaskDocument();
        taskDocument3.setStatus(TaskStatus.ACTIVE);
        taskDocument3.setDate(new Date());
        taskDocument3.setName("3");
        List<TaskDocument> taskDocumentList = new ArrayList<>();
        taskDocumentList.add(taskDocument1);
        taskDocumentList.add(taskDocument2);
        taskDocumentList.add(taskDocument3);
        userDocument.setTaskList(taskDocumentList);
        Mockito.when(userRepository.findByName("cagrika")).thenReturn(userDocument);
        assertThat(userService.userTasks("cagrika").size()).isEqualTo(3);
    }

    @Test
    public void shouldSortTasksByDate() throws Exception {
        UserDocument userDocument = new UserDocument();
        TaskDocument taskDocument1 = new TaskDocument();
        taskDocument1.setStatus(TaskStatus.ACTIVE);
        taskDocument1.setName("1");
        taskDocument1.setDate(new Date(10));
        TaskDocument taskDocument2 = new TaskDocument();
        taskDocument2.setStatus(TaskStatus.ACTIVE);
        taskDocument2.setDate(new Date(100));
        taskDocument2.setName("2");
        TaskDocument taskDocument3 = new TaskDocument();
        taskDocument3.setStatus(TaskStatus.ACTIVE);
        taskDocument3.setDate(new Date(50));
        taskDocument3.setName("3");
        List<TaskDocument> taskDocumentList = new ArrayList<>();
        taskDocumentList.add(taskDocument1);
        taskDocumentList.add(taskDocument2);
        taskDocumentList.add(taskDocument3);
        userDocument.setTaskList(taskDocumentList);
        Mockito.when(userRepository.findByName("cagrika")).thenReturn(userDocument);
        assertThat(userService.userTasks("cagrika").get(0).getDate()).isEqualTo(new Date(10));
        assertThat(userService.userTasks("cagrika").get(1).getDate()).isEqualTo(new Date(50));
        assertThat(userService.userTasks("cagrika").get(2).getDate()).isEqualTo(new Date(100));
    }

    @Test
    public void shouldAddTask(){
        Task task = new Task();
        task.setName("New Task");
        task.setDate(new Date());
        task.setStatus(TaskStatus.ACTIVE);
        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setName("New Task");
        taskDocument.setDate(new Date());
        taskDocument.setStatus(TaskStatus.ACTIVE);
        UserDocument userDocument = new UserDocument();
        userDocument.setName("cagrika");
        Mockito.when(mapperFacade.map(task,TaskDocument.class)).thenReturn(taskDocument);
        Mockito.when(userRepository.findByName("cagrika")).thenReturn(userDocument);
        Mockito.when(userRepository.save(userDocument)).thenReturn(userDocument);
        assertThat(userService.addTask("cagrika",task).getTaskList().get(0).getName()
                .equalsIgnoreCase(taskDocument.getName()));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenAddingTaskIfUserNotFound(){
        Task task = new Task();
        task.setName("New Task");
        task.setDate(new Date());
        task.setStatus(TaskStatus.ACTIVE);
        TaskDocument taskDocument = new TaskDocument();
        taskDocument.setName("New Task");
        taskDocument.setDate(new Date());
        taskDocument.setStatus(TaskStatus.ACTIVE);
        Mockito.when(mapperFacade.map(task,TaskDocument.class)).thenReturn(taskDocument);
        Mockito.when(userRepository.findByName("cagrika")).thenReturn(null);
        userService.addTask("cagrika", task);;
    }

    @Test
    public void shouldRegister() throws Exception{
        User user = new User();
        user.setName("cagrika");
        user.setEmail("cagrika@cagrika.com");
        user.setPassword("1234567");
        user.setSurname("Karaca");
        UserDocument userDocument = new UserDocument();
        userDocument.setName("cagrika");
        userDocument.setEmail("cagrika@cagrika.com");
        userDocument.setPassword("1234567");
        userDocument.setSurname("Karaca");
        Mockito.when(bCryptPasswordEncoder.encode("1234567")).thenReturn("7654321");
        Mockito.when(mapperFacade.map(user, UserDocument.class)).thenReturn(userDocument);
        Mockito.when(userRepository.save(Mockito.any(UserDocument.class))).thenReturn(userDocument);
        assertThat(userService.register(user).equals(userDocument));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenRegisteringWithPasswordLessThan6Characters() throws Exception{
        User user = new User();
        user.setName("cagrika");
        user.setEmail("cagrika@cagrika.com");
        user.setPassword("1234");
        user.setSurname("Karaca");
        userService.register(user);
    }


}
