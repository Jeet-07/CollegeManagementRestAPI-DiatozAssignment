package com.diatoz.assignment.services;

import com.diatoz.assignment.models.dto.DtoUtils;
import com.diatoz.assignment.models.dto.TeacherDTO;
import com.diatoz.assignment.models.entities.Subject;
import com.diatoz.assignment.models.entities.Teacher;
import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.roles.EnumRoles;
import com.diatoz.assignment.payload.request.create.TeacherCreateBody;
import com.diatoz.assignment.payload.request.updation.TeacherUpdateBody;
import com.diatoz.assignment.repositories.RoleRepository;
import com.diatoz.assignment.repositories.SubjectRepository;
import com.diatoz.assignment.repositories.TeacherRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired private RoleRepository roleRepo;

    @Autowired private SubjectRepository subjectRepo;

    @Autowired private PasswordEncoder passwordEncoder;

    public List<TeacherDTO> getAllTeachers(){
        return teacherRepo.findAll().stream().map(teacher-> DtoUtils.teacherToTeacherDto(teacher)).collect(Collectors.toList());
    }
    public boolean existsById(String id){
        return teacherRepo.existsById(id);
    }
    public boolean existsByUsername(String username){
        return teacherRepo.existsByUsername(username);
    }
    public TeacherDTO getTeacherById(String id) throws ResourceNotFoundException{
        Teacher teacher = teacherRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No user found with Id: "+id));
        return DtoUtils.teacherToTeacherDto(teacher);
    }

    public TeacherDTO save(TeacherCreateBody teacherBody){
        Teacher newTeacher= new Teacher();
        newTeacher.setUsername(teacherBody.getUsername().trim());
        newTeacher.setPassword(passwordEncoder.encode(teacherBody.getPassword()));
        newTeacher.setRole(roleRepo.findByName(EnumRoles.ROLE_TEACHER));
        newTeacher.setEmail(teacherBody.getEmail().trim());
        newTeacher.setEnabled(teacherBody.isEnabled());
        newTeacher.setFirstName(teacherBody.getFirstName().trim().toUpperCase());
        newTeacher.setLastName(teacherBody.getLastName().trim().toUpperCase());

        Set<Subject> subjects = teacherBody.getSubjects().stream()
                .filter(subName->subjectRepo.existsByName(subName.trim().toUpperCase()))
                .map(subName -> subjectRepo.findByName(subName.trim().toUpperCase()))
                .collect(Collectors.toSet());

        newTeacher.setSubjects(subjects);
        Teacher savedTeacher = teacherRepo.save(newTeacher);
        return DtoUtils.teacherToTeacherDto(savedTeacher);
    }

    public TeacherDTO update(String id,TeacherUpdateBody teacherBody)throws ResourceNotFoundException{
        Teacher teacher = teacherRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No user found with Id: "+id));
        if(!teacherBody.getUsername().isEmpty()){
            if(!teacherBody.getUsername().equals(teacher.getUsername()))
                teacher.setUsername(teacherBody.getUsername().trim());
        }
        if(!teacherBody.getFirstName().isEmpty())
            teacher.setFirstName(teacherBody.getFirstName().trim().toUpperCase());
        if(!teacherBody.getLastName().isEmpty())
            teacher.setLastName(teacherBody.getLastName().trim().toUpperCase());
        if(!teacherBody.getPassword().isEmpty()){
            teacher.setPassword(passwordEncoder.encode(teacherBody.getPassword().trim()));
        }
        if(!teacherBody.getEmail().isEmpty()){
            teacher.setEmail(teacherBody.getEmail().trim());
        }
        if(teacherBody.getSubjects().size()>0){
            teacherBody.getSubjects().stream()
                    .filter(subName->subjectRepo.existsByName(subName.trim().toUpperCase()))
                    .forEach(subName -> teacher.getSubjects().add(subjectRepo.findByName(subName.trim().toUpperCase())));
        }
        Teacher updatedTeacher =teacherRepo.save(teacher);
        return DtoUtils.teacherToTeacherDto(updatedTeacher);
    }

    public TeacherDTO delete(String id) throws ResourceNotFoundException{
        Teacher teacher = teacherRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No user found with Id: "+id));
        teacherRepo.deleteById(id);
        return DtoUtils.teacherToTeacherDto(teacher);
    }

    public TeacherDTO updateEnableStatus(String id,boolean enabled) throws ResourceNotFoundException{
        Teacher teacher = teacherRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No user found with Id: "+id));
        teacher.setEnabled(enabled);
        teacher=teacherRepo.save(teacher);
        return DtoUtils.teacherToTeacherDto(teacher);
    }


}
