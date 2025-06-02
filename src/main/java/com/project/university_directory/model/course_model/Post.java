package com.project.university_directory.model.course_model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_role")
    @NotNull
    private String role;

    @Column(name = "sender")
    @NotNull
    private String sender;

    @Column(name = "text_title")
    @NotNull
    private String title;

    @Column(name = "text_content")
    private String content;

    @Column(name = "date_time")
    private LocalDateTime localDateTime;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileData> files = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Post(String role, String sender, String title, String content, LocalDateTime localDateTime, List<FileData> files, Course course) {
        this.role = role;
        this.sender = sender;
        this.title = title;
        this.content = content;
        this.localDateTime = localDateTime;
        this.files = files;
        this.course = course;
    }

    public Post() {
    }

    public Post(Long id, String role, String sender, String title, String content, LocalDateTime localDateTime, List<FileData> files, Course course) {
        this.id = id;
        this.role = role;
        this.sender = sender;
        this.title = title;
        this.content = content;
        this.localDateTime = localDateTime;
        this.files = files;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public List<FileData> getFiles() {
        return files;
    }

    public void setFiles(List<FileData> files) {
        this.files = files;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
