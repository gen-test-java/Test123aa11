package com.example.demo.generator.model;

import java.util.List;
import java.util.Map;

public class Entity extends EntitySet{

    private String entityName;
    private List<Attribute> attributes;
    private Map<String,Object> meta;

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "entityName='" + entityName + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
