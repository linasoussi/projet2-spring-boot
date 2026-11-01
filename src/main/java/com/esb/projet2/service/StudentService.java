package com.esb.projet2.service;

import com.esb.projet2.exception.ResourceNotFoundException;
import com.esb.projet2.model.Course;
import com.esb.projet2.model.Department;
import com.esb.projet2.model.Student;
import com.esb.projet2.model.StudentProfile;
import com.esb.projet2.model.StudentStatus;
import com.esb.projet2.repository.GradeRepository;
import com.esb.projet2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    public List<Student> getStudentsByDepartmentName(String departmentName) {
        return studentRepository.findStudentByDepartmentName(departmentName);
    }

    public List<Student> getStudentsByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status);
    }

    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already used: " + student.getEmail());
        }
        student.setId(null);
        student.setCourses(new HashSet<>());
        student.setDepartment(resolveDepartment(student.getDepartment()));
        if (student.getProfile() != null) {
            student.getProfile().setId(null);
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);
        if (!student.getEmail().equals(studentDetails.getEmail())
                && studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new IllegalArgumentException("Email already used: " + studentDetails.getEmail());
        }
        student.setName(studentDetails.getName());
        student.setAge(studentDetails.getAge());
        student.setEmail(studentDetails.getEmail());
        student.setStatus(studentDetails.getStatus());
        student.setDepartment(resolveDepartment(studentDetails.getDepartment()));

        StudentProfile newProfile = studentDetails.getProfile();
        if (newProfile != null) {
            if (student.getProfile() == null) {
                newProfile.setId(null);
                student.setProfile(newProfile);
            } else {
                student.getProfile().setAddress(newProfile.getAddress());
                student.getProfile().setPhone(newProfile.getPhone());
                student.getProfile().setBiography(newProfile.getBiography());
            }
        }
        return studentRepository.save(student);
    }

    public Student assignDepartmentToStudent(Long studentId, Long deptId) {
        Student student = getStudentById(studentId);
        Department dept = departmentService.getDepartmentById(deptId);
        student.setDepartment(dept);
        return studentRepository.save(student);
    }

    // Inscription d'un étudiant à un cours (relation ManyToMany)
    public Student enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);
        if (isEnrolled(student, courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        student.getCourses().add(course);
        return studentRepository.save(student);
    }

    public String unenrollStudentFromCourse(Long studentId, Long courseId) {
        Student student = getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);
        if (!isEnrolled(student, courseId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
        student.getCourses().removeIf(c -> c.getId().equals(course.getId()));
        studentRepository.save(student);
        return "Student unenrolled";
    }

    // Moyenne générale de l'étudiant sur 20
    public double getAverageOn20(Long studentId) {
        getStudentById(studentId);
        Double average = gradeRepository.findAverageOn20ByStudentId(studentId);
        if (average == null) {
            throw new ResourceNotFoundException("No grades found for student " + studentId);
        }
        return Math.round(average * 100.0) / 100.0;
    }

    public String deleteStudent(Long id) {
        Student student = getStudentById(id);
        if (gradeRepository.existsByStudentId(id)) {
            throw new IllegalArgumentException("Student still has grades");
        }
        studentRepository.delete(student);
        return "Student deleted";
    }

    public boolean isEnrolled(Student student, Long courseId) {
        return student.getCourses().stream().anyMatch(c -> c.getId().equals(courseId));
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
