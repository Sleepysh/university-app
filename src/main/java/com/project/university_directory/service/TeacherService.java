package com.project.university_directory.service;

import com.project.university_directory.model.SecurityUser;
import com.project.university_directory.model.teacher_model.Subject;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import com.project.university_directory.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    public final TeacherRepository teacherRepository;
    private final SecurityUserService securityUserService;
    private final SubjectService subjectService;


    @Autowired
    public TeacherService(TeacherRepository theTeacherRepository, SecurityUserService securityUserService, SubjectService subjectService) {
        this.teacherRepository = theTeacherRepository;
        this.securityUserService = securityUserService;
        this.subjectService = subjectService;
    }

    public Teacher findByEmail(String email) {
        return teacherRepository.findBySecurityUser_Email(email).orElseThrow(() -> new RuntimeException("Email wasn't find " + email));
    }


    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher findById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no teacher with this id: " + id));
    }

    public TeacherDTO findDTOById(Long id) {
        return new TeacherDTO(teacherRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no teacher with this id: " + id)));
    }

    @Transactional
    public void update(TeacherDTO teacher) {

        Teacher newTeacher = teacherRepository.findById(teacher.getId()).orElseThrow(() -> new NoSuchElementException("There is no teacher with this id: " + teacher.getId()));
        newTeacher.setAge(teacher.getAge());
        newTeacher.setFirstName(teacher.getFirstName());
        newTeacher.setLastName(teacher.getLastName());
        newTeacher.setTelephoneNumber(teacher.getTelephoneNumber());

        teacherRepository.save(newTeacher);

    }

    public void deleteTeacher(String email) {
        teacherRepository.deleteById(teacherRepository.findBySecurityUser_Email(email).get().getId());
        securityUserService.deleteUser(email);
    }

    @Transactional
    public void registerTeacher(TeacherDTO teacherDTO) {
        SecurityUser user = securityUserService.saveUser(
                new SecurityUser(
                        teacherDTO.getEmail(),
                        teacherDTO.getPassword()),
                "TEACHER");

        user.getRoles().add(securityUserService.getRole("STUDENT"));
        securityUserService.save(user);

        List<Subject> subjects = new ArrayList<>(teacherDTO.getSubjects());

        if (Optional.ofNullable(teacherDTO.getNewSubjects()).isPresent()) {
            List<Subject> newSubjects = subjectService.saveAll(teacherDTO.getNewSubjects());
            subjects.addAll(newSubjects);
        }


        teacherRepository.save(
                new Teacher(
                        user,
                        teacherDTO.getFirstName(),
                        teacherDTO.getLastName(),
                        teacherDTO.getTelephoneNumber(),
                        teacherDTO.getAge(),
                        subjects
                ));
    }

    public List<Teacher> searchByName(String query) {
        return teacherRepository.findAll().stream().filter(teacher -> teacher.getFirstName().contains(query)).collect(Collectors.toList());
    }

    public List<Teacher> findByEmails(List<String> emails) {
        return teacherRepository.findByEmails(emails);
    }


    @Transactional
    public void updateProfile(TeacherDTO teacherDTO, List<Long> subjectIds) {
        Teacher teacher = teacherRepository.findById(teacherDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("There is no teacher with this id: " + teacherDTO.getId()));

        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setAge(teacherDTO.getAge());

        if (Optional.ofNullable(subjectIds).isPresent()) {
            List<Subject> subjects = subjectService.findAllById(subjectIds);
            teacher.setSubjects(subjects);

        } else {
            teacher.getSubjects().clear();
        }

        if (Optional.ofNullable(teacherDTO.getPassword()).isPresent() && !teacherDTO.getPassword().isBlank()) {

           SecurityUser currentUser = teacher.getSecurityUser();

           currentUser.setPassword(securityUserService.encodePassword(teacherDTO.getPassword()));

            securityUserService.save(currentUser);
        }

        teacherRepository.save(teacher);
    }
}
