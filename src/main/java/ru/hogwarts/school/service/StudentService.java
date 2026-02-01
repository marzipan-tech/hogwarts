package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NoSuchStudentException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(()-> new NoSuchStudentException(id));
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student editStudent(Student student) {
        Optional<Student> existingStudent = studentRepository.findById(student.getId());
        if (existingStudent.isEmpty()) {
            throw new NoSuchStudentException(student.getId());
        }
        return studentRepository.save(student);
    }

    @Transactional
    public void removeStudent(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            throw new NoSuchStudentException(id);
        }
        avatarRepository.deleteByStudentId(id);
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public List<Student> findByFaculty(Long facultyId) {
        return studentRepository.findAllByFaculty_Id(facultyId);
    }

    public Integer getAllStudentsCount() {
        return studentRepository.getAllStudentsCount();
    }

    public Integer getAverageStudentsAge() {
        return studentRepository.getAverageStudentsAge();
    }

    public List<Student> getFiveLastStudents() {
        return studentRepository.getFiveLastStudents();
    }
}
