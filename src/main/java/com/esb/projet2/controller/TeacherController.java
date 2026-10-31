package com.esb.projet2.controller;

import com.esb.projet2.model.Teacher;
import com.esb.projet2.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    // Recherche des enseignants par nom de département
    @GetMapping("/department/{departmentName}")
    public List<Teacher> getTeachersByDepartmentName(@PathVariable String departmentName) {
        return teacherService.getTeachersByDepartmentName(departmentName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher createTeacher(@Valid @RequestBody Teacher teacher) {
        return teacherService.createTeacher(teacher);
    }

    // Affectation d'un enseignant à un département
    @PostMapping("/{teacherId}/department/{deptId}")
    public Teacher assignDepartmentToTeacher(@PathVariable Long teacherId, @PathVariable Long deptId) {
        return teacherService.assignDepartmentToTeacher(teacherId, deptId);
    }

    @PutMapping("/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @Valid @RequestBody Teacher teacherDetails) {
        return teacherService.updateTeacher(id, teacherDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        return teacherService.deleteTeacher(id);
    }
}
