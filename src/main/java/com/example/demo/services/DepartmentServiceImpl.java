//
//package com.example.demo.services;
//
//import com.example.demo.dtos.DepartmentDTO;
//import com.example.demo.dtos.StudentDTO;
//import com.example.demo.entities.Department;
//import com.example.demo.entities.Student;
//import com.example.demo.repositories.DepartmentRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.BindingResult;
//
///**
// *
// * @author binod
// */
//@Service
//public class DepartmentServiceImpl implements DepartmentService{
//@Autowired
//private DepartmentRepository departmentRepository;
//    @Override
//    public void saveDepartment(Department department,BindingResult errors) {
//
//        departmentRepository.save(department);
//    }
//
//    @Override
//    public List<DepartmentDTO> findAllDepartments() {
//        List<Department> departmentList=departmentRepository.findAll();
//      return  departmentList
//              .stream()
//              .map(departmentDTOConverter)
//              .collect(Collectors.toList());
//
//    }
//
//    private Function<Department,DepartmentDTO> departmentDTOConverter=(d)->{
//        DepartmentDTO dto=new DepartmentDTO();
//        dto.setDepartmentId(d.getDepartmentId());
//        dto.setDepartmentName(d.getDepartmentName());
//         List<StudentDTO> studentList=new ArrayList<>();
//        for(Student s: d.getStudentList()){
//            StudentDTO stdDTO=new StudentDTO();
//            stdDTO.setFirstName(s.getFirstName());
//            stdDTO.setLastName(s.getLastName());
//            stdDTO.setAddress(s.getAddress());
//            studentList.add(stdDTO);
//        }
//        dto.setStudentList(studentList);
//       return dto;
//    };
//
//}
