package com.esb.projet2.service;

import com.esb.projet2.exception.ResourceNotFoundException;
import com.esb.projet2.model.Department;
import com.esb.projet2.model.Teacher;
import com.esb.projet2.repository.CourseRepository;
import com.esb.projet2.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentService departmentService;

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id " + id));
    }

    public List<Teacher> getTeachersByDepartmentName(String departmentName) {
        return teacherRepository.findTeachersByDepartmentName(departmentName);
    }

    public Teacher createTeacher(Teacher teacher) {
        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("Email already used: " + teacher.getEmail());
        }
        teacher.setId(null);
        teacher.setDepartment(resolveDepartment(teacher.getDepartment()));
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = getTeacherById(id);
        if (!teacher.getEmail().equals(teacherDetails.getEmail())
                && teacherRepository.existsByEmail(teacherDetails.getEmail())) {
            throw new IllegalArgumentException("Email already used: " + teacherDetails.getEmail());
        }
        teacher.setName(teacherDetails.getName());
        teacher.setEmail(teacherDetails.getEmail());
        teacher.setSpecialty(teacherDetails.getSpecialty());
        teacher.setDepartment(resolveDepartment(teacherDetails.getDepartment()));
        return teacherRepository.save(teacher);
    }

    public Teacher assignDepartmentToTeacher(Long teacherId, Long deptId) {
        Teacher teacher = getTeacherById(teacherId);
        Department dept = departmentService.getDepartmentById(deptId);
        teacher.setDepartment(dept);
        return teacherRepository.save(teacher);
    }

    public String deleteTeacher(Long id) {
        Teacher teacher = getTeacherById(id);
        if (courseRepository.existsByTeacherId(id)) {
            throw new IllegalArgumentException("Teacher still teaches courses");
        }
        teacherRepository.delete(teacher);
        return "Teacher deleted";
    }

    // Recharge le département depuis la base à partir de son id (404 s'il n'existe pas)
    private Department resolveDepartment(Department department) {
        if (department == null) {
            return null;
        }
        if (department.getId() == null) {
            throw new IllegalArgumentException("Department id is required");
        }
        return departmentService.getDepartmentById(department.getId());
    }
}
