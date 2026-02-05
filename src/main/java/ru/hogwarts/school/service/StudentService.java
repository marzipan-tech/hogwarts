package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Creating student method invoked");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.debug("Finding student method invoked");
        try {
            return studentRepository.findById(id).orElseThrow(()-> new NoSuchStudentException(id));
        } catch (NoSuchStudentException e) {
            logger.error("No student with id {}", id, e);
            throw e;
        }
    }

    public List<Student> findAllStudents() {
        logger.info("Getting all students method invoked");
        return studentRepository.findAll();
    }

    public Student editStudent(Student student) {
        logger.info("Editing student method invoked");
        Optional<Student> existingStudent = studentRepository.findById(student.getId());
        if (existingStudent.isEmpty()) {
            try {
                throw new NoSuchStudentException(student.getId());
            } catch (NoSuchStudentException e) {
                logger.error("No such student", e);
                throw e;
            }
        }
        return studentRepository.save(student);
    }

    @Transactional
    public void removeStudent(Long id) {
        logger.info("Removing student method invoked");
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            try {
                throw new NoSuchStudentException(id);
            } catch (NoSuchStudentException e) {
                logger.error("No student with id {}", id, e);
                throw e;
            }
        }
        avatarRepository.deleteByStudentId(id);
        studentRepository.deleteById(id);
    }

    public List<Student> findByAge(int age) {
        logger.info("Finding student by name method invoked");
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Finding student by age between min and max method invoked");
        return studentRepository.findByAgeBetween(min, max);
    }

    public List<Student> findByFaculty(Long facultyId) {
        logger.info("Finding students by faculty method invoked");
        return studentRepository.findAllByFaculty_Id(facultyId);
    }

    public Integer getAllStudentsCount() {
        logger.info("Getting all students count method invoked");
        return studentRepository.getAllStudentsCount();
    }

    public Integer getAverageStudentsAge() {
        logger.info("Getting average students age method invoked");
        return studentRepository.getAverageStudentsAge();
    }

    public List<Student> getFiveLastStudents() {
        logger.info("Getting last five students method invoked");
        return studentRepository.getFiveLastStudents();
    }
}
