package com.project.university_directory.repository;

import com.project.university_directory.model.teacher_model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("""
        SELECT s FROM Subject s
        WHERE (:search IS NULL OR s.subject LIKE %:search%)
    """)
    List<Subject> findAllFiltered(@Param("search") String search);

}
