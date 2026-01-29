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
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private URL baseUrl;
    private Long savedStudentId;
    private Long savedFacultyId;
    private List<Long> savedStudentsIds = new ArrayList<>();

    @BeforeEach
    public void setUp() throws MalformedURLException {
        this.baseUrl = new URL("http://localhost:" + port + "/students");
    }

    @AfterEach
    public void cleanUp() {
        if (savedStudentId != null) {
            studentRepository.deleteById(savedStudentId);
        }
        if (savedFacultyId != null) {
            facultyRepository.deleteById(savedFacultyId);
        }
        for (Long id : savedStudentsIds) {
            studentRepository.deleteById(id);
        }
        savedStudentsIds.clear();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student firstStudent = studentRepository.save(new Student("Ann", 15));
        this.savedStudentsIds.add(firstStudent.getId());
        Student secondStudent = studentRepository.save(new Student("Bob", 12));
        this.savedStudentsIds.add(secondStudent.getId());

        ParameterizedTypeReference<List<Student>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Student>> response = restTemplate.exchange(baseUrl.toString(), HttpMethod.GET, null, typeRef);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testGetStudentInfo() throws Exception {
        Student student = studentRepository.save(new Student("Jack", 12));
        this.savedStudentId = student.getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl.toString() + "/" + savedStudentId, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student("Jack", 12);
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl.toString(), student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
        this.savedStudentId = response.getBody().getId();
    }

    @Test
    void testEditStudent() throws Exception {
        Student student = studentRepository.save(new Student("Jane", 15));
        student.setName("Ann");
        student.setAge(16);
        this.savedStudentId = student.getId();
        ResponseEntity<Student> response = restTemplate.exchange(baseUrl.toString(),
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Ann");
        assertThat(response.getBody().getAge()).isEqualTo(16);
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student student = studentRepository.save(new Student("Jane", 15));

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl.toString() + "/" + student.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRepository.existsById(student.getId())).isFalse();
    }

    @Test
    void testGetStudentsByAge() throws Exception {
        Student firstStudent = studentRepository.save(new Student("Ann", 15));
        this.savedStudentsIds.add(firstStudent.getId());
        Student secondStudent = studentRepository.save(new Student("Bob", 12));
        this.savedStudentsIds.add(secondStudent.getId());
        String url = baseUrl.toString() + "/byAge?age=12";
        ParameterizedTypeReference<List<Student>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Student>> response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().stream()
                .anyMatch(student -> student.getAge() == 12))
                .isTrue();
    }

    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        Student firstStudent = studentRepository.save(new Student("Ann", 15));
        this.savedStudentsIds.add(firstStudent.getId());
        Student secondStudent = studentRepository.save(new Student("Bob", 12));
        this.savedStudentsIds.add(secondStudent.getId());
        String url = baseUrl.toString() + "/byAgeBetween?min=12&max=15";
        ParameterizedTypeReference<List<Student>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Student>> response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().stream()
                .allMatch(student -> student.getAge() >= 12 && student.getAge() <= 15)).isTrue();
    }

    @Test
    void testFindStudentsByFaculty() {
        Student student = new Student("Jane", 15);
        Faculty faculty = facultyRepository.save(new Faculty("aaa", "bbb"));
        this.savedFacultyId = faculty.getId();
        student.setFaculty(faculty);
        studentRepository.save(student);
        this.savedStudentId = student.getId();
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl.toString() + "/byFaculty/" + faculty.getId(), List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetNonExistingStudentInfo() throws Exception {
        Long nonExistingStudentId = 24367658L;
        ResponseEntity<Void> response = restTemplate.getForEntity(baseUrl.toString() + nonExistingStudentId, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}


