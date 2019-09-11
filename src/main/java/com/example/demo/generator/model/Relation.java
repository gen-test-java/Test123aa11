package com.example.demo.generator.model;

public class Relation {

    private String entityName;
    private String entityClass;
    private String relationType;
    private Boolean bidirectional=true;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Boolean getBidirectional() {
        return bidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "entityName='" + entityName + '\'' +
                ", entityClass='" + entityClass + '\'' +
                ", type='" + relationType + '\'' +
                ", bidirectional=" + bidirectional +
                '}';
    }
}
