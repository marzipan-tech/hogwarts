package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.hogwarts.school.exceptions.NoFacultiesException;
import ru.hogwarts.school.exceptions.NoSuchFacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Creating faculty method invoked");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.debug("Finding faculty method invoked");
        return facultyRepository.findById(id).orElseThrow(() -> new NoSuchFacultyException(id));
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Editing faculty method invoked");
        Optional<Faculty> existingFaculty = facultyRepository.findById(faculty.getId());
        if (existingFaculty.isEmpty()) {
            throw new NoSuchFacultyException(faculty.getId());
        }
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(Long id) {
        logger.info("Removing faculty method invoked");
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isEmpty()) {
            throw new NoSuchFacultyException(id);
        }
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColourIgnoreCaseOrNameIgnoreCase(String colour, String name) {
        logger.info("Finding faculty by colour method invoked");
        return facultyRepository.findByColourIgnoreCaseOrNameIgnoreCase(colour, name);
    }

    public Faculty findFacultyOfStudent(String name) {
        logger.info("Find faculty of student method invoked");
        return studentRepository.findByNameIgnoreCase(name).getFaculty();
    }

    public String getLongestFacultyName() {
        Optional<String> result = facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length));
        if (result.isEmpty()) {
            throw new NoFacultiesException();
        }
        return result.get();
    }
}
