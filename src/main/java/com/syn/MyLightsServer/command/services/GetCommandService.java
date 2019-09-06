package com.syn.MyLightsServer.command.services;

import com.syn.MyLightsServer.command.persistence.Command;
import com.syn.MyLightsServer.command.persistence.CommandRepository;
import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.services.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCommandService {

	private final CommandRepository commandRepository;
	private final GroupService groupService;

	public GetCommandService(CommandRepository commandRepository, GroupService groupService) {
		this.commandRepository = commandRepository;
		this.groupService = groupService;
	}

	public List<Command> getAllCommands() {
		return commandRepository.findAll();
	}

	public Command getCommandByGroup(int groupId) {
		Group group = groupService.getGroupById(groupId);
		return commandRepository.findByGroup(group);
	}

	public String getAllCommandsAsJSON() {
		String json = "\"commands\":[";
		List<Command> commands = getAllCommands();
		for (Command c : commands) {
			json += c.toJSON();
			if (commands.indexOf(c) < commands.size() - 1) {
				json += ",";
			}
		}
		json += "]";
		return json;
	}

	public String getCommandByGroupAsJSON(int groupId) {
		Command command = getCommandByGroup(groupId);
		if (command == null) {
			return "";
		}
		String json = "\"command\":" + command.toJSON();
		return json;
	}
}
