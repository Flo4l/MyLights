package com.syn.MyLightsServer.command.controller;

import com.syn.MyLightsServer.command.services.SetCommandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/command")
public class CommandController {

	private final SetCommandService setCommandService;

	public CommandController(SetCommandService setCommandService) {
		this.setCommandService = setCommandService;
	}

	@PostMapping("/set")
	@ResponseBody
	public String receiveCommand(String jsonData) {
		setCommandService.setCommand(jsonData);
		return "";
	}
}
