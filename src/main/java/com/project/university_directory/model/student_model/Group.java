package com.project.university_directory.model.student_model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "course")
    private short course;

    @Column(name = "group_number")
    private short groupNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "speciality")
    private String speciality;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();

    public Group(short course, short groupNumber, String name, String speciality) {
        this.course = course;
        this.groupNumber = groupNumber;
        this.name = name;
        this.speciality = speciality;
    }
}
