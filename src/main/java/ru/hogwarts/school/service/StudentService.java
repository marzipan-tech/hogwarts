package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void removeStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public List<Student> findByFaculty(Long facultyId) {
        return studentRepository.findByFaculty(facultyRepository.findById(facultyId).get());
    }

    public Faculty findFacultyOfStudent(String name) {
        Student student = studentRepository.findByNameIgnoreCase(name);
        if (student != null) {
            return student.getFaculty();
        }
        return null;
    }


}
