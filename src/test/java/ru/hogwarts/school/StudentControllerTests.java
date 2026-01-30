package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exceptions.NoSuchStudentException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoBean
    private StudentService studentService;

    JSONObject studentObject = new JSONObject();
    Student student = new Student();
    final String name = "aaa";
    final long id = 1L;
    final int age = 12;
    List<Student> students = Arrays.asList(
            new Student("Jane", age),
            new Student("Phil", age)
    );

    @BeforeEach
    void setUp() throws Exception {
        studentObject.put("name", name);
        studentObject.put("age", age);
        student.setId(id);
        student.setName(name);
        student.setAge(age);
    }

    @Test
    public void testCreateStudent() throws Exception {
        when(studentRepository.save(any())).thenReturn(student);
        when(studentService.createStudent(any())).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testEditStudent() throws Exception {
        when(studentRepository.save(any())).thenReturn(student);
        when(studentService.editStudent(any())).thenReturn(student);
        studentObject.put("id", id);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testGetStudentInfo() throws Exception {
        when(studentService.findStudent(eq(id))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        doNothing().when(studentService).removeStudent(eq(id));
        mockMvc.perform(delete("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Студент с идентификатором " + id + " удален")));
    }

    @Test
    public void testDeleteNonExistingStudent() throws Exception {
        Long nonExistingId = 999L;
        doThrow(new NoSuchStudentException(nonExistingId)).when(studentService).removeStudent(eq(nonExistingId));
        mockMvc.perform(delete("/students/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Студент с id " + nonExistingId + " не найден")));
    }

    @Test
    public void testGetStudentsByAge() throws Exception {
        when(studentService.findByAge(age)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/byAge?age=" + age)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].age", hasItem(age)));
    }

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {
        int minAge = 8;
        int maxAge = 20;
        List<Student> students = Arrays.asList(
                new Student("Jane", 13),
                new Student("Phil", 19)
        );
        when(studentService.findByAgeBetween(minAge, maxAge)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/byAgeBetween?min={min}&max={max}", minAge, maxAge)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].age", everyItem(greaterThanOrEqualTo(minAge))))
                .andExpect(jsonPath("$.[*].age", everyItem(lessThanOrEqualTo(maxAge))));
    }

    @Test
    public void testGetAllStudents() throws Exception {
        when(studentService.findAllStudents()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("Jane", "Phil")));
    }

    @Test
    public void testFindStudentsByFaculty() throws Exception {
        Long facultyId = 1L;
        when(studentService.findByFaculty(facultyId)).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/byFaculty/{facultyId}", facultyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].facultyId", everyItem(is(facultyId.intValue()))));
    }
}


