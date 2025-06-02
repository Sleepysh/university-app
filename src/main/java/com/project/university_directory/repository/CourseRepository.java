package com.project.university_directory.repository;

import com.project.university_directory.model.course_model.Course;
import com.project.university_directory.model.student_model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT DISTINCT c FROM Course c " +
            "JOIN c.teachers t " +
            "JOIN t.securityUser su " +
            "WHERE su.email = :email")
    List<Course> findCoursesCreatedTeacherByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT c FROM Course c " +
            "JOIN c.students s " +
            "JOIN s.securityUser su " +
            "WHERE su.email = :email")
    List<Course> findCoursesContainedStudentsEmail(@Param("email") String email);

}
