package com.example.demo.generator.extraGenerator;

import com.example.demo.generator.model.Project;
import com.example.demo.generator.utils.Do;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

public class ConfigGenerator {

    public synchronized void generate(Project project,String path) throws IOException {
        JavaClassSource javaClassSource = Roaster.create(JavaClassSource.class);
        javaClassSource.setName("Application").setPackage(project.getGroupId());
        javaClassSource.addAnnotation(SpringBootApplication.class);

        javaClassSource.addImport(SpringApplication.class);
        javaClassSource.addImport(SpringBootApplication.class);
        javaClassSource.addMethod().setName("main").setPublic().setStatic(true)
                .setReturnTypeVoid().setParameters("String[] args").setBody("SpringApplication.run(Application.class, args);");
        path = new StringBuilder(path).append(project.getGroupId().replaceAll("\\.","/")).append("/").toString();
        System.out.println("thepath is " +path);
        Do.writeFile(path,"Application.java",javaClassSource.toString());
    }
}
