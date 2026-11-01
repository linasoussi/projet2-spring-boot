package com.esb.projet2;

import com.esb.projet2.model.*;
import com.esb.projet2.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Tests des requêtes personnalisées des repositories (base H2 en mémoire). */
@DataJpaTest
class RepositoryTests {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ExamRepository examRepository;
    @Autowired private GradeRepository gradeRepository;

    private Department newDepartment(String name) {
        Department d = new Department();
        d.setName(name);
        return departmentRepository.save(d);
    }

    private Student newStudent(String name, String email, Department dept, StudentStatus status) {
        Student s = new Student();
        s.setName(name);
        s.setEmail(email);
        s.setAge(20);
        s.setStatus(status);
        s.setDepartment(dept);
        return studentRepository.save(s);
    }

    private Course newCourse(String code) {
        Course c = new Course();
        c.setTitle("Cours " + code);
        c.setCode(code);
        c.setCredits(5);
        return courseRepository.save(c);
    }

    @Test
    void findStudentByDepartmentName_returnsOnlyStudentsOfThatDepartment() {
        Department info = newDepartment("Informatique");
        Department gestion = newDepartment("Gestion");
        newStudent("Alice", "alice@esb.tn", info, StudentStatus.ACTIVE);
        newStudent("Bob", "bob@esb.tn", gestion, StudentStatus.ACTIVE);

        List<Student> result = studentRepository.findStudentByDepartmentName("Informatique");

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void findByStatus_returnsStudentsWithGivenStatus() {
        Department info = newDepartment("Informatique");
        newStudent("Alice", "alice@esb.tn", info, StudentStatus.ACTIVE);
        newStudent("Bob", "bob@esb.tn", info, StudentStatus.GRADUATED);

        assertEquals(1, studentRepository.findByStatus(StudentStatus.GRADUATED).size());
    }

    @Test
    void findTeachersByDepartmentName_works() {
        Department info = newDepartment("Informatique");
        Teacher t = new Teacher();
        t.setName("Dr Ben Ali");
        t.setEmail("benali@esb.tn");
        t.setDepartment(info);
        teacherRepository.save(t);

        assertEquals(1, teacherRepository.findTeachersByDepartmentName("Informatique").size());
        assertTrue(teacherRepository.findTeachersByDepartmentName("Gestion").isEmpty());
    }

    @Test
    void findCoursesByStudentId_and_findPopularCourses_work() {
        Student alice = newStudent("Alice", "alice@esb.tn", null, StudentStatus.ACTIVE);
        Student bob = newStudent("Bob", "bob@esb.tn", null, StudentStatus.ACTIVE);
        Course java = newCourse("JAVA1");
        Course sql = newCourse("SQL1");

        alice.getCourses().add(java);
        bob.getCourses().add(java);
        alice.getCourses().add(sql);
        studentRepository.saveAll(List.of(alice, bob));
        studentRepository.flush();

        assertEquals(2, courseRepository.findCoursesByStudentId(alice.getId()).size());
        assertEquals(1, courseRepository.findCoursesByStudentId(bob.getId()).size());

        List<Course> popular = courseRepository.findPopularCourses(2);
        assertEquals(1, popular.size());
        assertEquals("JAVA1", popular.get(0).getCode());
    }

    @Test
    void findAverageOn20ByStudentId_computesNormalizedAverage() {
        Student alice = newStudent("Alice", "alice@esb.tn", null, StudentStatus.ACTIVE);
        Course java = newCourse("JAVA1");

        Exam e1 = new Exam();
        e1.setTitle("DS1");
        e1.setExamDate(LocalDate.of(2026, 12, 1));
        e1.setMaxScore(20);
        e1.setCourse(java);
        e1 = examRepository.save(e1);

        Exam e2 = new Exam();
        e2.setTitle("DS2");
        e2.setExamDate(LocalDate.of(2026, 12, 15));
        e2.setMaxScore(10);
        e2.setCourse(java);
        e2 = examRepository.save(e2);

        Grade g1 = new Grade();
        g1.setScore(10.0);   // 10/20 -> 10 sur 20
        g1.setStudent(alice);
        g1.setExam(e1);
        gradeRepository.save(g1);

        Grade g2 = new Grade();
        g2.setScore(10.0);   // 10/10 -> 20 sur 20
        g2.setStudent(alice);
        g2.setExam(e2);
        gradeRepository.save(g2);

        Double avg = gradeRepository.findAverageOn20ByStudentId(alice.getId());
        assertNotNull(avg);
        assertEquals(15.0, avg, 0.001);
    }
}
