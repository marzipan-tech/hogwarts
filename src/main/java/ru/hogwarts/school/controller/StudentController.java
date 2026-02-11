package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
        return "Студент с идентификатором " + id + " удален";
    }

    @GetMapping("/byAge")
    public List<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("/byAgeBetween")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAllStudents();
    }

    @GetMapping("/byFaculty/{id}")
    public List<Student> findStudentsByFaculty(@PathVariable("id") Long facultyId) {
        return studentService.findByFaculty(facultyId);
    }

    @GetMapping("/all-students-count")
    public Integer getAllStudentsCount() {
        return studentService.getAllStudentsCount();
    }

    @GetMapping("/avg-students-age")
    public Integer getAverageStudentsAge() {
        return studentService.getAverageStudentsAge();
    }

    @GetMapping("/five-last-students")
    public List<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/average-age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/students/print-parallel")
    public void printNamesInParallel() {
        studentService.printNamesInParallel();
    }

    @GetMapping("/students/print-synchronized")
    public void printNamesSynchronized() {
        studentService.printNamesSynchronized();
    }
}
