package com.example.demo.generator.utils;

import com.example.demo.generator.model.EntitySet;
import com.example.demo.generator.model.Project;

public class ProjectUtils {
    public static Object[] getPath(Project project,String projectDirectory,String parameter){
        EntitySet entitySet = project.getEntitySet();
        String packageName=new StringBuilder(project.getGroupId()).append(".").append(parameter).toString();
        String pathToClass = new StringBuilder(projectDirectory).append(packageName.replaceAll("\\.", "/")).append("/").toString();

        return  new Object[]{packageName,pathToClass,entitySet};
    }
}
