package com.syn.MyLightsServer.group.controller;

import com.syn.MyLightsServer.group.services.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/group")
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@PostMapping("/create")
	@ResponseBody
	public String create(String groupName) {
		groupService.createGroup(groupName);
		return "";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(int groupId, String groupName) {
		groupService.updateGroupName(groupId, groupName);
		return "";
	}

	@PostMapping("/delete")
	@ResponseBody
	public String delete(int groupId) {
		groupService.deleteGroup(groupId);
		return "";
	}

	@PostMapping("/get/all")
	@ResponseBody
	public String get() {
		return "{" + groupService.getAllGroupsAsJSON() + "}";
	}
}
