package com.example.editor.util.constants;

public enum Authorities {
    RESET_ANY_USER_PASSWORD(1l, "RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANE(2l, "ACCESS_ADMIN_PANEL");

    private Long authorityId;
    private String authorityString;
    private Authorities(Long authorityId, String authorityString) {
        this.authorityId = authorityId;
        this.authorityString = authorityString;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public String getAuthorityString() {
        return authorityString;
    }
}
