package com.project.university_directory.controller;

import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.student_model.StudentDTO;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import com.project.university_directory.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Arrays;

@Controller
@RequestMapping("/general")
public class GeneralController {

    private final SecurityUserService securityUser;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final GroupService groupService;

    public GeneralController(SecurityUserService securityUser, TeacherService teacherService, StudentService studentService, SubjectService subjectService, GroupService groupService) {
        this.securityUser = securityUser;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.groupService = groupService;
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, Principal principal, Authentication authentication) {
        if (principal == null) {return "redirect:/login";}

        if (whichRole(authentication, "ROLE_ADMIN")) {
            model.addAttribute("teacher", teacherService.findByEmail(principal.getName()));
            model.addAttribute("allSubjects", subjectService.findAll());

            return "directory/teachers/teacher-profile-page";

        } else if (whichRole(authentication, "ROLE_TEACHER")) {
            Teacher teacher = teacherService.findByEmail(principal.getName());
            model.addAttribute("teacher", new TeacherDTO(teacher));
            model.addAttribute("updateTeacher", new TeacherDTO(teacher));
            model.addAttribute("allSubjects", subjectService.findAll());

            return "directory/teachers/teacher-profile-page";

        } else {
            Student student = studentService.findByEmail(principal.getName());
            model.addAttribute("student",
                    new StudentDTO(student));
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("updateStudent", new StudentDTO(student));

            return "directory/students/student-profile-page";
        }
    }

    private static boolean whichRole(Authentication authentication, String expectedRole) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(role -> Arrays.asList(role
                        .getAuthority()
                        .split(",")).contains(expectedRole));
    }
}
