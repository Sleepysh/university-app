package com.project.university_directory.service;

import com.project.university_directory.model.course_model.FileData;
import com.project.university_directory.repository.FileDataRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    public final FileDataRepository fileDataRepository;

    public FileService(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }


    public FileData findById(Long id) {
        return fileDataRepository.findById(id).orElseThrow(() -> new RuntimeException("file by id " + id + " is not exist"));
    }
}
