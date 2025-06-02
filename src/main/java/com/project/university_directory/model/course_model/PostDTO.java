package com.project.university_directory.model.course_model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class PostDTO {
    private String title;

    private String content;

    MultipartFile[] files;

    public PostDTO() {
    }

    public PostDTO(String title, String content, MultipartFile[] files) {
        this.title = title;
        this.content = content;
        this.files = files;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
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
}
