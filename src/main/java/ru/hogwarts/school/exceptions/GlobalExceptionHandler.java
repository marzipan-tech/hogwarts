package ru.hogwarts.school.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchStudentException.class)
    public ResponseEntity<Void> handleNoSuchStudent(NoSuchStudentException exception) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(NoSuchFacultyException.class)
    public ResponseEntity<Void> handleNoSuchFaculty(NoSuchFacultyException exception) {
        return ResponseEntity.notFound().build();
    }
}
