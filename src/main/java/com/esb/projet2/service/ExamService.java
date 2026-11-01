package com.esb.projet2.service;

import com.esb.projet2.exception.ResourceNotFoundException;
import com.esb.projet2.model.Course;
import com.esb.projet2.model.Exam;
import com.esb.projet2.repository.ExamRepository;
import com.esb.projet2.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private CourseService courseService;

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + id));
    }

    public List<Exam> getExamsByCourseId(Long courseId) {
        courseService.getCourseById(courseId);
        return examRepository.findByCourseId(courseId);
    }

    public Exam createExam(Exam exam) {
        exam.setId(null);
        exam.setCourse(resolveCourse(exam.getCourse()));
        return examRepository.save(exam);
    }

    public Exam updateExam(Long id, Exam examDetails) {
        Exam exam = getExamById(id);
        exam.setTitle(examDetails.getTitle());
        exam.setExamDate(examDetails.getExamDate());
        exam.setMaxScore(examDetails.getMaxScore());
        exam.setCourse(resolveCourse(examDetails.getCourse()));
        return examRepository.save(exam);
    }

    public String deleteExam(Long id) {
        Exam exam = getExamById(id);
        if (gradeRepository.existsByExamId(id)) {
            throw new IllegalArgumentException("Exam still has grades");
        }
        examRepository.delete(exam);
        return "Exam deleted";
    }

    private Course resolveCourse(Course course) {
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException("Course id is required");
        }
        return courseService.getCourseById(course.getId());
    }
}
