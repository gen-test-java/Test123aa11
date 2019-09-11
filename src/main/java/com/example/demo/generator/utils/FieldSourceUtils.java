package com.example.demo.generator.utils;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.util.Refactory;

import java.util.List;
import java.util.stream.Collectors;

public class FieldSourceUtils {

    public static class Builder {
        private String fieldName;
        private Class<?> type;
        private String typeString=null;

        public Builder setFieldName(String fieldName){
            this.fieldName = Do.classVariableNamingConvention(fieldName);
            return this;
        }

        public Builder setType(Class<?> type){
            this.type = type;
            return this;
        }
        public Builder setType(String type){
            this.typeString = type;
            return this;
        }
        public FieldSource<JavaClassSource> create(JavaClassSource javaClass) {
            List<FieldSource<JavaClassSource>> fieldSourceList= javaClass.getFields();
            List<String> fieldNames = fieldSourceList.stream().map(fieldName->fieldName.getName()).collect(Collectors.toList());

            if(fieldNames.stream().noneMatch(f-> fieldName.equalsIgnoreCase(f))){
                FieldSource<JavaClassSource> fieldSource = javaClass.addField().setName(fieldName).setPrivate();
                if (typeString!=null){
                    fieldSource.setType(typeString);
                }else {
                    fieldSource.setType(type);
                }
                Refactory.createGetterAndSetter(javaClass, fieldSource);
                return fieldSource;
            }

            return javaClass.getField(fieldName);
        }

    }
}
