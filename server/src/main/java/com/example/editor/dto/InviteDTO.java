package com.example.editor.dto;

import lombok.Data;

@Data
public class InviteDTO {
    private String recipientEmail;
    private String documentId;
    private String title;
}