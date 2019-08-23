package com.syn.MyLightsServer.command.controller;

import com.syn.MyLightsServer.command.services.GetCommandService;
import com.syn.MyLightsServer.command.services.SetCommandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/command")
public class CommandController {

	private final SetCommandService setCommandService;
	private final GetCommandService getCommandService;

	public CommandController(SetCommandService setCommandService, GetCommandService getCommandService) {
		this.setCommandService = setCommandService;
		this.getCommandService = getCommandService;
	}

	@PostMapping("/set")
	@ResponseBody
	public String receiveCommand(String jsonData) {
		setCommandService.setCommand(jsonData);
		return "";
	}

	@PostMapping("/get/group")
	@ResponseBody
	public String getByGroup(int groupId) {
		return "{" + getCommandService.getCommandByGroupAsJSON(groupId) + "}";
	}

	@PostMapping("/get/all")
	@ResponseBody
	public String getAll() {
		return "{" + getCommandService.getAllCommandsAsJSON() + "}";
	}
}
