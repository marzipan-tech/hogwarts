package ru.hogwarts.school.model;

import java.util.Objects;

public class Faculty {
    private Long id;
    private String name;
    private String colour;

    public Faculty(String name, String colour) {
        this.name = name;
        this.colour = colour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(colour, faculty.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, colour);
    }

}
