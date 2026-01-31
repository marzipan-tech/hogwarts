package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NoSuchFacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

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
        return facultyRepository.findById(id).orElseThrow(()-> new NoSuchFacultyException(id));
    }

    public Faculty editFaculty(Faculty faculty) {
        Optional<Faculty> existingFaculty = facultyRepository.findById(faculty.getId());
        if (existingFaculty.isEmpty()) {
            throw new NoSuchFacultyException(faculty.getId());
        }
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(Long id) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isEmpty()) {
            throw new NoSuchFacultyException(id);
        }
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColourIgnoreCaseOrNameIgnoreCase(String colour, String name) {
        return facultyRepository.findByColourIgnoreCaseOrNameIgnoreCase(colour, name);
    }

    public Faculty findFacultyOfStudent(String name) {
        return studentRepository.findByNameIgnoreCase(name).getFaculty();
    }
}
