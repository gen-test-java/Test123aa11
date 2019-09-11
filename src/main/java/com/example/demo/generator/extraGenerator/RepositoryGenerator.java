package com.example.demo.generator.extraGenerator;

import com.example.demo.generator.model.Entity;
import com.example.demo.generator.utils.Do;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Component
public class RepositoryGenerator {
    public void doGenerate(String pathToEntity, Entity entity) throws IOException {
        String packageName = String.valueOf(entity.getMeta().get("packageName"));
        String idType = String.valueOf(entity.getMeta().get("idType"));
        String[] pathStrings = pathToEntity.split("/");
        StringBuilder pathToRepository = new StringBuilder();
        for (int i=0;i<pathStrings.length-1;i++){
            pathToRepository.append(pathStrings[i]).append("/");
        }
        pathToRepository.append("repository/");
        JavaInterfaceSource javaInterfaceSource = Roaster.create(JavaInterfaceSource.class);
        String fileName = Do.classNamingConvention(entity.getEntityName())+"Repository";
        javaInterfaceSource.setName(fileName);
        javaInterfaceSource.setPackage(packageName);
        javaInterfaceSource.addAnnotation(Repository.class);
        javaInterfaceSource.addImport(JpaRepository.class);

        try {
            javaInterfaceSource.addInterface(JpaRepository.class.getSimpleName() +
                    "<" + Do.classNamingConvention(entity.getEntityName()) + ", " + idType + ">");
        }catch (IndexOutOfBoundsException iex){
            javaInterfaceSource.addInterface(JpaRepository.class.getSimpleName() +
                    "<" + Do.classNamingConvention(entity.getEntityName()) + ", " +Do.classNamingConvention(idType) + ">");
        }
        Do.mkDirectory(pathToRepository.toString());
        Do.writeFile(pathToRepository.toString(),fileName+".java",javaInterfaceSource.toString());
    }
}
