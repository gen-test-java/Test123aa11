package com.example.demo.generator.model;

public class Meta {
    private Boolean primaryId=false;

    public Boolean getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Boolean primaryId) {
        this.primaryId = primaryId;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "primaryId=" + primaryId +
                '}';
    }
}
