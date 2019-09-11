package com.example.demo.generator.extraGenerator;

import com.example.demo.generator.model.Dependencies;
import com.example.demo.generator.utils.Do;

import java.io.IOException;

public class CreateProject {
    private String projectName;
    private String groupId;
    private String artifactId;
//    war | jar
    private String generationMode;
    private String description;
    private String springVersion;
    private String javaVersion;
    private Dependencies dependencies;
    private String projectDirectory;

    private CreateProject(){}

    public CreateProject(Builder builder) {
        this.projectName = builder.projectName;
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
        this.generationMode = builder.generationMode;
        this.description = builder.description;
        this.springVersion = builder.springVersion;
        this.javaVersion = builder.javaVersion;
        this.dependencies = builder.dependencies;
        this.projectDirectory = builder.projectDirectory;
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(String generationMode) {
        this.generationMode = generationMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpringVersion() {
        return springVersion;
    }

    public void setSpringVersion(String springVersion) {
        this.springVersion = springVersion;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Dependencies getDependencies() {
        return dependencies;
    }

    public void setDependencies(Dependencies dependencies) {
        this.dependencies = dependencies;
    }

    public static class Builder{
        private String projectName;
        private String groupId;
        private String artifactId;
        private String generationMode;
        private String description;
        private String springVersion;
        private String javaVersion;
        private Dependencies dependencies;
        private String projectDirectory;

        public Builder withProjectName(String projectName){
            this.projectName =projectName;
            return this;
        }

        public Builder withGroupId(String groupId){
            this.groupId = groupId;
            return this;
        }

        public Builder withArtifactId(String artifactId){
            this.artifactId = artifactId;
            return this;
        }
        public Builder withDescription(String description){
            this.description=description;
            return this;
        }
        public Builder withSpringVersion(String springVersion){
            this.springVersion=springVersion;
            return this;
        }
        public Builder withJavaVersion(String javaVersion){
            this.javaVersion=javaVersion;
            return this;
        }
        public Builder withDependencies(Dependencies dependencies){
            this.dependencies=dependencies;
            return this;
        }
        public Builder withGenerationMode(String mode){
            this.generationMode=mode;
            return this;
        }
        public Builder withProjectDirectory(String directory){
            this.projectDirectory=directory;
            return this;
        }

        public void build() throws IOException {
            new CreateProject(this);
            String project = projectName;
            Do.mkDirectory(project);
            PomGenerator.create(new CreateProject(this));

        }

    }
}
