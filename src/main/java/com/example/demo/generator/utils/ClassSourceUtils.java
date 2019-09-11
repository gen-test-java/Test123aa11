package com.example.demo.generator.utils;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

import java.io.*;

public class ClassSourceUtils {

    public static class Builder{
        private String className;
        private String packageName;
        private Boolean serializable;
        private Boolean syncToDb;

        public Builder setClassName(String className){
            this.className = Do.classNamingConvention(className);
            return this;
        }
        public Builder setPackageName(String packageName){
            this.packageName = packageName;
            return this;
        }
        public Builder setSyncToDb(Boolean syncToDb){
            this.syncToDb = syncToDb;
            return this;
        }
        public JavaClassSource createClass(String classPath){

            JavaClassSource javaClass;
            try {
                InputStream inputStream = new FileInputStream(new StringBuilder(classPath).append(className)
                        .append(".java").toString());
                String str;
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    if (inputStream != null) {
                        while ((str = reader.readLine()) != null) {
                            stringBuffer.append(str + "\n" );
                        }
                    }
                } finally {
                    try { inputStream.close(); } catch (Throwable ignore) {}
                }
                javaClass = Roaster.parse(JavaClassSource.class, stringBuffer.toString());
            } catch (Exception e) {
                javaClass = Roaster.create(JavaClassSource.class);
                javaClass.setName(className).setPackage(packageName);
                try {
                    if (serializable) {
                        javaClass.addInterface(Serializable.class);
                    }
                    if (syncToDb){
                        javaClass.addAnnotation(javax.persistence.Entity.class);
                    }
                }catch (NullPointerException ne){

                }

            }

            return javaClass;
        }

        public JavaInterfaceSource createInterface(String classPath){

            JavaInterfaceSource javaInterface;
            try {
                InputStream inputStream = new FileInputStream(new StringBuilder(classPath).append(className)
                        .append(".java").toString());
                String str;
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    if (inputStream != null) {
                        while ((str = reader.readLine()) != null) {
                            stringBuffer.append(str + "\n" );
                        }
                    }
                } finally {
                    try { inputStream.close(); } catch (Throwable ignore) {}
                }
                javaInterface = Roaster.parse(JavaInterfaceSource.class, stringBuffer.toString());
            } catch (Exception e) {
                javaInterface = Roaster.create(JavaInterfaceSource.class);
                javaInterface.setName(className).setPackage(packageName);
                try {
                    if (serializable) {
                        javaInterface.addInterface(Serializable.class);
                    }
                    if (syncToDb){
                        javaInterface.addAnnotation(javax.persistence.Entity.class);
                    }
                }catch (NullPointerException ne){

                }

            }

            return javaInterface;
        }

        public Builder isSerializable(Boolean serializable) {
            this.serializable =serializable;
            return this;
        }
    }
}