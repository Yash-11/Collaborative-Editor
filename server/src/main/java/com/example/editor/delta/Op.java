package com.example.editor.delta;

public class Op {
    private String insert;
    private Integer delete;
    private Object retain;  // This can be Integer or another object
    private AttributeMap attributes;

    // Getters and Setters
    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public Object getRetain() {
        return retain;
    }

    public void setRetain(Object retain) {
        this.retain = retain;
    }

    public AttributeMap getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeMap attributes) {
        this.attributes = attributes;
    }

    // Utility method for calculating the length of an Op
    public static int length(Op op) {
        if (op.getDelete() != null) {
            return op.getDelete();
        } else if (op.getRetain() instanceof Integer) {
            return (Integer) op.getRetain();
        } else if (op.getRetain() instanceof Object) {
            return 1;  // Assuming non-numeric retain counts as length 1
        } else if (op.getInsert() != null) {
            return op.getInsert() instanceof String ? op.getInsert().length() : 1;
        }
        return 0;
    }
}

