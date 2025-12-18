package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(Long id) {
        facultyRepository.findById(id);
    }

    public List<Faculty> findByColourIgnoreCaseOrNameIgnoreCase(String colour, String name) {
        return facultyRepository.findByColourIgnoreCaseOrNameIgnoreCase(colour, name);
    }

    public Faculty findFacultyOfStudent(String name) {
        return studentRepository.findByNameIgnoreCase(name).getFaculty();
    }
}
