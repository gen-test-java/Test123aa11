package com.example.demo.generator.model;

import java.util.List;

public class EntitySet {

    private String packageName;
    private List<Entity> entities;
    private Boolean serialize;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Boolean getSerialize() {
        return serialize;
    }

    public void setSerialize(Boolean serialize) {
        this.serialize = serialize;
    }

    @Override
    public String toString() {
        return "Entities{" +
                "packageName='" + packageName + '\'' +
                ", entities=" + entities +
                ", serialize=" + serialize +
                '}';
    }
}
