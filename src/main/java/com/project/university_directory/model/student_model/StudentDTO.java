package com.project.university_directory.model.student_model;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class StudentDTO{

    Long id;

    @NotNull(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @NotNull(message = "Password is required")
    @Size(min = 2,
            max = 20,
            message = "First name must be no more than 2 and no less than 20")
    private String firstName;

    @NotNull(message = "Password is required")
    @Size(min = 2,
            max = 20,
            message = "Last name must be no more than 2 and no less than 20")
    private String lastName;

    @NotNull(message = "Is required")
    @Pattern(regexp = "^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{1,4}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$",
            message = "Invalid number format")
    private String telephoneNumber;

    @NotNull(message = "Is required")
    @Min(value = 16, message = "Only from age of 16")
    @Max(value = 110, message = "People don't live that long \uD83D\uDC40")
    private Integer age;

//    @NotNull(message = "Is required")
    private short groupNumber;

//    @NotNull(message = "Is required")
    private short groupCourse;

//    @NotNull(message = "Is required")
    private String groupName;

    @NotNull(message = "Is required")
    private Integer roomNumber;

   // @NotNull(message = "Is required")
    private String speciality;
    private Long groupId;

    public StudentDTO(Long id, String email, String password, String firstName, String lastName, String telephoneNumber, Integer age, short groupNumber, short groupCourse, String groupName, Integer roomNumber, String speciality, Long groupId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.age = age;
        this.groupNumber = groupNumber;
        this.groupCourse = groupCourse;
        this.groupName = groupName;
        this.roomNumber = roomNumber;
        this.speciality = speciality;
        this.groupId = groupId;
    }

    public StudentDTO() {
    }

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.email = student.getSecurityUser().getEmail();
        this.password = student.getSecurityUser().getPassword();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.telephoneNumber = student.getTelephoneNumber();
        this.age = student.getAge();
        this.groupCourse = student.getGroup().getCourse();
        this.groupNumber = student.getGroup().getGroupNumber();
        this.groupName = student.getGroup().getName();
        this.roomNumber = student.getRoomNumber();
        this.speciality = student.getGroup().getSpeciality();
        this.groupId = student.getGroup().getId();

    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public short getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(short groupNumber) {
        this.groupNumber = groupNumber;
    }

    public short getGroupCourse() {
        return groupCourse;
    }

    public void setGroupCourse(short groupCourse) {
        this.groupCourse = groupCourse;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
