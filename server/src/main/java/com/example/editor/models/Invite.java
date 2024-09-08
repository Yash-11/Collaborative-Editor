package com.example.editor.models;

import lombok.Data;

@Data
public class Invite {
    private String title;
    private String id;

    public Invite(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
