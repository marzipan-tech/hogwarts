package ru.hogwarts.school.exceptions;

public class NoStudentsException extends RuntimeException {
    public NoStudentsException() {
        super("Нет студентов");
    }
}
