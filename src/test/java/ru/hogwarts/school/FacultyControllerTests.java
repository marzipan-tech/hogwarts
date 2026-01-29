package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private URL baseUrl;
    private Long savedFacultyId;
    private Long savedStudentId;
    private List<Long> savedFacultiesIds = new ArrayList<>();

    @BeforeEach
    public void setUp() throws MalformedURLException {
        this.baseUrl = new URL("http://localhost:" + port + "/faculties");
    }

    @AfterEach
    public void cleanUp() {
        if (savedStudentId != null) {
            studentRepository.deleteById(savedStudentId);
        }

        if (savedFacultyId != null) {
            facultyRepository.deleteById(savedFacultyId);
        }

        for (Long id : savedFacultiesIds) {
            facultyRepository.deleteById(id);
        }
        savedFacultiesIds.clear();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testGetFacultyInfo() throws Exception {
        Faculty faculty = facultyRepository.save(new Faculty("aaa", "bbb"));
        this.savedFacultyId = faculty.getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl.toString() + "/" + savedFacultyId, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getColour()).isEqualTo(faculty.getColour());
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("aaa", "bbb");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl.toString(), faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        assertThat(response.getBody().getColour()).isEqualTo(faculty.getColour());
        this.savedFacultyId = response.getBody().getId();
    }

    @Test
    void testEditStudent() throws Exception {
        Faculty faculty = facultyRepository.save(new Faculty("aaa", "bbb"));
        faculty.setName("ccc");
        faculty.setColour("ddd");
        this.savedFacultyId = faculty.getId();
        ResponseEntity<Faculty> response = restTemplate.exchange(baseUrl.toString(),
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("ccc");
        assertThat(response.getBody().getColour()).isEqualTo("ddd");
    }

    @Test
    void testDeleteFaculty() throws Exception {
        Faculty faculty = facultyRepository.save(new Faculty("aaa", "bbb"));

        ResponseEntity<Void> response = restTemplate.exchange(baseUrl.toString() + "/" + faculty.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyRepository.existsById(faculty.getId())).isFalse();
    }

    @Test
    void testFindByColourIgnoreCaseOrNameIgnoreCase() throws Exception {
        Faculty savedFaculty = facultyRepository.save(new Faculty("aaa", "bbb"));
        this.savedFacultyId = savedFaculty.getId();
        String url = baseUrl.toString() + "/byColourOrName?name=aaa";
        ParameterizedTypeReference<List<Faculty>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().stream()
                .anyMatch(faculty -> Objects.equals(faculty.getName(), "aaa")))
                .isTrue();
    }

    @Test
    void testFindFacultyOfStudent() throws Exception {
        Student student = new Student("Jane", 15);
        Faculty faculty = facultyRepository.save(new Faculty("aaa", "bbb"));
        this.savedFacultyId = faculty.getId();
        student.setFaculty(faculty);
        studentRepository.save(student);
        this.savedStudentId = student.getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl.toString() + "/faculty/" + student.getName(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(faculty);
    }

    @Test
    void testGetNonExistingFacultyInfo() throws Exception {
        Long nonExistingFacultyId = 24367658L;
        ResponseEntity<Void> response = restTemplate.getForEntity(baseUrl.toString() + nonExistingFacultyId, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}