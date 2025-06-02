package com.project.university_directory.model.course_model;

import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.teacher_model.Subject;
import com.project.university_directory.model.teacher_model.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.ALL})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "teachers_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.ALL})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "students_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public Course(String name, String description, List<Teacher> teachers) {
        this.name = name;
        this.teachers = teachers;
        this.description = description;
    }

    public Course() {
    }


}
