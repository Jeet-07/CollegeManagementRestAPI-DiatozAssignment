package com.diatoz.assignment.services;

import com.diatoz.assignment.models.dto.AdminDTO;
import com.diatoz.assignment.models.dto.DtoUtils;
import com.diatoz.assignment.models.entities.Admin;
import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.roles.EnumRoles;
import com.diatoz.assignment.models.roles.Role;
import com.diatoz.assignment.payload.request.create.AdminCreateBody;
import com.diatoz.assignment.payload.request.updation.AdminUpdateBody;
import com.diatoz.assignment.repositories.AdminRepository;
import com.diatoz.assignment.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepo;

    @Autowired private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username){
        return adminRepo.existsByUsername(username.trim());
    }
    public boolean existsById(String id){
        return adminRepo.existsById(id);
    }

    public AdminDTO getAdminById(String id) throws ResourceNotFoundException {
        Admin admin=adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No such user found with id: "+id));
        return DtoUtils.adminToAdminDto(admin);
    }
    public AdminDTO save(AdminCreateBody adminBody){
        Admin newAdmin = new Admin();
        newAdmin.setUsername(adminBody.getUsername());
        newAdmin.setPassword(passwordEncoder.encode(adminBody.getPassword().trim()));
        if(!adminBody.getFirstName().isEmpty()
                && !adminBody.getFirstName().trim().isEmpty())
            newAdmin.setFirstName(adminBody.getFirstName().trim().toUpperCase());
        if(!adminBody.getLastName().isEmpty()
                && !adminBody.getLastName().trim().isEmpty())
            newAdmin.setLastName(adminBody.getLastName().trim().toUpperCase());
        newAdmin.setEnabled(adminBody.isEnabled());
        Role role = roleRepo.findByName(EnumRoles.ROLE_ADMIN);
        newAdmin.setRole(role);
        return DtoUtils.adminToAdminDto(adminRepo.save(newAdmin));
    }

    public AdminDTO update(String id, AdminUpdateBody adminBody) throws ResourceNotFoundException{
        Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No such user found with id: "+id));
        admin.setUsername(adminBody.getUsername());
        admin.setPassword(passwordEncoder.encode(adminBody.getPassword()));
        admin.setFirstName(adminBody.getFirstName().trim().toUpperCase());
        admin.setLastName(adminBody.getLastName().trim().toUpperCase());
        return DtoUtils.adminToAdminDto(adminRepo.save(admin));
    }

    public AdminDTO deleteById(String id) throws ResourceNotFoundException {
        if(adminRepo.existsById(id)) {
            Admin deletedAdmin = adminRepo.findById(id).get();
            adminRepo.deleteById(id);
            return DtoUtils.adminToAdminDto(deletedAdmin);
        }else
            throw new ResourceNotFoundException("No such user found with id: "+id);
    }

    public List<AdminDTO> getAllAdmins(){
        return adminRepo.findAll().stream().map(admin -> DtoUtils.adminToAdminDto(admin)).collect(Collectors.toList());
    }

    public AdminDTO updateEnableStatus(String id,boolean enabled) throws ResourceNotFoundException {
        Admin admin = adminRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No such user found with id: "+id));
        admin.setEnabled(enabled);
        adminRepo.save(admin);
        return DtoUtils.adminToAdminDto(admin);
    }

}
