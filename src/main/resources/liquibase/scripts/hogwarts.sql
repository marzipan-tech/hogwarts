-- liquibase formatted sql
-- changeset mpanova:1
CREATE INDEX student_name_index ON student (name);
-- changeset mpanova:2
CREATE INDEX faculty_name_colour_index ON faculty (name, colour);