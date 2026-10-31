package com.esb.projet2.repository;

import com.esb.projet2.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByEmail(String email);

    boolean existsByDepartmentId(Long departmentId);

    // Requête personnalisée n°1 : enseignants d'un département (même principe que le Chapitre 3)
    @Query("SELECT t FROM Teacher t WHERE t.department.name = :departmentName")
    List<Teacher> findTeachersByDepartmentName(@Param("departmentName") String departmentName);
}
