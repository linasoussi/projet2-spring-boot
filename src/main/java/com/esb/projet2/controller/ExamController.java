package com.esb.projet2.controller;

import com.esb.projet2.model.Exam;
import com.esb.projet2.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @GetMapping("/{id}")
    public Exam getExamById(@PathVariable Long id) {
        return examService.getExamById(id);
    }

    @GetMapping("/course/{courseId}")
    public List<Exam> getExamsByCourseId(@PathVariable Long courseId) {
        return examService.getExamsByCourseId(courseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exam createExam(@Valid @RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @PutMapping("/{id}")
    public Exam updateExam(@PathVariable Long id, @Valid @RequestBody Exam examDetails) {
        return examService.updateExam(id, examDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteExam(@PathVariable Long id) {
        return examService.deleteExam(id);
    }
}
