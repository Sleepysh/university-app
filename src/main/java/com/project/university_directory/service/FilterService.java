package com.project.university_directory.service;

import com.project.university_directory.model.student_model.Student;
import com.project.university_directory.model.student_model.StudentDTO;
import com.project.university_directory.model.teacher_model.Subject;
import com.project.university_directory.model.teacher_model.Teacher;
import com.project.university_directory.model.teacher_model.TeacherDTO;
import com.project.university_directory.repository.StudentRepository;
import com.project.university_directory.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilterService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    public FilterService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public List<Student> filterStudents(StudentDTO filter) {
        return studentRepository.filterStudents(
                filter.getEmail(),
                filter.getFirstName(),
                filter.getLastName(),
                filter.getTelephoneNumber(),
                filter.getAge(),
                filter.getGroupName(),
                filter.getRoomNumber(),
                filter.getSpeciality()
                );
    }

    @Transactional
    public List<Teacher> filterTeacher(TeacherDTO filter) {
        List<String> subjectNames = null;
        if (filter.getSubjects() != null) {
            subjectNames = filter.getSubjects().stream()
                    .map(Subject::getSubject)
                    .toList();
        }

        return teacherRepository.filterTeachers(
                filter.getFirstName(),
                filter.getLastName(),
                filter.getTelephoneNumber(),
                filter.getEmail(),
                filter.getAge(),
                subjectNames
        );
    }
}
