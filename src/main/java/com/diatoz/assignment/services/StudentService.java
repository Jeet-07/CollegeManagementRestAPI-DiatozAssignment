package com.diatoz.assignment.services;

import com.diatoz.assignment.models.dto.DtoUtils;
import com.diatoz.assignment.models.dto.StudentDTO;
import com.diatoz.assignment.models.entities.Student;
import com.diatoz.assignment.models.entities.Subject;
import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.roles.EnumRoles;
import com.diatoz.assignment.payload.request.create.StudentCreateBody;
import com.diatoz.assignment.payload.request.create.SubjectDetailsBody;
import com.diatoz.assignment.payload.request.updation.StudentUpdateBody;
import com.diatoz.assignment.repositories.RoleRepository;
import com.diatoz.assignment.repositories.StudentRepository;
import com.diatoz.assignment.repositories.SubjectRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired private StudentRepository studentRepo;
    @Autowired private RoleRepository roleRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SubjectRepository subjectRepo;
    
    public StudentDTO getStudentById(String id)throws ResourceNotFoundException{
        Student student= studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));
        return DtoUtils.studentToStudentDto(student);
    }
    
    public List<StudentDTO> getAllStudents(){
        return studentRepo.findAll().stream().map(student-> DtoUtils.studentToStudentDto(student)).collect(Collectors.toList());
    }
    public StudentDTO create(StudentCreateBody studentBody){
        Student student = getStudentFromStudentCreateBody(studentBody);
        Student savedStudent = studentRepo.save(student);
        return DtoUtils.studentToStudentDto(savedStudent);
    }
    public StudentDTO update(String id,StudentUpdateBody studentBody) throws ResourceNotFoundException{
        Student student= studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));
        if(!studentBody.getUsername().trim().isEmpty()){
            if(!studentBody.getUsername().equals(student.getUsername().trim()))
                student.setUsername(studentBody.getUsername().trim());
        }
        if(!studentBody.getFirstName().trim().isEmpty())
            student.setFirstName(studentBody.getFirstName().trim().toUpperCase());
        if(!studentBody.getLastName().trim().isEmpty())
            student.setLastName(studentBody.getLastName().trim().toUpperCase());
        if(!studentBody.getPassword().trim().isEmpty()){
            student.setPassword(passwordEncoder.encode(studentBody.getPassword()));
        }
        if(!studentBody.getEmail().trim().isEmpty()){
            student.setEmail(studentBody.getEmail());
        }
        if(!studentBody.getGender().trim().isEmpty()){
            if('M' == studentBody.getGender().toUpperCase().charAt(0)){
                student.setGender("MALE");
            }else if('F' == studentBody.getGender().toUpperCase().charAt(0)){
                student.setGender("FEMALE");
            }else{
                student.setGender("OTHER");
            }
        }
        studentBody.getSubjectDetails().stream()
                .filter(subDetail -> subjectRepo.existsByName(subDetail.getName().trim().toUpperCase()))
                .forEach(subDetail->
                        student.getSubjects()
                                .add(new Student.SubjectDetails(
                                        subjectRepo.findByName(subDetail.getName().trim().toUpperCase()),
                                        subDetail.getMarks())
                                )
                );
        
        Student updatedStudent = studentRepo.save(student);
        return DtoUtils.studentToStudentDto(updatedStudent);
    }
    public StudentDTO deleteById(String id) throws ResourceNotFoundException{
        Student student= studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));
        studentRepo.deleteById(id);
        return DtoUtils.studentToStudentDto(student);
    }
    
    public StudentDTO updateEnableStatus(String id,boolean enabled)throws ResourceNotFoundException{
        Student student= studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));
        student.setEnabled(enabled);
        return DtoUtils.studentToStudentDto(studentRepo.save(student));
    }
    
    public StudentDTO addSubjectMarks(String id, SubjectDetailsBody detailsBody)throws ResourceNotFoundException{
        Student student= studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));

        if(!subjectRepo.existsByName(detailsBody.getName().trim().toUpperCase()))
            throw new ResourceNotFoundException("No subject exists with name: "+detailsBody.getName());

        Student.SubjectDetails subjectDetails =
                new Student.SubjectDetails(
                        subjectRepo.findByName(detailsBody.getName().trim().toUpperCase()),
                        detailsBody.getMarks());
        if(student.getSubjects().contains(subjectDetails))
            student.getSubjects().remove(subjectDetails);

        student.getSubjects().add(subjectDetails);
        Student savedStudent =  studentRepo.save(student);
        return DtoUtils.studentToStudentDto(savedStudent);
    }

    public StudentDTO addALlSubjectDetails(String id,List<SubjectDetailsBody> details) throws ResourceNotFoundException{
        Student student = studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));
        details.stream().filter(detail-> subjectRepo.existsByName(detail.getName().toUpperCase().trim())).
                forEach(detail -> {
                Student.SubjectDetails subjectDetails =
                        new Student.SubjectDetails(subjectRepo.findByName(detail
                        .getName()
                        .trim()
                        .toUpperCase()),
                        detail.getMarks());

                if (student.getSubjects().contains(subjectDetails))
                    student.getSubjects().remove(subjectDetails);

                student.getSubjects().add(subjectDetails);
        });
        Student savedStudent=studentRepo.save(student);
        return DtoUtils.studentToStudentDto(savedStudent);
    }

    public StudentDTO removeSubject(String id,String subName) throws ResourceNotFoundException{
        subName=subName.toUpperCase().trim();
        Student student = studentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No student exists with Id: "+id));
        if(!subjectRepo.existsByName(subName))
            throw new ResourceNotFoundException("No subject exists with name: "+subName);
        Subject subject = subjectRepo.findByName(subName);
        Student.SubjectDetails subDetails = new Student.SubjectDetails(subject,0.0);
        if(student.getSubjects().contains(subDetails)){
            student.getSubjects().remove(subDetails);
        }
        Student updatedStudent = studentRepo.save(student);
        return DtoUtils.studentToStudentDto(updatedStudent);
    }

    public List<StudentDTO> createAllStudents(List<StudentCreateBody> studentsList){
        List<Student> studentList = studentsList.stream().map(studentBody -> getStudentFromStudentCreateBody(studentBody)).collect(Collectors.toList());
        return studentRepo.saveAll(studentList).stream().map(student-> DtoUtils.studentToStudentDto(student)).collect(Collectors.toList());
    }

    private Student getStudentFromStudentCreateBody(StudentCreateBody studentBody){
        Student student = new Student();
        student.setUsername(studentBody.getUsername().trim());
        student.setPassword(passwordEncoder.encode(studentBody.getPassword().trim()));
        student.setFirstName(studentBody.getFirstName().toUpperCase().trim());
        student.setLastName(studentBody.getLastName().toUpperCase().trim());
        student.setRole(roleRepo.findByName(EnumRoles.ROLE_STUDENT));
        student.setEnabled(studentBody.isEnabled());
        student.setEmail(studentBody.getEmail().trim());
        if('M' == studentBody.getGender().toUpperCase().charAt(0)){
            student.setGender("MALE");
        }else if('F' == studentBody.getGender().toUpperCase().charAt(0)){
            student.setGender("FEMALE");
        }else{
            student.setGender("OTHER");
        }

        studentBody.getSubjectDetails().stream()
                .filter(subDetail -> subjectRepo.existsByName(subDetail.getName().trim().toUpperCase()))
                .forEach(subDetail-> student.getSubjects()
                                        .add(new Student.SubjectDetails(subjectRepo.findByName(subDetail.getName().trim().toUpperCase()),
                                                subDetail.getMarks()))
                );
        return student;
    }


    
}
