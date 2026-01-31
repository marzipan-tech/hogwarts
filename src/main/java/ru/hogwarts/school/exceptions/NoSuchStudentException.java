package ru.hogwarts.school.exceptions;

public class NoSuchStudentException extends RuntimeException {
    public NoSuchStudentException(long id) {
        super("Студент с id " + id + " не найден");
    }
}
