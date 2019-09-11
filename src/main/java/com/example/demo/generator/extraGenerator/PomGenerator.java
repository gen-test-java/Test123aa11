package com.example.demo.generator.extraGenerator;

import com.example.demo.generator.model.Dependencies;
import com.example.demo.generator.utils.Do;

import java.io.IOException;
import java.util.List;

public class PomGenerator {

    private synchronized static String description(String description){
        if (description != null) {
            String desc = new StringBuilder("    <description>").append(description).append("</description>\n").toString();
            return desc;
        }
        return null;
    }
    private static String javaVersion(String javaVersion){
        String version=null;
        if (javaVersion != null) {
            version = new StringBuilder("        <java.version>").append(javaVersion).append("</java.version>\n").toString();
            return version;
        }
        version = new StringBuilder("        <java.version>").append("1.8").append("</java.version>\n").toString();
        return version;
    }
    private synchronized static String header(CreateProject project) {
        String header = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").append(
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n").append(
                "    <modelVersion>4.0.0</modelVersion>\n").append(
                "    <groupId>").append(project.getGroupId()).append("</groupId>\n").append(
                "    <artifactId>").append(project.getArtifactId()).append("</artifactId>\n").append(
                "    <version>0.0.1-SNAPSHOT</version>\n").append(
                "    <packaging>").append(project.getGenerationMode()).append("</packaging>\n").append(
                "    <name>").append(project.getProjectName()).append("</name>\n").append(
                        description(project.getDescription())).append(
                "    <parent>\n").append(
                "        <groupId>org.springframework.boot</groupId>\n").append(
                "        <artifactId>spring-boot-starter-parent</artifactId>\n").append(
                "        <version>").append(project.getSpringVersion()).append("</version>\n").append(
                "        <relativePath/>\n").append(
                "        <!-- lookup parent from repository -->\n").append(
                "    </parent>\n").append(
                "    <properties>\n").append(
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n").append(
                "        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>\n").append(
                javaVersion(project.getJavaVersion())).append(
                "    </properties>").toString();
        return header;
    }

    private static String footer(){
        String footer =new StringBuilder("</dependencies>\n").append("<build>\n").append(
                "        <plugins>\n").append(
                "            <plugin>\n").append(
                "                <groupId>org.springframework.boot</groupId>\n").append(
                "                <artifactId>spring-boot-maven-plugin</artifactId>\n").append(
                "                <configuration>\n").append(
                "                    <fork>true</fork>\n").append(
                "                </configuration>\n").append(
                "            </plugin>\n").append(
                "        </plugins>\n").append(
                "    </build>")
                .append("</project>").toString();
        return footer;
    }

    private synchronized static String dependencies(Dependencies dependencies){
        StringBuilder dependency = new StringBuilder("<dependencies>\n").append(
                "      \n").append(
                "        <dependency>\n").append(
                "            <groupId>org.springframework.boot</groupId>\n").append(
                "            <artifactId>spring-boot-devtools</artifactId>\n").append(
                "            <scope>runtime</scope>\n").append(
                "            <optional>true</optional>\n").append(
                "        </dependency>\n").append(
                "                \n").append(
                "        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations -->\n").append(
                "        <dependency>\n").append(
                "            <groupId>com.fasterxml.jackson.core</groupId>\n").append(
                "            <artifactId>jackson-annotations</artifactId>\n").append(
                "        </dependency>\n").append(
                "        <dependency>\n").append(
                "            <groupId>org.springframework.boot</groupId>\n").append(
                "            <artifactId>spring-boot-starter-web</artifactId>\n").append(
                "        </dependency>\n").append(
                "\n");
        try {
            List<String> dependencieList = dependencies.getDependencies();
            if (dependencieList.isEmpty()) {
                dependency.append(extraDependencies(dependencies.getDependencies())).append(
                        "    </dependencies>");
            }
        }catch (NullPointerException ne){

        }
        return dependency.toString();
    }



    private static String jpa(){
        String jpa = new StringBuilder().append(
                "        <dependency>\n").append(
                "            <groupId>org.springframework.boot</groupId>\n").append(
                "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n").append(
                "        </dependency>\n").toString();
        return jpa;
    }

    private static String mysql(){
        String mysql=new StringBuilder().append(
                "        <dependency>\n").append(
                "            <groupId>mysql</groupId>\n").append(
                "            <artifactId>mysql-connector-java</artifactId>\n").append(
                "            <scope>runtime</scope>\n").append(
                "        </dependency>\n").toString();
        return mysql;
    }

    private static String swagger(){
        String swagger = new StringBuilder().append(
                "        <dependency>\n").append(
                "            <groupId>io.springfox</groupId>\n").append(
                "            <artifactId>springfox-swagger2</artifactId>\n").append(
                "            <version>2.7.0</version>\n").append(
                "        </dependency>\n").append(
                "        <dependency>\n").append(
                "            <groupId>io.springfox</groupId>\n").append(
                "            <artifactId>springfox-swagger-ui</artifactId>\n").append(
                "            <version>2.7.0</version>\n").append(
                "        </dependency>\n").toString();
        return swagger;
    }

    private static String extraDependencies(List<String> dependencies) {
        if (dependencies==null){
            return "";
        }
        if (dependencies.parallelStream().anyMatch(s -> s.equalsIgnoreCase("mysql"))){
            return mysql();
        }
        if (dependencies.parallelStream().anyMatch(s -> s.equalsIgnoreCase("jpa"))){
            return jpa();
        }
        if (dependencies.parallelStream().anyMatch(s -> s.equalsIgnoreCase("swagger"))){
            return swagger();
        }
        return null;
    }

    public static void create(CreateProject createProject) throws IOException {
        String pom = new StringBuilder(header(createProject)).append(dependencies(createProject.getDependencies())).append(footer()).toString();

        Do.writeFile(createProject.getProjectDirectory(),"pom.xml",pom);
    }

}
