package com.diatoz.assignment.security.customUserDetails;

import com.diatoz.assignment.models.entities.User;
import com.diatoz.assignment.repositories.AdminRepository;
import com.diatoz.assignment.repositories.StudentRepository;
import com.diatoz.assignment.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserFromDatabase(username).orElseThrow(()->
         new UsernameNotFoundException("No such user exists with username: "+username));
        return new CustomUserDetails(user);
    }

    private Optional<? extends User> getUserFromDatabase(String username){
        if(adminRepo.existsByUsername(username)){
            return adminRepo.findByUsername(username);
        }

        if(studentRepo.existsByUsername(username)){
            return studentRepo.findByUsername(username);
        }

        if(teacherRepo.existsByUsername(username)) {
            return teacherRepo.findByUsername(username);
        }

        return Optional.ofNullable(null);
    }
}
