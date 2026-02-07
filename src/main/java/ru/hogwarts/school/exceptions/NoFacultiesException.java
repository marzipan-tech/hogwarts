package ru.hogwarts.school.exceptions;

public class NoFacultiesException extends RuntimeException {
    public NoFacultiesException() {
        super("Нет факультетов");
    }
}
