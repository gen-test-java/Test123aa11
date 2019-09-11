package com.example.demo.generator.service;

import com.example.demo.generator.model.Entity;
import com.example.demo.generator.model.EntitySet;
import com.example.demo.generator.model.Project;
import com.example.demo.generator.utils.ClassSourceUtils;
import com.example.demo.generator.utils.Do;
import com.example.demo.generator.utils.ProjectUtils;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.springframework.stereotype.Service;

import java.io.IOException;

public class ServiceGenerator {
    private String packageName;
    private String projectDirectory;
    private Project project;
    public void generate(String projectDirectory, Project project) {
        this.projectDirectory=projectDirectory;
        this.project= project;
        Object[] strings = ProjectUtils.getPath(project,projectDirectory,"services");

        packageName= String.valueOf(strings[0]);
        String pathToService = String.valueOf(strings[1]);
        EntitySet entitySet = (EntitySet) strings[2];

        Do.mkDirectory(pathToService);
        entitySet.getEntities().stream().forEach(e -> {
            try {
                doGenerate(pathToService,e);
            } catch (IOException ex) {

                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });
    }

    private void doGenerate(String pathToEntity, Entity e) throws IOException {
        String className = Do.classNamingConvention(e.getEntityName());
        String classVariable = Do.classVariableNamingConvention(className);

        String serviceClassName =new StringBuilder(className).append("Service").toString();
        JavaInterfaceSource javaInterfaceSource = new ClassSourceUtils.Builder().setClassName(serviceClassName).setPackageName(packageName).createInterface(pathToEntity);

        javaInterfaceSource.addMethod().setName(new StringBuilder("save").append(className).toString()).setReturnType(className).setParameters(new StringBuilder(className).append(" ").append(Do.classVariableNamingConvention(className)).toString());

        javaInterfaceSource.addMethod().setName(new StringBuilder("find").append(className).append("ById").toString()).setReturnType(className).setParameters(new StringBuilder("String").append(" ").append(Do.classVariableNamingConvention(className)).append("Id").toString());

        javaInterfaceSource.addMethod().setName(new StringBuilder("findAll").append(className).toString()).setReturnType(new StringBuilder("List<").append(className).append(">").toString());

        javaInterfaceSource.addMethod().setName(new StringBuilder("delete").append(className).append("ById").toString()).setReturnTypeVoid().setParameters(new StringBuilder("Long id").toString());

        Do.writeFile(pathToEntity,new StringBuilder(serviceClassName).append(".java").toString(),javaInterfaceSource.toString());



        generateServiceImpl(javaInterfaceSource,e);
    }

    private void generateServiceImpl(JavaInterfaceSource javaInterfaceSource,Entity e) throws IOException {
        Object[] strings = ProjectUtils.getPath(this.project,this.projectDirectory,"servicesImpl");

        packageName= String.valueOf(strings[0]);
        String pathToServiceImpl = String.valueOf(strings[1]);
        EntitySet entitySet = (EntitySet) strings[2];

        Do.mkDirectory(pathToServiceImpl);

        doGenerateServiceImpl(pathToServiceImpl,javaInterfaceSource,e);

    }
    private void doGenerateServiceImpl(String pathToEntity,JavaInterfaceSource javaInterfaceSource, Entity e) throws IOException {
        String className = Do.classNamingConvention(e.getEntityName());
        String classVariable = Do.classVariableNamingConvention(className);

        String serviceClassName =new StringBuilder(className).append("ServiceImpl").toString();
        JavaClassSource javaClassSource = new ClassSourceUtils.Builder().setClassName(serviceClassName).setPackageName(packageName).createClass(pathToEntity);
        javaClassSource.implementInterface(javaInterfaceSource);
        javaClassSource.addAnnotation(Service.class);

        //imports
        String repositoryPackage = new StringBuilder(project.getGroupId()).append(".").append("repository").toString();
        String repository =new StringBuilder(Do.classNamingConvention(className)).append("Repository").toString();
        javaClassSource.addImport(new StringBuilder(repositoryPackage).append(".").append(repository).toString());

        //global variables and instances
        javaClassSource.addField().setName(Do.classVariableNamingConvention(repository)).setType(repository);

        Do.writeFile(pathToEntity,new StringBuilder(serviceClassName).append(".java").toString(),javaClassSource.toString());

    }
}
