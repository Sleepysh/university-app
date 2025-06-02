package com.project.university_directory.service;

import com.project.university_directory.model.course_model.*;
import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import com.project.university_directory.repository.CourseRepository;
import com.project.university_directory.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final PostRepository postRepository;
    private final StudentService studentService;


    public CourseService(CourseRepository courseRepository, TeacherService teacherService, PostRepository postRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.postRepository = postRepository;
        this.studentService = studentService;
    }


    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public void deleteById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.getTeachers().clear();
        course.getStudents().clear();
        courseRepository.save(course);

        courseRepository.deleteById(id);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Course by this id" + id + " is not exist!"));
    }

    @Transactional
    public void update(CourseDTO changedCourse, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course by " + id + " id wasn't find"));

        if (changedCourse.getTeachers() != null ) {
            List<Teacher> teachers = new ArrayList<>();
            for (TeacherDTO teacher : changedCourse.getTeachers()) {
                teachers.add(teacherService.findByEmail(teacher.getEmail()));
            }
            course.setTeachers(teachers);
        }

        if (changedCourse.getName() != null && !(changedCourse.getName().isEmpty())
                && !changedCourse.getName().equals(course.getName()))
            course.setName(changedCourse.getName());

        if (changedCourse.getDescription() != null && !(changedCourse.getDescription().isEmpty())
                && !changedCourse.getDescription().equals(course.getDescription())) {
            course.setDescription(changedCourse.getDescription());
        }

        if (changedCourse.getStudents() != null) {
            course.setStudents(studentService.findByEmails(changedCourse.getStudents().stream()
                    .map(student -> student.getSecurityUser().getEmail()).toList()));
        }

        courseRepository.save(course);
    }

    @Transactional
    public void addStudentByEmail(String studentEmail, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course by " + id + " id wasn't find"));

        if(course.getStudents().stream()
                .map(student -> student.getSecurityUser().getEmail())
                .noneMatch(email -> email.equals(studentEmail))) {

            course.getStudents().add(studentService.findByEmail(studentEmail));
        }

        courseRepository.save(course);
    }

    public void addStudentsByGroup(String group, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course by " + id + " id wasn't find"));

        List<Student> students = studentService.getStudentsFromGroup(group);

        for (Student student : students) {
            if(!course.getStudents().contains(student)) {
                course.getStudents().add(student);
            }
        }

        courseRepository.save(course);
    }
    public void sendPost(PostDTO postDTO, Long courseId, String teachersEmail) {
        Teacher teacher = teacherService.findByEmail(teachersEmail);
        Post post = new Post();

        post.setLocalDateTime(LocalDateTime.now());
        post.setSender(teacher.getFirstName() + " " + teacher.getLastName());
        post.setCourse(findById(courseId));
        post.setRole("TEACHER");
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());

        // Mapping files and filtering out nulls or empty files
        List<FileData> fileDataList = Arrays.stream(postDTO.getFiles())
                .filter(file -> !file.isEmpty()) // Ignore empty files
                .map(file -> {
                    try {
                        return new FileData(file.getOriginalFilename(), file.getContentType(), file.getBytes(), post);
                    } catch (Exception e) {
                        // Log the error
                        System.err.println("Failed to process file: " + file.getOriginalFilename());
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Remove null entries
                .collect(Collectors.toList());

        post.setFiles(fileDataList);

        // Save the post
        postRepository.save(post);
    }

    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }


    @Transactional
    public void removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course by " + courseId + " id wasn't find"));

        Student student = studentService.findById(studentId);

        if(course.getStudents().stream()
                .anyMatch(tempStudent -> tempStudent.equals(student))) {
            course.getStudents().remove(student);
        }
        courseRepository.save(course);
    }


    public void updatePost(Long courseId, Post updatedPost, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post by id " + postId + " is not exist"));

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());

        postRepository.save(post);
    }

    public List<Course> findAllTeachersCourses(String email) {
        return courseRepository.findCoursesCreatedTeacherByEmail(email);
    }

    public Object findAllStudentsCourses(String email) {
        return courseRepository.findCoursesContainedStudentsEmail(email);
    }
}
