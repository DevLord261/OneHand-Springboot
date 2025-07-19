package org.devlord.onehand.ControllersTest;


import org.devlord.onehand.Config.SecurityConfigTest;
import org.devlord.onehand.ObjectMappers.UserMapper;
import org.devlord.onehand.User.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserEntityControllerTest {

    @Autowired
    MockMvc mvc;

    @Mock
    UserService userService;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserController userController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @Test
    void createUserTest() throws Exception {
        CreateUserDTO user = mock(CreateUserDTO.class);
        user.setFirstname("hsen");
        user.setLastname("byomi");
        user.setEmail("hsnebyomi@gmail.com");
        user.setPassword("password123");
        user.setUsername("hasson");


        when(userService.ExistsEmail(user.getEmail())).thenReturn(false);
        when(userService.ExistsUsername(user.getUsername())).thenReturn(false);
        when(userService.Registeruser(any())).thenReturn(true);

        mvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "firstname":"hussein",
                          "lastname":"bayoumi",
                          "username":"devlpr",
                          "password":"password123",
                          "email":"hsenbyomi@gmail.com"
                        }""")).andExpect(status().isOk())
                .andExpect(content().string("Successfully registered"));

    }
    @Test
    void GetAllUsers() {
        UserDTO user = mock(UserDTO.class);
        user.setFirstname("hsen");
        user.setLastname("byomi");
        user.setEmail("hsnebyomi@gmail.com");
//        user.setPassword("password123");
        user.setUsername("hasson");
        UserDTO user2 = mock(UserDTO.class);
        user2.setFirstname("hsen2");
        user2.setLastname("byomi2");
        user2.setEmail("hsnebyomi2@gmail.com");
//        user2.setPassword("password1232");
        user2.setUsername("hasson2");
        List<UserDTO> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        when(userService.GetAll()).thenReturn(list);

        try{
            mvc.perform(get("/api/users/").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].username").value("hasson"))
                    .andExpect(jsonPath("$[1].username").value("hasson2"));
        }catch (Exception e){
            System.out.println("error :"+ e.getMessage());
        }
    }

}
