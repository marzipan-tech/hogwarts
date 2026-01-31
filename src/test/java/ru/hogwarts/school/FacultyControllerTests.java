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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.exceptions.NoSuchFacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private StudentRepository studentRepository;

    JSONObject facultyObject = new JSONObject();
    Faculty faculty = new Faculty();
    final String name = "aaa";
    final long id = 1L;
    final String colour = "bbb";

    @BeforeEach
    void setUp() throws Exception {
        facultyObject.put("name", name);
        facultyObject.put("colour", colour);
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColour(colour);
    }

    @Test
    public void testCreateFaculty() throws Exception {
        when(facultyRepository.save(any())).thenReturn(faculty);
        when(facultyService.createFaculty(any())).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculties")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));
    }

    @Test
    public void testEditFaculty() throws Exception {
        when(facultyRepository.save(any())).thenReturn(faculty);
        when(facultyService.editFaculty(any())).thenReturn(faculty);
        facultyObject.put("id", id);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));
    }

    @Test
    public void testGetFacultyInfo() throws Exception {
        when(facultyService.findFaculty(eq(id))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        doNothing().when(facultyService).removeFaculty(eq(id));
        mockMvc.perform(delete("/faculties/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Факультет с идентификатором " + id + " удален")));
    }

    @Test
    public void testDeleteNonExistingFaculty() throws Exception {
        Long nonExistingId = 999L;
        doThrow(new NoSuchFacultyException(nonExistingId)).when(facultyService).removeFaculty(eq(nonExistingId));
        mockMvc.perform(delete("/faculties/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Факультет с id " + nonExistingId + " не найден")));
    }

    @Test
    public void testFindByColourIgnoreCaseOrNameIgnoreCase() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                faculty,
                new Faculty("a", colour)
        );

        when(facultyService.findByColourIgnoreCaseOrNameIgnoreCase(colour, null)).thenReturn(faculties);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/byColourOrName?colour=" + colour)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("a", name)));
    }

    @Test
    public void testFindFacultyOfStudent() throws Exception {
        String studentName = "James";
        Student student = new Student(studentName, 15);
        when(studentRepository.findByNameIgnoreCase(anyString())).thenReturn(student);
        when(facultyService.findFacultyOfStudent(studentName)).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/faculty/{name}", studentName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));
    }
}