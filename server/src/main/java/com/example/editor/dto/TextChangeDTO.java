package com.example.editor.dto;

import lombok.Data;

@Data
public class TextChangeDTO {
    private String documentId;
    private String clientId;
    private Object delta; // You can adjust the type based on your delta structure

    // Getters and setters
    // public String getDocumentId() {
    //     return documentId;
    // }

    // public void setDocumentId(String documentId) {
    //     this.documentId = documentId;
    // }

    // public Object getDelta() {
    //     return delta;
    // }

    // public void setDelta(Object delta) {
    //     this.delta = delta;
    // }
}

