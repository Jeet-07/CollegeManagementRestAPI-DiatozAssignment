package com.diatoz.assignment.models.dto;

import com.diatoz.assignment.models.entities.Admin;
import com.diatoz.assignment.models.entities.Student;
import com.diatoz.assignment.models.entities.Teacher;

public final class DtoUtils {
    public static final AdminDTO adminToAdminDto(Admin admin){
        return new AdminDTO(admin.getId(),
                admin.getUsername(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.isEnabled(),
                admin.getRole().getName().name());
    }
    public static final StudentDTO studentToStudentDto(Student student)
    {
        StudentDTO studentDto = new StudentDTO(
                student.getId(),
                student.getUsername(),
                student.getFirstName(),
                student.getLastName(),
                student.isEnabled(),
                student.getRole().getName().name(),
                student.getEmail(),
                student.getGender(),
                student.getSubjects()
        );
        return studentDto;
    }

    public static final TeacherDTO teacherToTeacherDto(Teacher teacher){
        TeacherDTO teacherDto= new TeacherDTO(
                teacher.getId(),
                teacher.getUsername(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.isEnabled(),
                teacher.getRole().getName().name(),
                teacher.getSubjects()
        );
        return teacherDto;
    }


}
