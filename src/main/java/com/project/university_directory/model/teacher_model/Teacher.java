package com.project.university_directory.model.teacher_model;

import com.project.university_directory.model.Role;
import com.project.university_directory.model.SecurityUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "security_user_email", referencedColumnName = "email", nullable = false)
    private SecurityUser securityUser;

    @Column(name = "first_name")
    private  String firstName;

    @Column(name = "last_name")
    private  String lastName;

    @Column(name = "telephone_number")
    private  String telephoneNumber;

    @Column(name = "age")
    private  Integer age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "teachers_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "academ_subjects_id")
    )
    private List<Subject> subjects = new ArrayList<>();



    public Teacher(SecurityUser securityUser, String firstName, String lastName, String telephoneNumber, Integer age, List<Subject> subjects) {
        this.securityUser = securityUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.age = age;
        this.subjects = subjects;
    }

}
