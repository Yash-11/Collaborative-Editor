package com.example.editor.dto;

import lombok.Data;

@Data
public class DocElementDTO {
    private String title;
    private String id;

    public DocElementDTO(String id, String title) {
        this.id = id;
        this.title = title;
    }

}
