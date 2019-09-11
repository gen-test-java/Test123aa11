package com.example.demo.generator.extraGenerator;

import com.example.demo.generator.model.Entity;
import com.example.demo.generator.model.EntitySet;
import com.example.demo.generator.model.Project;
import com.example.demo.generator.utils.ClassSourceUtils;
import com.example.demo.generator.utils.Do;
import com.example.demo.generator.utils.ProjectUtils;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

public class ControllerGenerator {
    private String packageName;
    public synchronized void generate(String projectDirectory, Project project) {

        Object[] strings = ProjectUtils.getPath(project,projectDirectory,"controllers");

        packageName= String.valueOf(strings[0]);
        String pathToClass = String.valueOf(strings[1]);
        EntitySet entitySet = (EntitySet) strings[2];

        Do.mkDirectory(pathToClass);
        entitySet.getEntities().stream().forEach(e -> {
            try {
                doGenerate(pathToClass,e);
            } catch (IOException ex) {

                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
    }

    private void doGenerate(String pathToEntity, Entity e) throws IOException {
        String className = Do.classNamingConvention(e.getEntityName());
        String classVariable = Do.classVariableNamingConvention(className);

        String controllerClassName =new StringBuilder(className).append("Controller").toString();
        JavaClassSource javaClassSource = new ClassSourceUtils.Builder().setClassName(controllerClassName).setPackageName(packageName).createClass(pathToEntity);
        javaClassSource.addAnnotation(RestController.class);
        javaClassSource.addAnnotation(RequestMapping.class).setStringValue(new StringBuilder("/api/").append(e.getEntityName().toLowerCase()).toString());

        //Imports
        javaClassSource.addImport(ResponseEntity.class);
        javaClassSource.addImport(RequestBody.class);
        javaClassSource.addImport(HttpStatus.class);

        //global variables and instances
        String serviceClass = new StringBuilder(className).append("Service").toString();
        String serviceClassVariable = Do.classVariableNamingConvention(serviceClass);
        javaClassSource.addField().setName(serviceClassVariable).setPrivate().setType(serviceClass).addAnnotation(Autowired.class);

        //method declaration
        javaClassSource.addMethod().setName(new StringBuilder("getAll").append(className).toString()).setReturnType("ResponseEntity<?>").setPublic().setBody(
                new StringBuilder("try{")
                        .append("List<").append(className).append("> ").append(classVariable).append("s = ").append(serviceClassVariable).append(".findAll").append(className).append("();")
                        .append("return new ResponseEntity<>(").append(classVariable).append("s, HttpStatus.OK);")
                        .append("}catch(Exception e){")
                        .append("e.printStackTrace();")
                        .append("throw new Exception(\"").append(className).append(" is Null.\");")
                        .append("}").toString()
        ).addAnnotation(GetMapping.class);

        javaClassSource.addMethod().setName(new StringBuilder("delete").append(className).toString()).setReturnType("ResponseEntity<?>").setPublic().setBody(
                new StringBuilder("try{")
                        .append(serviceClassVariable).append(".delete").append(className).append("ById").append("(id);")
                        .append("return new ResponseEntity<>(\"Deleted Successful\",HttpStatus.OK);")
                        .append("}catch(Exception e){")
                        .append("e.printStackTrace();")
                        .append("throw new Exception(\"").append(className).append(" is Deleted.\");")
                        .append("}").toString()
        ).setParameters("@PathVariable Long id").addAnnotation(DeleteMapping.class);

        javaClassSource.addMethod().setName(new StringBuilder("save").append(className).toString()).setReturnType("ResponseEntity<?>").setPublic().setBody(
                new StringBuilder("if (errors.hasErrors()) {")
                        .append("return new ResponseEntity<>(errors.toString(), HttpStatus.UNPROCESSABLE_ENTITY);")
                        .append("}")
                        .append("try{")
                        .append(className).append(" ").append(classVariable).append(" = ").append(serviceClassVariable).append(".save").append(className).append("();")
                        .append("return new ResponseEntity<>(").append(classVariable).append(",HttpStatus.OK);")
                        .append("}catch(Exception e){")
                        .append("e.printStackTrace();")
                        .append("throw new Exception(\"").append(className).append(" could not be Saved.\");")
                        .append("}").toString()
        ).setParameters(
                new StringBuilder("@RequestBody ").append(className).append(" ").append(classVariable).append(",BindingResult error").toString()
        ).addAnnotation(PostMapping.class);

        Do.writeFile(pathToEntity,new StringBuilder(controllerClassName).append(".java").toString(),javaClassSource.toString());
    }
}
