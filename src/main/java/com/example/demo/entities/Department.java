//
//package com.example.demo.entities;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author binod
// */
//@Entity
//public class Department {
//    @Id
//    @GeneratedValue
//    @Column
//    private Long departmentId;
//    @Column(unique = true)
//    private String departmentName;
//
//    @JsonManagedReference
//@OneToMany(mappedBy = "department",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    List<Student> studentList=new ArrayList<>();
//
//    public Long getDepartmentId() {
//        return departmentId;
//    }
//
//    public void setDepartmentId(Long departmentId) {
//        this.departmentId = departmentId;
//    }
//
//    public String getDepartmentName() {
//        return departmentName;
//    }
//
//    public void setDepartmentName(String departmentName) {
//        this.departmentName = departmentName;
//    }
//
//    public List<Student> getStudentList() {
//        return studentList;
//    }
//
//    public void setStudentList(List<Student> studentList) {
//        this.studentList = studentList;
//    }
//
//
//
//}
