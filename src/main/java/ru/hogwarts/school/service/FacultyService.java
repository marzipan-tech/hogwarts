package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private Long lastId = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(Long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public void removeFaculty(Long id) {
        faculties.remove(id);
    }

    public List<Faculty> findFacultyByColour(String colour) {
        return faculties.values()
                .stream()
                .filter(faculty -> faculty.getColour().equals(colour))
                .collect(Collectors.toList());
    }
}
