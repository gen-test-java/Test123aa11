package com.example.demo.generator.ModelGenerators;

import com.example.demo.generator.extraGenerator.RepositoryGenerator;
import com.example.demo.generator.model.Attribute;
import com.example.demo.generator.model.Entity;
import com.example.demo.generator.model.EntitySet;
import com.example.demo.generator.model.Relation;
import com.example.demo.generator.utils.ClassSourceUtils;
import com.example.demo.generator.utils.Do;
import com.example.demo.generator.utils.FieldSourceUtils;
import com.example.demo.generator.utils.RelationType;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EntityGenerator {

    private String packageName;
    private String entityName;
    private Boolean sync;
    private String classPath;
    private String pathToEntity;

    @Autowired
    TaskExecutor taskExecutor;

    @Autowired
    RepositoryGenerator repositoryGenerator;

    public synchronized void generate(String projectDirectory,EntitySet entitySet) throws IOException {

        this.packageName=entitySet.getPackageName();
        this.sync=entitySet.getSerialize();
        this.pathToEntity = new StringBuilder(projectDirectory).append(entitySet.getPackageName().replaceAll("\\.", "/")).append("/").toString();

        Do.mkDirectory(pathToEntity);

        entitySet.getEntities().stream().forEach(e->{
            try {
                this.doGenerate(pathToEntity,e);
            } catch (IOException ex) {
               throw new RuntimeException(ex);
            }
        });

    }

    private void doGenerate(String pathToEntity,Entity entity) throws IOException {
        this.classPath = pathToEntity;

        ClassSourceUtils.Builder builder = new ClassSourceUtils.Builder()
                .setClassName(entity.getEntityName())
                .setPackageName(packageName)
                .isSerializable(sync);
        JavaClassSource javaClass =null;
        try {
            Boolean syncToDb = Boolean.valueOf(String.valueOf(entity.getMeta().get("syncToDb")));
            javaClass= builder.setSyncToDb(syncToDb)
                .createClass(classPath);
            if (!syncToDb && syncToDb != null) {
                javaClass.addImport("com.fasterxml.jackson.annotation.JsonIgnoreProperties");
                javaClass.addAnnotation("JsonIgnoreProperties").setLiteralValue("ignoreUnknown","true");
            }else {
                javaClass.addAnnotation(javax.persistence.Entity.class);
            }
        }catch (Exception e){
            System.out.println(entity.getEntityName());

            javaClass=builder.createClass(classPath);
            javaClass.addAnnotation(javax.persistence.Entity.class);
        }
//        entity.getAttributes().stream().forEach(f -> doGenerateField(javaClass,f)));
        for(Attribute attribute: entity.getAttributes()){
            doGenerateField(javaClass,attribute);
        }

        Do.writeFile(pathToEntity,new StringBuilder(Do.classNamingConvention(entity.getEntityName()))
                .append(".java").toString(),javaClass.toString());
    }

    private String doGenerateField(JavaClassSource javaClass, Attribute f) throws IOException {
        if (f.getRelation() == null) {
            javaClass.addImport(f.getTypeClass());
            try {
                FieldSource<JavaClassSource> fieldSource1 = new FieldSourceUtils.Builder().setFieldName(f.getName()).setType(f.getType()).create(javaClass);
                if (f.getMeta().getPrimaryId()) {
                    fieldSource1.addAnnotation(Id.class);

                    try {
                        taskExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                Entity entity = new Entity();
                                entity.setEntityName(javaClass.getName());
                                Map<String,Object> map = new HashMap<>();
                                map.put("packageName",packageName);
                                map.put("idType",f.getType());
                                entity.setMeta(map);
                                try {
                                    repositoryGenerator.doGenerate(pathToEntity,entity);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
            }
        }else {

            Relation relation = f.getRelation();
            javaClass.addImport(relation.getEntityClass());
            javaClass.addImport(List.class);
            FieldSource<JavaClassSource> fieldSource = new FieldSourceUtils.Builder().setFieldName(f.getName()).setType("List<" + Do.classNamingConvention(relation.getEntityName()) + ">").create(javaClass);

            if (relation.getBidirectional()) {
                JavaClassSource javaClass1= new ClassSourceUtils.Builder().setClassName(relation.getEntityName()).setPackageName(packageName).isSerializable(sync).createClass(classPath);

                String relationName = RelationType.reverseValue(relation.getRelationType());
                FieldSource<JavaClassSource> fieldSource1 =null;
                switch (relationName){
                    case "ManyToOne":
                    case "OneToOne":
                        fieldSource1 = new FieldSourceUtils.Builder().setFieldName(javaClass.getName()).setType(Do.classNamingConvention(javaClass.getName())).create(javaClass1);
                        fieldSource1.addAnnotation("JsonBackReference");

                        AnnotationSource annotationSource = fieldSource1.addAnnotation("JoinColumn");
                        annotationSource.setStringValue("name",javaClass.getName().toLowerCase()+"_id");
                        break;
                    case "OneToMany":
                    case "ManyToMany":
                        fieldSource1 = new FieldSourceUtils.Builder().setFieldName(javaClass.getName()).setType("List<"+Do.classNamingConvention(javaClass.getName())+">").create(javaClass1);
                        break;
                }

                javaClass1.addImport(new StringBuilder("javax.persistence.").append(relation.getRelationType()).toString());
                System.out.println(fieldSource1.toString());

                fieldSource1.addAnnotation(RelationType.reverseValue(relation.getRelationType()));

                Do.writeFile(classPath,new StringBuilder(Do.classNamingConvention(relation.getEntityName()))
                        .append(".java").toString(),javaClass1.toString());

            }
            for (String s : RelationType.values()) {
                if (relation.getRelationType().equalsIgnoreCase(s)) {
                    javaClass.addImport(new StringBuilder("javax.persistence.").append(s).toString());
                    AnnotationSource<JavaClassSource> annotationSource = fieldSource.addAnnotation(s);
                    annotationSource.setStringValue("mappedBy",javaClass.getName());
                    annotationSource.setStringValue("cascade","CascadeType.ALL");
                    annotationSource.setStringValue("fetch","FetchType.EAGER");

                    javaClass.addImport("com.fasterxml.jackson.annotation.JsonManagedReference");
                    fieldSource.addAnnotation("JsonManagedReference");
                    break;
                }
            }
            }

        return javaClass.toString();
    }

}
