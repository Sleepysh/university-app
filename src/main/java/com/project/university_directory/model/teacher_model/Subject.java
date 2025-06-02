package com.project.university_directory.model.teacher_model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="academ_subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "subject")
    String subject;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    List<Teacher> teachers = new ArrayList<>();

    public Subject() {
    }

    public Subject(Long id, String subject, List<Teacher> teachers) {
        this.id = id;
        this.subject = subject;
        this.teachers = teachers;
    }

    public Subject(String subject) {
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
