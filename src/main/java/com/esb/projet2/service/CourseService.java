package com.esb.projet2.service;

import com.esb.projet2.exception.ResourceNotFoundException;
import com.esb.projet2.model.Course;
import com.esb.projet2.model.Department;
import com.esb.projet2.model.Teacher;
import com.esb.projet2.repository.CourseRepository;
import com.esb.projet2.repository.ExamRepository;
import com.esb.projet2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    public List<Course> getCoursesByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
        return courseRepository.findCoursesByStudentId(studentId);
    }

    public List<Course> getPopularCourses(int minStudents) {
        if (minStudents < 0) {
            throw new IllegalArgumentException("minStudents must be positive");
        }
        return courseRepository.findPopularCourses(minStudents);
    }

    public Course createCourse(Course course) {
        if (courseRepository.existsByCode(course.getCode())) {
            throw new IllegalArgumentException("Course code already exists: " + course.getCode());
        }
        course.setId(null);
        course.setTeacher(resolveTeacher(course.getTeacher()));
        course.setDepartment(resolveDepartment(course.getDepartment()));
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = getCourseById(id);
        if (!course.getCode().equals(courseDetails.getCode())
                && courseRepository.existsByCode(courseDetails.getCode())) {
            throw new IllegalArgumentException("Course code already exists: " + courseDetails.getCode());
        }
        course.setTitle(courseDetails.getTitle());
        course.setCode(courseDetails.getCode());
        course.setCredits(courseDetails.getCredits());
        course.setTeacher(resolveTeacher(courseDetails.getTeacher()));
        course.setDepartment(resolveDepartment(courseDetails.getDepartment()));
        return courseRepository.save(course);
    }

    public Course assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Course assignDepartmentToCourse(Long courseId, Long deptId) {
        Course course = getCourseById(courseId);
        Department dept = departmentService.getDepartmentById(deptId);
        course.setDepartment(dept);
        return courseRepository.save(course);
    }

    public String deleteCourse(Long id) {
        Course course = getCourseById(id);
        if (!course.getStudents().isEmpty()) {
            throw new IllegalArgumentException("Course still has enrolled students");
        }
        if (examRepository.existsByCourseId(id)) {
            throw new IllegalArgumentException("Course still has exams");
        }
        courseRepository.delete(course);
        return "Course deleted";
    }

    private Teacher resolveTeacher(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        if (teacher.getId() == null) {
            throw new IllegalArgumentException("Teacher id is required");
        }
        return teacherService.getTeacherById(teacher.getId());
    }

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
