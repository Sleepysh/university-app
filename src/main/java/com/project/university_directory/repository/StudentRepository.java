package com.project.university_directory.repository;

import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.teacher_model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
     Optional<Student> findBySecurityUser_Email(String email);
     @Query("""
         SELECT s FROM Student s
         LEFT JOIN s.group g
         WHERE (:email IS NULL OR s.securityUser.email LIKE %:email%)
          AND (:firstName IS NULL OR s.firstName LIKE %:firstName%)
          AND (:lastName IS NULL OR s.lastName LIKE %:lastName%)
          AND (:telephoneNumber IS NULL OR s.telephoneNumber LIKE %:telephoneNumber%)
          AND (:age IS NULL OR s.age = :age)
          AND (:group IS NULL OR CONCAT(g.course, g.groupNumber, g.name) LIKE %:group%)
          AND (:roomNumber IS NULL OR s.roomNumber = :roomNumber)
          AND (:speciality IS NULL OR g.speciality LIKE %:speciality%)
     """)
     List<Student> filterStudents(
             @Param("email") String email,
             @Param("firstName") String firstName,
             @Param("lastName") String lastName,
             @Param("telephoneNumber") String telephoneNumber,
             @Param("age") Integer age,
             @Param("group") String group,
             @Param("roomNumber") Integer roomNumber,
             @Param("speciality") String speciality
     );

     @Query("SELECT s FROM Student s WHERE s.securityUser.email IN :emails")
     List<Student> findByEmails(@Param("emails") List<String> emails);

     @Modifying
     @Query(value = "DELETE FROM students_courses WHERE student_id = :studentId", nativeQuery = true)
     void removeStudentFromCourses(@Param("studentId") Long studentId);

}
