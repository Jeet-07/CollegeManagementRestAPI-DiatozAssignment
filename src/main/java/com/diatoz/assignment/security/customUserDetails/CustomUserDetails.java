package com.diatoz.assignment.security.customUserDetails;

import com.diatoz.assignment.models.entities.Admin;
import com.diatoz.assignment.models.entities.Student;
import com.diatoz.assignment.models.entities.Teacher;
import com.diatoz.assignment.models.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private String id;
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private boolean enabled;

    private String userType;
    public CustomUserDetails(User user){
        setDetails(user);
    }
    private void setDetails(User user){
        if(user instanceof Admin){
            Admin admin = (Admin)user;
            this.id=admin.getId();
            this.username=admin.getUsername();
            this.password=admin.getPassword();
            this.enabled=admin.isEnabled();
            this.userType="ADMIN";
            this.authorities = List.of(new SimpleGrantedAuthority(admin.getRole().getName().name()));
        }
        if(user instanceof Student){
            Student student = (Student)user;
            this.id = student.getId();
            this.username = student.getUsername();
            this.password = student.getPassword();
            this.enabled=student.isEnabled();
            this.userType="STUDENT";
            this.authorities = List.of(new SimpleGrantedAuthority(student.getRole().getName().name()));
        }
        if(user instanceof Teacher){
            Teacher teacher = (Teacher)user;
            this.id = teacher.getId();
            this.username = teacher.getUsername();
            this.password = teacher.getPassword();
            teacher.isEnabled();
            this.userType="TEACHER";
            this.authorities=List.of(new SimpleGrantedAuthority(teacher.getRole().getName().name()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
