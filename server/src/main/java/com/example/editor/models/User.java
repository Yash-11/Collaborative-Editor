package com.example.editor.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

@org.springframework.data.mongodb.core.mapping.Document(collection = "users")
@Data
public class User {
    
    
    private String name;
    @Id
    private String email;
    private String password;

    @DBRef
    private List<Document> documents;

    private List<Invite> invites;

}
