package com.example.editor.models;

// import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Data;
import lombok.NoArgsConstructor;

@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
@Data
@NoArgsConstructor
public class Document {


    private String user;
    private String headline;

    @Id
    private String id;
    private LinkedList<Object> data;
    private LocalDate date;

    @DBRef
    private List<User> users;

    // public Document() {
    //     this.headline = "Unknown";
    //     this.date = new Date();
    // }
}