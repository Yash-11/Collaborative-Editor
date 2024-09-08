package com.example.editor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.editor.models.User;
import com.example.editor.services.UserService;
import com.example.editor.util.constants.Roles;

import java.util.LinkedList;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {


        User account01 = new User();
        User account02 = new User();
        User account03 = new User();

        User u1User = userService.getUserById("abc@gmail.com");
        if (u1User==null) {
            account01.setEmail("abc@gmail.com");
            account01.setPassword("pass");
            account01.setName("yash");
            account01.setDocuments(new LinkedList<>());
            userService.createUser(account01);
        }

        User u2User = userService.getUserById("naman@gmail.com");
        if (u2User==null) {
            account02.setEmail("naman@gmail.com");
            account02.setPassword("pass");
            account02.setName("naman");
            account02.setDocuments(new LinkedList<>());

            userService.createUser(account02);
        }


        User u3User = userService.getUserById("shubh@gmail.com");
        if (u3User==null) {
            account03.setEmail("shubh@gmail.com");
            account03.setPassword("pass");
            account03.setName("shubh");
            account03.setDocuments(new LinkedList<>());

            userService.createUser(account03);
        }

        
    }
    
}
