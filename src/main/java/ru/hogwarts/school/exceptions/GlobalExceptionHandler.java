package ru.hogwarts.school.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchStudentException.class)
    public ResponseEntity<String> handleNoSuchStudent(NoSuchStudentException exception) {
        logger.error("No such student");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(NoSuchFacultyException.class)
    public ResponseEntity<String> handleNoSuchFaculty(NoSuchFacultyException exception) {
        logger.error("No such faculty");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(NoStudentsException.class)
    public ResponseEntity<String> handleNoStudents(NoStudentsException exception) {
        logger.error("No students");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(NoFacultiesException.class)
    public ResponseEntity<String> handleNoFaculties(NoFacultiesException exception) {
        logger.error("No faculties");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
