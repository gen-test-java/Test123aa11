package com.example.demo.generator.utils;

import java.io.*;

public class Do {
    public static void writeFile(String filePath,String fileName,String content) throws IOException {
        File file= new File(filePath);
        if (!file.exists()){
            mkDirectory(filePath);
        }
//        System.out.println(new StringBuilder(filePath).append(fileName).toString());
//        FileOutputStream out = new FileOutputStream(new StringBuilder(filePath).append(fileName).toString());
        FileWriter fstream = new FileWriter(new StringBuilder(filePath).append(fileName).toString());
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(content);
        out.flush();
        out.close();

    }

    public static void mkDirectory(String directory){
        File file = new File(directory);
        if (!file.exists()){
            new File(directory).mkdirs();
        }
    }

    public static String classNamingConvention(String className){
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }
    public static String classVariableNamingConvention(String className){
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
