package com.esb.projet2;

import com.esb.projet2.exception.ResourceNotFoundException;
import com.esb.projet2.model.*;
import com.esb.projet2.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Tests de la logique métier (couche Service). Chaque test est annulé (rollback). */
@SpringBootTest
@Transactional
class ServiceTests {

    @Autowired private DepartmentService departmentService;
    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;
    @Autowired private ExamService examService;
    @Autowired private GradeService gradeService;

    private Student student(String email) {
        Student s = new Student();
        s.setName("Alice");
        s.setEmail(email);
        s.setAge(20);
        return studentService.createStudent(s);
    }

    private Course course(String code) {
        Course c = new Course();
        c.setTitle("Java");
        c.setCode(code);
        c.setCredits(4);
        return courseService.createCourse(c);
    }

    private Exam exam(Course c, double max) {
        Exam e = new Exam();
        e.setTitle("DS1");
        e.setExamDate(LocalDate.of(2026, 12, 1));
        e.setMaxScore(max);
        e.setCourse(c);
        return examService.createExam(e);
    }

    private Grade grade(Student s, Exam e, double score) {
        Grade g = new Grade();
        g.setScore(score);
        g.setStudent(s);
        g.setExam(e);
        return g;
    }

    @Test
    void getUnknownDepartment_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> departmentService.getDepartmentById(999999L));
    }

    @Test
    void createDepartmentWithSameName_isRejected() {
        Department d = new Department();
        d.setName("Informatique");
        departmentService.createDepartment(d);

        Department dup = new Department();
        dup.setName("Informatique");
        assertThrows(IllegalArgumentException.class, () -> departmentService.createDepartment(dup));
    }

    @Test
    void enrollTwice_isRejected() {
        Student s = student("alice@esb.tn");
        Course c = course("JAVA1");

        studentService.enrollStudentInCourse(s.getId(), c.getId());
        assertThrows(IllegalArgumentException.class,
                () -> studentService.enrollStudentInCourse(s.getId(), c.getId()));
    }

    @Test
    void gradeRequiresEnrollment_andScoreWithinMax() {
        Student s = student("alice@esb.tn");
        Course c = course("JAVA1");
        Exam e = exam(c, 20);

        // pas inscrit -> refusé
        assertThrows(IllegalArgumentException.class, () -> gradeService.createGrade(grade(s, e, 15)));

        studentService.enrollStudentInCourse(s.getId(), c.getId());

        // note > barème -> refusé
        assertThrows(IllegalArgumentException.class, () -> gradeService.createGrade(grade(s, e, 25)));

        // note valide -> accepté, puis doublon refusé
        Grade saved = gradeService.createGrade(grade(s, e, 16));
        assertNotNull(saved.getId());
        assertEquals("Très bien", saved.getMention());
        assertThrows(IllegalArgumentException.class, () -> gradeService.createGrade(grade(s, e, 12)));
    }

    @Test
    void averageIsComputedOn20() {
        Student s = student("alice@esb.tn");
        Course c = course("JAVA1");
        Exam e = exam(c, 10);
        studentService.enrollStudentInCourse(s.getId(), c.getId());
        gradeService.createGrade(grade(s, e, 8));

        assertEquals(16.0, studentService.getAverageOn20(s.getId()), 0.001);
    }

    @Test
    void averageWithoutGrades_throwsNotFound() {
        Student s = student("alice@esb.tn");
        assertThrows(ResourceNotFoundException.class, () -> studentService.getAverageOn20(s.getId()));
    }
}
