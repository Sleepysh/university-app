package com.project.university_directory.repository;

import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.teacher_model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findBySecurityUser_Email(String email);

    @Query("""
                SELECT DISTINCT t FROM Teacher t
                JOIN t.securityUser su
                LEFT JOIN t.subjects s
                WHERE (:firstName IS NULL OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
                  AND (:lastName IS NULL OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
                  AND (:telephoneNumber IS NULL OR LOWER(t.telephoneNumber) LIKE LOWER(CONCAT('%', :telephoneNumber, '%')))
                  AND (:email IS NULL OR LOWER(su.email) LIKE LOWER(CONCAT('%', :email, '%')))
                  AND (:age IS NULL OR t.age = :age)
                  AND (:subjectNames IS NULL OR s.subject IN :subjectNames)
            """)
    List<Teacher> filterTeachers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("telephoneNumber") String telephoneNumber,
            @Param("email") String email,
            @Param("age") Integer age,
            @Param("subjectNames") List<String> subjectNames
    );
    @Query("SELECT t FROM Teacher t WHERE t.securityUser.email IN :emails")
    List<Teacher> findByEmails(@Param("emails") List<String> emails);
}
