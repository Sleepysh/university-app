package com.project.university_directory.controller;

import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import com.project.university_directory.service.FilterService;
import com.project.university_directory.service.SecurityUserService;
import com.project.university_directory.service.SubjectService;
import com.project.university_directory.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final FilterService filterService;
    private final SubjectService subjectService;
    private final SecurityUserService userService;

    public TeacherController(TeacherService theteacherService, FilterService filterService, SubjectService subjectService, SecurityUserService userService) {
        this.teacherService = theteacherService;
        this.filterService = filterService;
        this.subjectService = subjectService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String getTeachersList(Model model) {
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("teachers",  teacherService.findAll());
        model.addAttribute("filter", new TeacherDTO());
        return "/directory/teachers/teachers-list";
    }


    @GetMapping("/list/filtered")
    public String getFilteredTeachersList(@ModelAttribute() TeacherDTO teacherDTO, Model model) {
        List<Teacher> teacherList= teacherService.findAll();

        if (teacherDTO != null && !teacherList.isEmpty()) {
            teacherList = filterService.filterTeacher(teacherDTO);
        }

        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("filter", teacherDTO);
        model.addAttribute("teachers", teacherList);
        return "directory/teachers/teachers-list";
    }

    @GetMapping("/create/form")
    public String getTeacherForm(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        model.addAttribute("allSubjects", subjectService.findAll());
        return "directory/teachers/teacher-form";
    }

    @PostMapping("/save")
    public String registerTeacher(@ModelAttribute("teacher") @Valid TeacherDTO teacherDTO,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("teacher", teacherDTO);
            model.addAttribute("allSubjects", subjectService.findAll());
            return "directory/teachers/teacher-form";
        } else if (userService.isEmailExist(teacherDTO.getEmail())) {
            model.addAttribute("suchEmailAlreadyExist", "This email is already registered");
            model.addAttribute("teacher", teacherDTO);
            model.addAttribute("allSubjects", subjectService.findAll());
            return "directory/teachers/teacher-form";
        }

        teacherService.registerTeacher(teacherDTO);
        return "redirect:/general/profile";
    }

    @GetMapping("/update/form")
    public  String getUpdateTeacherForm(@RequestParam Long id, Model model) {
        model.addAttribute("teacher", teacherService.findDTOById(id));
        return "directory/teachers/teacher-update";
    }

    @PostMapping("/update")
    public String updateTeacher(@ModelAttribute("teacher") @Valid TeacherDTO teacher,
                                BindingResult bindingResult,
                                Model model) {

        if (hasErrorInCommonField(bindingResult)) {
            model.addAttribute("teacher", teacher);
            return "directory/teachers/teacher-update";
        }

        teacherService.update(teacher);
        return "redirect:/teachers/list";
    }

    @PostMapping("/profile/update")
    public String updateTeacherProfile(@Valid @ModelAttribute("teacher") TeacherDTO teacher,
                                       BindingResult bindingResult,
                                       @RequestParam(value = "subjectIds", required = false) List<Long> subjectIds,
                                       @RequestParam(value = "currentPassword", required = false) String currentPassword,
                                       RedirectAttributes redirectAttributes,
                                       Principal principal,
                                       Model model) {


        if (hasErrorInCommonField(bindingResult)) {
            redirectAttributes.addFlashAttribute("toastValidError", "Validation error occurred");
            return "redirect:/teachers/profile";
        }

        SecurityUser user = userService.findByEmail(principal.getName());

        if (!currentPassword.isEmpty() && !userService.isPasswordMatch(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("toastError", "Current password is incorrect!");
            return "redirect:/teachers/profile";
        }

        if (Optional.ofNullable(teacher.getPassword()).isPresent() && !teacher.getPassword().isBlank()) {
            if (bindingResult.hasFieldErrors("password")) {
                redirectAttributes.addFlashAttribute("toastValidError", "Validation error occurred");
                return "redirect:/teachers/profile";
            }
        }

        teacherService.updateProfile(teacher, subjectIds);

        redirectAttributes.addFlashAttribute("toastSuccess", "Profile updated successfully!");
        return "redirect:/teachers/profile";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, Principal principal) {

            Teacher teacher = teacherService.findByEmail(principal.getName());
            model.addAttribute("teacher", new TeacherDTO(teacher));
            model.addAttribute("updateTeacher", new TeacherDTO(teacher));
            model.addAttribute("allSubjects", subjectService.findAll());

            return "directory/teachers/teacher-profile-page";
    }

    private static boolean hasErrorInCommonField(BindingResult bindingResult) {
        return bindingResult.hasFieldErrors("firstName")
                || bindingResult.hasFieldErrors("lastName")
                || bindingResult.hasFieldErrors("telephoneNumber")
                || bindingResult.hasFieldErrors("age");
    }

    @GetMapping("/delete")
    public String deleteTeacher(@RequestParam String email, Principal principal) {
        if (principal.getName().equals(email)) {
            teacherService.deleteTeacher(email);
            return "redirect:/logout";
        }
        teacherService.deleteTeacher(email);
        return "redirect:/teachers/list";
    }

    @GetMapping("/profile/review/{teacherId}")
    public String reviewTeacher(@PathVariable Long teacherId, Model model) {
        model.addAttribute("teacher", teacherService.findDTOById(teacherId));
        return "directory/teachers/teacher-review";
    }

    @GetMapping("/subjects")
    public String viewSubjects(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "directory/teachers/subjects";
    }

    @PostMapping("/subjects/add")
    public String createSubject(@RequestParam("subject") String subjectName) {
        subjectService.saveSubjects(subjectName);
        return "redirect:/teachers/subjects";
    }

    @GetMapping("/subjects/filtered")
    public String viewSubjectsFiltered(@RequestParam("search") String search, Model model) {
        model.addAttribute("subjects", subjectService.findAllFiltered(search));
        return "directory/teachers/subjects";
    }

    @PostMapping("/subjects/edit")
    public String editSubject(@RequestParam("id") Long subjectId,
                              @RequestParam("subject") String subjectName) {

        subjectService.updateSubject(subjectId, subjectName);
        return "redirect:/teachers/subjects";
    }

    @PostMapping("/subjects/delete")
    public String deleteSubject(@RequestParam("id") Long subjectId) {

        subjectService.deleteSubject(subjectId);
        return "redirect:/teachers/subjects";
    }
}
