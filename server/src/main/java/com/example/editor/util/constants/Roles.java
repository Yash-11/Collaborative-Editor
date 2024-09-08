package com.example.editor.util.constants;

public enum Roles {
    PATIENT("ROLE_PATIENT"),
    DOCTOR("ROLE_DOCTOR");

    private String role;
    private Roles(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
