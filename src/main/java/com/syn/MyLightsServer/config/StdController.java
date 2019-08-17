package com.syn.MyLightsServer.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StdController {

	@GetMapping("/")
	public String home() {
		return "/home.html";
	}
}
