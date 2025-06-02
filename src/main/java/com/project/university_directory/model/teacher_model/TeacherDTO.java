package com.project.university_directory.model.teacher_model;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class TeacherDTO {
    Long id;
    @NotNull(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;
    @NotNull(message = "First Name is required")
    @Size(min = 2,
            max = 20,
            message = "First name must be no more than 2 and no less than 20")
    private String firstName;

    @NotNull(message = "Last Name  is required")
    @Size(min = 2,
            max = 20,
            message = "Last name must be no more than 2 and no less than 20")
    private String lastName;

    @NotNull(message = "Is required")
    @Pattern(regexp = "^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{1,4}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$",
            message = "Invalid number format")
    private String telephoneNumber;

    @NotNull(message = "Is required")
    @Min(value = 18, message = "Only from age of 16")
    @Max(value = 110, message = "People don't live that long \uD83D\uDC40")
    private Integer age;

    @NotNull(message = "Is required")
    private List<Subject> subjects = new ArrayList<>();


    private List<String> newSubjects;


    public TeacherDTO() {
    }

    public TeacherDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.email = teacher.getSecurityUser().getEmail();
        this.password = teacher.getSecurityUser().getPassword();
        this.firstName = teacher.getFirstName();
        this.lastName = teacher.getLastName();
        this.telephoneNumber = teacher.getTelephoneNumber();
        this.age = teacher.getAge();
        this.subjects = teacher.getSubjects();
    }

    public TeacherDTO(String email, String password, String firstName, String lastName, String telephoneNumber, Integer age, List<Subject> subjects, List<String> newSubjects) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.age = age;
        this.subjects = subjects;
        this.newSubjects = newSubjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<String> getNewSubjects() {
        return newSubjects;
    }

    public void setNewSubjects(List<String> newSubjects) {
        this.newSubjects = newSubjects;
    }
}
