package com.example.demo.generator.controllers;

import com.example.demo.generator.ModelGenerators.EntityGenerator;
import com.example.demo.generator.extraGenerator.ConfigGenerator;
import com.example.demo.generator.extraGenerator.ControllerGenerator;
import com.example.demo.generator.extraGenerator.CreateProject;
import com.example.demo.generator.model.EntitySet;
import com.example.demo.generator.model.Project;
import com.example.demo.generator.service.ServiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/generate")
public class GeneratorController {

    @Autowired
    EntityGenerator entityGenerator;


    @GetMapping
    public ResponseEntity<?> test() throws IOException {
        return new ResponseEntity<>("running............",HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> callGenerator(@RequestBody Project project, BindingResult bindingResult) throws IOException {
        commonSet(project);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("skeleton")
    public ResponseEntity<?> callSkeletonGenerator(@RequestBody EntitySet entiySet, BindingResult bindingResult){

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void commonSet(Project project) throws IOException {

        String baseDirectory = new StringBuilder().append(project.getProjectName()).append("/").toString();
        String projectDirectory = new StringBuilder().append(project.getProjectName()).append("/src/main/java/").toString();
        //Generate project
        new CreateProject.Builder()
                .withArtifactId(project.getArtifactId())
                .withGenerationMode(project.getGenerateMode())
                .withGroupId(project.getGroupId())
                .withProjectName(project.getProjectName())
                .withDependencies(project.getDependencies())
                .withGenerationMode(project.getGenerateMode())
                .withDescription(project.getDescription())
                .withJavaVersion(project.getJavaVersion())
                .withSpringVersion(project.getSpringVersion())
                .withProjectDirectory(baseDirectory)
                .build();

        new ConfigGenerator().generate(project,projectDirectory);
        entityGenerator.generate(projectDirectory,project.getEntitySet());
        new ControllerGenerator().generate(projectDirectory,project);
        new ServiceGenerator().generate(projectDirectory,project);
    }
}
