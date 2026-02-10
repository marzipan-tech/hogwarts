package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("faculties")
public class FacultyController {
    private final FacultyService facultyService;

    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
        return "Факультет с идентификатором " + id + " удален";
    }

    @GetMapping("/byColourOrName")
    public List<Faculty> findByColourIgnoreCaseOrNameIgnoreCase(@RequestParam(required = false) String colour, @RequestParam(required = false) String name) {
        return facultyService.findByColourIgnoreCaseOrNameIgnoreCase(colour, name);
    }

    @GetMapping("/faculty/{name}")
    public Faculty findFacultyOfStudent(@PathVariable("name") String name) {
        return facultyService.findFacultyOfStudent(name);
    }

    @GetMapping("/longest-name")
    public String getLongestFacultyName() {
        return facultyService.getLongestFacultyName();
    }

    @GetMapping("/get-sum")
    public long getSum() {
        long startTime = System.currentTimeMillis();
        long sum = calculateSum(1000000);
        logger.info("Время расчета {} мс", System.currentTimeMillis() - startTime);
        return sum;
    }

    private static long calculateSum(long n) {
        return n * (n + 1L) / 2;
    }

}
