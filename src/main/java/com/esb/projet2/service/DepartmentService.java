package com.esb.projet2.service;

import com.esb.projet2.exception.ResourceNotFoundException;
import com.esb.projet2.model.Department;
import com.esb.projet2.repository.CourseRepository;
import com.esb.projet2.repository.DepartmentRepository;
import com.esb.projet2.repository.StudentRepository;
import com.esb.projet2.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
    }

    public Department createDepartment(Department dept) {
        if (departmentRepository.existsByName(dept.getName())) {
            throw new IllegalArgumentException("Department name already exists: " + dept.getName());
        }
        dept.setId(null);
        return departmentRepository.save(dept);
    }

    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department dept = getDepartmentById(id);
        if (!dept.getName().equals(updatedDepartment.getName())
                && departmentRepository.existsByName(updatedDepartment.getName())) {
            throw new IllegalArgumentException("Department name already exists: " + updatedDepartment.getName());
        }
        dept.setName(updatedDepartment.getName());
        return departmentRepository.save(dept);
    }

    public String deleteDepartment(Long id) {
        Department dept = getDepartmentById(id);
        // Règle métier : on ne supprime pas un département qui contient encore des données
        if (studentRepository.existsByDepartmentId(id)
                || teacherRepository.existsByDepartmentId(id)
                || courseRepository.existsByDepartmentId(id)) {
            throw new IllegalArgumentException("Department is not empty (students, teachers or courses linked)");
        }
        departmentRepository.delete(dept);
        return "Department deleted";
    }
}
