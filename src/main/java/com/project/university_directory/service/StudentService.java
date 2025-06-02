package com.project.university_directory.service;

import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.model.student_model.Group;
import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.student_model.StudentDTO;
import com.project.university_directory.repository.GroupRepository;
import com.project.university_directory.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final SecurityUserService securityUserService;
    private final GroupRepository groupRepository;


    @Autowired
    public StudentService(StudentRepository studentRepository, SecurityUserService securityUserService, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.securityUserService = securityUserService;
        this.groupRepository = groupRepository;
    }

    public void registerStudent(StudentDTO studentDTO) {
        SecurityUser user = securityUserService.saveUser(
                new SecurityUser(
                        studentDTO.getEmail(),
                        studentDTO.getPassword()),
                "STUDENT");

        studentRepository.save(
                processStudent(studentDTO, user));
    }

    private Student processStudent(StudentDTO studentDTO, SecurityUser user) {
        Group group = groupRepository.findById(studentDTO.getGroupId())
                .orElseThrow(() ->  new RuntimeException("Group is not exist by id: " + studentDTO.getGroupId()));

        Student student = new Student(
                user,
                studentDTO.getFirstName(),
                studentDTO.getLastName(),
                studentDTO.getTelephoneNumber(),
                studentDTO.getAge(),
                studentDTO.getRoomNumber());

        student.setGroup(group);

        return student;
    }

    public Student findByEmail(String email) {
        return studentRepository.findBySecurityUser_Email(email)
                .orElseThrow(() ->  new RuntimeException("student is not exist by email: " + email));

    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public void deleteStudent(String email) {
        Student student = studentRepository.findBySecurityUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student by email " + email + " is not exist"));


        studentRepository.removeStudentFromCourses(student.getId());
        student.getGroup().getStudents().remove(student);
        studentRepository.delete(student);

        securityUserService.deleteUser(email);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no student with this id: " + id));
    }

    public StudentDTO findDTOById(Long id) {
        return new StudentDTO(studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no student with this id: " + id)));
    }

    public void update(StudentDTO student) {
        Student newStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new NoSuchElementException("There is no student with this id: " + student.getId()));

        newStudent.setAge(student.getAge());
        newStudent.setFirstName(student.getFirstName());
        newStudent.setLastName(student.getLastName());
        newStudent.setTelephoneNumber(student.getTelephoneNumber());
        newStudent.setRoomNumber(student.getRoomNumber());
        newStudent.setGroup(groupRepository.findById(student.getGroupId())
                .orElseThrow(() -> new RuntimeException("No group exists for this id: " + student.getGroupId())));

        studentRepository.save(newStudent);
    }

    public List<Student> searchByName(String query) {
        return studentRepository.findAll().stream().filter(student-> student.getFirstName().contains(query)).collect(Collectors.toList());
    }

    public List<Student> findByEmails(List<String> emails) {
        return studentRepository.findByEmails(emails);
    }

    public List<Student> getStudentsFromGroup(String group) {
        return groupRepository.findByGroupCode(group).orElseThrow(() -> new RuntimeException("Group wasn`t find by name " + group)).getStudents();
    }

    public void updateProfile(StudentDTO studentDTO) {
        Student student = studentRepository.findById(studentDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("There is no student with this id: " + studentDTO.getId()));

        student.setAge(studentDTO.getAge());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setTelephoneNumber(studentDTO.getTelephoneNumber());

        if (Optional.ofNullable(studentDTO.getPassword()).isPresent() && !studentDTO.getPassword().isBlank()) {

            SecurityUser currentUser = student.getSecurityUser();

            currentUser.setPassword(securityUserService.encodePassword(studentDTO.getPassword()));

            securityUserService.save(currentUser);
        }

        studentRepository.save(student);
    }
}
