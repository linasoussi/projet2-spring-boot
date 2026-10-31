package com.esb.projet2.repository;

import com.esb.projet2.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    boolean existsByDepartmentId(Long departmentId);

    boolean existsByTeacherId(Long teacherId);

    // Requête personnalisée n°4 : cours suivis par un étudiant (relation ManyToMany)
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    // Requête personnalisée n°5 : cours "populaires" (au moins N étudiants inscrits)
    @Query("SELECT c FROM Course c WHERE SIZE(c.students) >= :minStudents")
    List<Course> findPopularCourses(@Param("minStudents") int minStudents);
}
