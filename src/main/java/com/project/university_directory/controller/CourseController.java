package com.project.university_directory.controller;

import com.project.university_directory.model.course_model.*;
import com.project.university_directory.model.teacher_model.Subject;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.repository.FileDataRepository;
import com.project.university_directory.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final CourseService courseService;
    private final FileDataRepository fileDataRepository;
    private final FileService fileService;

    public CourseController(StudentService studentService, TeacherService teacherService, SubjectService subjectService, CourseService courseService, FileDataRepository fileDataRepository, FileService fileService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.courseService = courseService;
        this.fileDataRepository = fileDataRepository;
        this.fileService = fileService;
    }

    @GetMapping("/view")
    public String getUsersClasses(Authentication authentication, Model model) {
        if (whichRole(authentication, "ROLE_ADMIN")) {
            model.addAttribute("courses", courseService.findAll());

        } else if (whichRole(authentication, "ROLE_TEACHER")) {
            model.addAttribute("courses", courseService.findAllTeachersCourses(authentication.getName()));

        } else if (whichRole(authentication, "ROLE_STUDENT")) {
            model.addAttribute("courses", courseService.findAllStudentsCourses(authentication.getName()));
        }

        return "directory/courses/courses-page";
    }

    @GetMapping("/save")
    public String saveCourse(Principal principal, Model model) {
        courseService.save(new Course("New Course", "none", List.of(teacherService.findByEmail(principal.getName()))));

        return "redirect:/courses/view";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteById(id);
        return "redirect:/courses/view";
    }

    @GetMapping("/details/{id}")
    public String viewCourseDetails(@PathVariable Long id, Model model) {
        model.addAttribute("course", new CourseDTO(courseService.findById(id)));
        model.addAttribute("post", new PostDTO());

        return "directory/courses/course-details";
    }


    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("course") CourseDTO changedCourse) {
        courseService.update(changedCourse, id);
        return "redirect:/courses/details/{id}";
    }

    @PostMapping("/add/student/{id}")
    public String addStudentByEmail(@PathVariable Long id, @ModelAttribute("studentEmail") String studentEmail) {
        courseService.addStudentByEmail(studentEmail, id);
        return "redirect:/courses/details/{id}";
    }

    @PostMapping("/add/group/{courseId}")
    public String addStudentsByGroup(@PathVariable Long courseId, @ModelAttribute("groupName") String groupName) {
        courseService.addStudentsByGroup(groupName, courseId);
        return "redirect:/courses/details/{courseId}";
    }

    @GetMapping("/remove/{courseId}/student/{studentId}")
    public String removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.removeStudentFromCourse(courseId, studentId);
        return "redirect:/courses/details/" + courseId;
    }

    @PostMapping("/{courseId}/post/add")
    public String sendPost(
            @PathVariable Long courseId,
            @ModelAttribute PostDTO postDTO,
            Principal principal) throws IOException {

        courseService.sendPost(postDTO, courseId, principal.getName());

        return "redirect:/courses/details/" + courseId;
    }

    @GetMapping("/{courseId}/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId,
                             @PathVariable Long courseId) {
        courseService.deletePostById(postId);
        return "redirect:/courses/details/" + courseId;
    }


    @PostMapping("/{courseId}/post/{postId}/update")
    public String updatePost(@PathVariable Long courseId,
                             @PathVariable Long postId,
                             @ModelAttribute Post post) {

        courseService.updatePost(courseId, post, postId);

        return "redirect:/courses/details/" + courseId;
    }

    private static boolean whichRole(Authentication authentication, String expectedRole) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(role -> Arrays.asList(role
                        .getAuthority()
                        .split(",")).contains(expectedRole));
    }

}

