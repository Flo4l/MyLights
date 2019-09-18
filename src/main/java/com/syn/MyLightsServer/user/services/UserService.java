package com.syn.MyLightsServer.user.services;

import com.syn.MyLightsServer.user.persistence.Role;
import com.syn.MyLightsServer.user.persistence.RoleRepository;
import com.syn.MyLightsServer.user.persistence.User;
import com.syn.MyLightsServer.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	private String username;
	private String defaultPass;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		loadProperties();
		createDefaultRolesIfNotExist();
		createDefaultUserIfNotExists();
	}

	private void loadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream("src/main/resources/application.properties"));
			defaultPass = new BCryptPasswordEncoder().encode(
					properties.getProperty("application.user.defaultPassword"));
			username = properties.getProperty("application.user.username");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDefaultRolesIfNotExist() {
		if (roleRepository.findAll().size() < 1) {
			Role role = new Role("USER");
			roleRepository.save(role);
		}
	}

	private void createDefaultUserIfNotExists() {
		if (userRepository.findAll().size() < 1) {
			User user = new User(username, defaultPass);
			user.setEnabled(1);
			Role role = roleRepository.findByRole("USER");
			user.setRole(role);
			userRepository.save(user);
		}
	}

	public void changePass(String oldPass, String newPass, String newPassCheck) {
		User user = userRepository.getByUsername(username);
		checkCorrectPass(user, oldPass);
		checkSamePass(newPass, newPassCheck);
		user.setPassword(
				passwordEncoder.encode(newPass));
		userRepository.save(user);
	}

	private void checkCorrectPass(User user, String pass) {
		if (!passwordEncoder.matches(pass, user.getPassword())) {
			throw new IllegalArgumentException();
		}
	}

	private void checkSamePass(String pass, String passCheck) {
		if (!pass.equals(passCheck)) {
			throw new IllegalArgumentException();
		}
	}
}
