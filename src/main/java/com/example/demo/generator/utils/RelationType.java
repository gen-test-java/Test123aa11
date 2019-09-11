package com.example.demo.generator.utils;

public class RelationType {
 private static String OneToMany = "OneToMany";
 private static String ManyToOne = "ManyToOne";
 private static String ManyToMany = "ManyToMany";
 private static String OneToOne = "OneToOne";

 public static String[] values() {
  return new String[]{OneToMany, ManyToMany, ManyToOne, OneToOne};
 }

 public static String reverseValue(String value) {
  switch (value.toLowerCase()) {
   case "onetomany":
    return ManyToOne;
   case "manytoone":
    return OneToMany;
   case "manytomany":
    return ManyToMany;
   case "onetoone":
    return OneToOne;
    default:
  }
  return null;
 }

}