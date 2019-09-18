package com.syn.MyLightsServer.user.controller;

import com.syn.MyLightsServer.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String login() {
		return "sites/login.html";
	}

	@PostMapping("/settings/changepass")
	@ResponseBody
	public String changePass(String oldPass, String newPass, String newPassCheck) {
		userService.changePass(oldPass, newPass, newPassCheck);
		return "";
	}
}
