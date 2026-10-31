package com.esb.projet2.controller;

import com.esb.projet2.model.Course;
import com.esb.projet2.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    // Cours suivis par un étudiant
    @GetMapping("/student/{studentId}")
    public List<Course> getCoursesByStudentId(@PathVariable Long studentId) {
        return courseService.getCoursesByStudentId(studentId);
    }

    // Cours ayant au moins N étudiants inscrits
    @GetMapping("/popular/{minStudents}")
    public List<Course> getPopularCourses(@PathVariable int minStudents) {
        return courseService.getPopularCourses(minStudents);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@Valid @RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/{courseId}/teacher/{teacherId}")
    public Course assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        return courseService.assignTeacherToCourse(courseId, teacherId);
    }

    @PostMapping("/{courseId}/department/{deptId}")
    public Course assignDepartmentToCourse(@PathVariable Long courseId, @PathVariable Long deptId) {
        return courseService.assignDepartmentToCourse(courseId, deptId);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @Valid @RequestBody Course courseDetails) {
        return courseService.updateCourse(id, courseDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourse(id);
    }
}
