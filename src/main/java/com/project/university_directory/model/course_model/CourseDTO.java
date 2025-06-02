package com.project.university_directory.model.course_model;
import com.project.university_directory.model.course_model.Course;
import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.teacher_model.Subject;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CourseDTO {

    private Long id;

//    @NotNull(message = "Course name is required")
//    @Size(min = 3, max = 50, message = "Course name must be between 3 and 50 characters")
    private String name;

//    @NotNull(message = "Course name is required")
//    @Size(min = 3, max = 50, message = "Course name must be between 3 and 50 characters")
    private String description;


//    @NotEmpty(message = "At least one teacher must be assigned")
    private List<TeacherDTO> teachers;

//    @NotEmpty(message = "At least one student must be enrolled")
    private List<Student> students;

    private List<Post> posts = new ArrayList<>();

    public CourseDTO(Long id, String name, String description, List<TeacherDTO> teachers, List<Student> students, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teachers = teachers;
        this.students = students;
        this.posts= posts;
    }

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.teachers = course.getTeachers().stream().map(TeacherDTO::new).collect(Collectors.toList());
        this.students = course.getStudents();
        this.posts = course.getPosts();
    }

    public CourseDTO() {}


}


