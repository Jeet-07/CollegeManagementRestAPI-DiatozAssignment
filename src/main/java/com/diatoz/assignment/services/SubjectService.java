package com.diatoz.assignment.services;

import com.diatoz.assignment.models.entities.Subject;
import com.diatoz.assignment.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepo;

    public List<Subject> getAllSubjects(){
        return subjectRepo.findAll();
    }
    public boolean existsByName(String name){
        return subjectRepo.existsByName(name);
    }

    public Subject findByName(String name) {
        return subjectRepo.findByName(name);
    }

    public Subject save(Subject subject){
        subject.setName(subject.getName().toUpperCase().trim());
        return subjectRepo.save(subject);
    }

    public Subject deleteByName(String name){
        Subject deletedSub = findByName(name);
        subjectRepo.deleteById(deletedSub.getId());
        return deletedSub;
    }
}
