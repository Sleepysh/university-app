package com.project.university_directory.controller;

import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.model.student_model.Group;
import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.student_model.StudentDTO;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import com.project.university_directory.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    private final GroupService groupService;

    private final FilterService filterService;
    private final SecurityUserService userService;


    @Autowired
    public StudentController (StudentService theStudentService, TeacherService theTeacherService, GroupService groupService, FilterService theFilterService, SecurityUserService userService) {
        this.studentService = theStudentService;
        this.teacherService = theTeacherService;
        this.groupService = groupService;
        this.filterService = theFilterService;
        this.userService = userService;
    }


    public String getStudentProfilePage(Model model, String email) {

        model.addAttribute("student", studentService.findByEmail(email));

        return "directory/students/student-profile-page";

    }

    @GetMapping("/list")
    public String getStudentsList(Model model, RedirectAttributes redirectAttrs) {

        redirectAttrs.addFlashAttribute("flashMessage", "Success message");
        redirectAttrs.addFlashAttribute("flashType", "success");
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("filter", new StudentDTO());
        return "directory/students/students-list";
    }

    @GetMapping("/list/filtered")
    public String getFilteredStudentsList(@ModelAttribute() StudentDTO studentDTO, Model model) {
        List<Student> studentList = studentService.findAll();

        if (studentDTO != null && !studentList.isEmpty()) {
            studentList = filterService.filterStudents(studentDTO);
        }

        model.addAttribute("filter", new StudentDTO());
        model.addAttribute("students", studentList);
        return "directory/students/students-list";
    }

    @GetMapping("/create/form")
    public String getStudentForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("groups", groupService.findAll());

        return "directory/students/student-form";
    }

    @PostMapping("/save")
    public String registerStudent(@ModelAttribute("student") @Valid StudentDTO studentDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("student", studentDTO);
            model.addAttribute("groups", groupService.findAll());
            return "directory/students/student-form";
        } else if (userService.isEmailExist(studentDTO.getEmail())) {
            model.addAttribute("suchEmailAlreadyExist", "This email is already registered");
            model.addAttribute("student", studentDTO);
            model.addAttribute("groups", groupService.findAll());
            return "directory/students/student-form";
        }


        studentService.registerStudent(studentDTO);
        return "redirect:/students/list";
    }

    @GetMapping("/update/form")
    public  String getUpdateStudentForm(@RequestParam Long id, Model model) {
        model.addAttribute("student", studentService.findDTOById(id));
        model.addAttribute("groups", groupService.findAll());

        return "directory/students/student-update";
    }

    @PostMapping("/update")
    public String updateStudent(@ModelAttribute("student") @Valid StudentDTO student,
                                BindingResult bindingResult,
                                Model model) {
        if (hasErrorInCommonField(bindingResult)) {
            model.addAttribute("student", student);
            model.addAttribute("groups", groupService.findAll());
            return "directory/students/student-update";
        }
        studentService.update(student);
        return "redirect:/students/list";
    }

    @PostMapping("/profile/update")
    public String updateStudentProfile(@Valid @ModelAttribute("updateStudent") StudentDTO studentDTO,
                                       BindingResult bindingResult,
                                       @RequestParam(value = "currentPassword", required = false) String currentPassword,
                                       RedirectAttributes redirectAttributes,
                                       Principal principal) {


        if (hasErrorInCommonField(bindingResult)) {
            redirectAttributes.addFlashAttribute("toastValidError", "Validation error occurred");
            return "redirect:/students/profile";
        }

        SecurityUser user = userService.findByEmail(principal.getName());

        if (!currentPassword.isEmpty() && !userService.isPasswordMatch(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("toastError", "Current password is incorrect!");
            return "redirect:/students/profile";
        }

        if (Optional.ofNullable(studentDTO.getPassword()).isPresent() && !studentDTO.getPassword().isBlank()) {
            if (bindingResult.hasFieldErrors("password")) {
                redirectAttributes.addFlashAttribute("toastValidError", "Validation error occurred");
                return "redirect:/students/profile";
            }
        }

        studentService.updateProfile(studentDTO);

        redirectAttributes.addFlashAttribute("toastSuccess", "Profile updated successfully!");
        return "redirect:/students/profile";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, Principal principal) {

        Student student = studentService.findByEmail(principal.getName());
        model.addAttribute("student",
                new StudentDTO(student));
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("updateStudent", new StudentDTO(student));

        return "directory/students/student-profile-page";
    }

    private static boolean hasErrorInCommonField(BindingResult bindingResult) {
        return bindingResult.hasFieldErrors("firstName")
                || bindingResult.hasFieldErrors("lastName")
                || bindingResult.hasFieldErrors("telephoneNumber")
                || bindingResult.hasFieldErrors("age");
    }

    @GetMapping("/delete/{email}")
    public String deleteStudent(@PathVariable String email, Principal principal, RedirectAttributes redirectAttrs) {
        if (principal.getName().equals(email)) {
            studentService.deleteStudent(email);
            return "redirect:/logout";
        }
        redirectAttrs.addFlashAttribute("flashMessage", "Success delete");
        redirectAttrs.addFlashAttribute("flashType", "success");
        studentService.deleteStudent(email);
        return "redirect:/students/list";
    }

    @GetMapping("/groups/list")
    public String getGroupsList(Model model) {
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("newGroup", new Group());

        return "directory/students/groups";
    }

    @GetMapping("/profile/review/{studentId}")
    public String reviewProfile(@PathVariable Long studentId, Model model) {
        model.addAttribute("student", studentService.findDTOById(studentId));
        return "directory/students/student-review";
    }

    @PostMapping("/groups/create")
    public String createNewGroup(@ModelAttribute("newGroup") Group newGroup, Model model) {

        groupService.saveNewGroup(newGroup);

        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("newGroup", new Group());

        return "directory/students/groups";
    }

    @PostMapping("/groups/update")
    public String updateGroup(@RequestParam("id") Long groupId,
                              @RequestParam("course") short groupCourse,
                              @RequestParam("name") String groupName,
                              @RequestParam("speciality") String groupSpeciality,
                              Model model) {

        groupService.updateGroup(groupId, new Group(groupCourse, (short) 1, groupName, groupSpeciality));

        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("newGroup", new Group());

        return "directory/students/groups";
    }

    @GetMapping("/groups/list/filtered")
    public String getGroupsListFiltered(
            @RequestParam("groupNameFilter") String groupName,
            @RequestParam(name = "courseFilter", required = false) Short groupCourse,
            @RequestParam("groupSpecialityFilter") String groupSpeciality,
            Model model) {
        model.addAttribute("groups", groupService.findAllWithFilter(groupName, groupCourse, groupSpeciality));
        model.addAttribute("newGroup", new Group());

        return "directory/students/groups";
    }

    @GetMapping("/groups/delete/{groupId}")
    public String deleteGroup(@PathVariable Long groupId) {
        groupService.deleteById(groupId);

        return "redirect:/students/groups/list";
    }
}
