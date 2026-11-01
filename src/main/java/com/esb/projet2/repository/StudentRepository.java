package com.esb.projet2.repository;

import com.esb.projet2.model.Student;
import com.esb.projet2.model.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByDepartmentId(Long departmentId);

    // Requête personnalisée n°2 : étudiants d'un département (Chapitre 3 - slide 12)
    @Query("SELECT s FROM Student s WHERE s.department.name = :departmentName")
    List<Student> findStudentByDepartmentName(@Param("departmentName") String departmentName);

    // Requête personnalisée n°3 : étudiants selon leur statut (requête dérivée Spring Data JPA)
    List<Student> findByStatus(StudentStatus status);
}
