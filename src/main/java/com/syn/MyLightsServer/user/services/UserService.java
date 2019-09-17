package com.syn.MyLightsServer.user.services;

import com.syn.MyLightsServer.user.persistence.Role;
import com.syn.MyLightsServer.user.persistence.RoleRepository;
import com.syn.MyLightsServer.user.persistence.User;
import com.syn.MyLightsServer.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		createDefaultRolesIfNotExist();
		createDefaultUserIfNotExists();
	}

	private void createDefaultRolesIfNotExist() {
		if (roleRepository.findAll().size() < 1) {
			Role role = new Role("USER");
			roleRepository.save(role);
		}
	}

	private void createDefaultUserIfNotExists() {
		try {
			if (userRepository.findAll().size() < 1) {
				Properties properties = new Properties();
				properties.load(new FileInputStream("src/main/resources/application.properties"));
				String defaultPass = new BCryptPasswordEncoder().encode(
						properties.getProperty("application.user.defaultPassword"));
				String username = properties.getProperty("application.user.username");
				User user = new User(username, defaultPass);
				user.setEnabled(1);
				Role role = roleRepository.findByRole("USER");
				user.setRole(role);
				userRepository.save(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
