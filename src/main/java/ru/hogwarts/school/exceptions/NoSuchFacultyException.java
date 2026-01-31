package ru.hogwarts.school.exceptions;

public class NoSuchFacultyException extends RuntimeException {
    public NoSuchFacultyException(long id) {
        super("Факультет с id " + id + " не найден");
    }
}
