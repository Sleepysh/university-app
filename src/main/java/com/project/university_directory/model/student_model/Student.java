package com.project.university_directory.model.student_model;

import com.project.university_directory.model.SecurityUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "security_user_email", referencedColumnName = "email", nullable = false)
    private SecurityUser securityUser;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "age")
    private Integer age;

    @Column(name = "room_number")
    private Integer roomNumber;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(SecurityUser securityUser, String firstName, String lastName, String telephoneNumber, Integer age, int roomNumber) {
        this.securityUser = securityUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.age = age;
        this.roomNumber = roomNumber;
        this.group = group;
    }
}
