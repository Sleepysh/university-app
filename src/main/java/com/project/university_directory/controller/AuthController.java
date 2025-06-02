package com.project.university_directory.controller;

import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.service.SecurityUserService;
import com.project.university_directory.service.StudentService;
import com.project.university_directory.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    @GetMapping("/loginForm")
    public String getLoginPage() {
        return "auth/login-form";
    }




}
