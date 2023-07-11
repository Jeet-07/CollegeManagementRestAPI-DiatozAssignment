package com.diatoz.assignment;

import com.diatoz.assignment.models.entities.Admin;
import com.diatoz.assignment.models.roles.EnumRoles;
import com.diatoz.assignment.models.roles.Role;
import com.diatoz.assignment.repositories.AdminRepository;
import com.diatoz.assignment.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class CollegeManagementApp {

	@Value("${default.admin.enabled}")
	private boolean defaultAdminEnabled;
	@Value("${default.admin.password}")
	private String defaultAdminPassword;

	@Autowired
	private final AdminRepository adminRepo;

	@Autowired
	private final RoleRepository roleRepo;
	@Autowired
	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init(){
		if(!roleRepo.existsByName(EnumRoles.ROLE_ADMIN)){
			roleRepo.save(new Role(EnumRoles.ROLE_ADMIN));
		}
		if(!roleRepo.existsByName(EnumRoles.ROLE_STUDENT)){
			roleRepo.save(new Role(EnumRoles.ROLE_STUDENT));
		}
		if(!roleRepo.existsByName(EnumRoles.ROLE_TEACHER)){
			roleRepo.save(new Role(EnumRoles.ROLE_TEACHER));
		}
		if(!adminRepo.existsByUsername("default_admin")){
			Admin admin = new Admin("default_admin",null,defaultAdminEnabled);
			admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
			admin.setRole(roleRepo.findByName(EnumRoles.ROLE_ADMIN));
			adminRepo.save(admin);
		}else{
			Admin admin = adminRepo.findByUsername("default_admin").get();
			admin.setEnabled(defaultAdminEnabled);
			admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
			adminRepo.save(admin);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(CollegeManagementApp.class, args);
	}

}
