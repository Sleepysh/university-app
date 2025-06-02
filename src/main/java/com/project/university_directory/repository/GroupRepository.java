package com.project.university_directory.repository;

import com.project.university_directory.model.student_model.Group;
import com.project.university_directory.model.student_model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g WHERE CONCAT(g.course, g.groupNumber, g.name) = :groupCode")
    Optional<Group> findByGroupCode(@Param("groupCode") String groupCode);

    @Query("""
        SELECT g FROM Group g
        WHERE (:name IS NULL OR LOWER(CONCAT(g.course, g.groupNumber, g.name)) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:speciality IS NULL OR LOWER(g.speciality) LIKE LOWER(CONCAT('%', :speciality, '%')))
        AND (:course IS NULL OR g.course = :course)
    """)
    List<Group> filterGroup(
            @Param("name") String name,
            @Param("speciality") String speciality,
            @Param("course") Short course
    );
}
