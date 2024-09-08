package com.example.editor.dto;

import java.util.List;

import com.example.editor.models.Document;

import lombok.Data;

@Data
public class DocumentDTO {
    private List<Object> doc;
    private String title;
    private String documentId;
}
