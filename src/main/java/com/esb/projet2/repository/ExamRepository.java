package com.esb.projet2.repository;

import com.esb.projet2.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByCourseId(Long courseId);

    boolean existsByCourseId(Long courseId);
}
