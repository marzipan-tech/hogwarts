SELECT student.name AS student_name, student.age, faculty.name AS faculty_name
FROM student
JOIN faculty ON student.faculty_id = faculty.id;
SELECT student.name, avatar.file_path
FROM student
INNER JOIN avatar ON student.id = avatar.student_id;