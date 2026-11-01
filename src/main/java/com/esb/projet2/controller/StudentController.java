package com.esb.projet2.controller;

import com.esb.projet2.model.Student;
import com.esb.projet2.model.StudentStatus;
import com.esb.projet2.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // Recherche des étudiants par nom de département (Chapitre 3 - slide 15)
    @GetMapping("/department/{departmentName}")
    public List<Student> getStudentsByDepartmentName(@PathVariable String departmentName) {
        return studentService.getStudentsByDepartmentName(departmentName);
    }

    // Recherche des étudiants par statut (ACTIVE, SUSPENDED, GRADUATED)
    @GetMapping("/status/{status}")
    public List<Student> getStudentsByStatus(@PathVariable StudentStatus status) {
        return studentService.getStudentsByStatus(status);
    }

    // Moyenne générale de l'étudiant (sur 20)
    @GetMapping("/{id}/average")
    public Map<String, Object> getAverage(@PathVariable Long id) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("studentId", id);
        result.put("average", studentService.getAverageOn20(id));
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentService.createStudent(student);
    }

    // Affectation d'un étudiant à un département (Chapitre 3 - slide 16)
    @PostMapping("/{studentId}/department/{deptId}")
    public Student assignDepartmentToStudent(@PathVariable Long studentId, @PathVariable Long deptId) {
        return studentService.assignDepartmentToStudent(studentId, deptId);
    }

    // Inscription à un cours
    @PostMapping("/{studentId}/courses/{courseId}")
    public Student enrollStudentInCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.enrollStudentInCourse(studentId, courseId);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        return studentService.updateStudent(id, studentDetails);
    }

    // Désinscription d'un cours
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public String unenrollStudentFromCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.unenrollStudentFromCourse(studentId, courseId);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }
}
