package com.project.university_directory.service;

import com.project.university_directory.model.teacher_model.Subject;
import com.project.university_directory.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectRepository subjectsRepository;

    public SubjectService(SubjectRepository subjectsRepository) {
        this.subjectsRepository = subjectsRepository;
    }

    public List<Subject> findAll() {
        return subjectsRepository.findAll();
    }

    @Transactional
    public List<Subject> saveAll(List<String> newSubjects) {
        List<Subject> subjects = newSubjects.stream().map(Subject::new).toList();
        subjectsRepository.saveAll(subjects);
        return subjects;
    }

    public List<Subject> searchByTitle(String query) {
        return subjectsRepository.findAll().stream().filter(subject -> subject.getSubject().contains(query)).collect(Collectors.toList());
    }

    public void saveSubjects(String subjectName) {
        subjectsRepository.save(new Subject(subjectName));
    }

    public List<Subject> findAllFiltered(String search) {
        return subjectsRepository.findAllFiltered(search);
    }

    @Transactional
    public void updateSubject(Long subjectId, String subjectName) {
        Subject subject = subjectsRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("A subject with this id" + subjectId + " does not exist"));

        subject.setSubject(subjectName);

        subjectsRepository.save(subject);
    }

    public void deleteSubject(Long subjectId) {
        subjectsRepository.deleteById(subjectId);
    }

    public List<Subject> findAllById(List<Long> subjectIds) {
        return subjectsRepository.findAllById(subjectIds);
    }
}
