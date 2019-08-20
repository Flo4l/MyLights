package com.syn.MyLightsServer.command.services;

import com.syn.MyLightsServer.command.exceptions.InvalidColorIntervalException;
import com.syn.MyLightsServer.command.exceptions.InvalidCommandColorException;
import com.syn.MyLightsServer.command.exceptions.InvalidCommandGroupException;
import com.syn.MyLightsServer.command.exceptions.InvalidCommandModeException;
import com.syn.MyLightsServer.command.persistence.Color;
import com.syn.MyLightsServer.command.persistence.Command;
import com.syn.MyLightsServer.command.persistence.CommandRepository;
import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.services.GroupService;
import com.syn.MyLightsServer.stripe.services.MessageStripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class SetCommandService {

	private final CommandRepository commandRepository;
	private final GroupService groupService;
	private final MessageStripeService messageStripeService;

	@Autowired
	public SetCommandService(CommandRepository commandRepository, GroupService groupService, MessageStripeService messageStripeService) {
		this.commandRepository = commandRepository;
		this.groupService = groupService;
		this.messageStripeService = messageStripeService;
	}

	@Transactional
	public void setCommand(String jsonCommand) {
		try {
			Command command = extractCommand(jsonCommand);
			checkValidMode(command);
			checkValidInterval(command);
			checkValidColors(command);
			checkValidGroup(command);
			messageStripeService.sendCommand(command);
			commandRepository.removeAllByGroup(command.getGroup());
			commandRepository.save(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Command extractCommand(String jsonCommand) throws JSONException {

		JSONObject jsonObject = new JSONObject(jsonCommand).getJSONObject("command");

		char mode = jsonObject.get("mode").toString().charAt(0);
		int secondsToNextColor = jsonObject.getInt("secondsToNextColor");
		Group group = groupService.getGroupById(jsonObject.getInt("groupId"));
		JSONArray jsonColors = jsonObject.getJSONArray("colors");

		ArrayList<Color> colors = new ArrayList<>();
		for (int i = 0; i < jsonColors.length(); i++) {

			JSONObject jsonColor = jsonColors.getJSONObject(i);
			int red = jsonColor.getInt("red");
			int green = jsonColor.getInt("green");
			int blue = jsonColor.getInt("blue");
			Color color = new Color(red, green, blue);
			colors.add(color);
		}
		return new Command(secondsToNextColor, mode, group, colors);
	}

	private void checkValidMode(Command command) {
		if (command.getMode() == Command.FADE_COLOR_MODE ||
				command.getMode() == Command.MULTIPLE_COLOR_MODE ||
				command.getMode() == Command.SINGLE_COLOR_MODE) {
			return;
		}
		throw new InvalidCommandModeException();
	}

	private void checkValidColors(Command command) throws InvalidCommandColorException {
		command.getColors().stream().forEach(c -> {
			if (c.getRed() < 0 || c.getRed() > 255
					|| c.getGreen() < 0 || c.getGreen() > 255
					|| c.getBlue() < 0 || c.getBlue() > 255) {

				throw new InvalidCommandColorException();
			}
		});
	}

	private void checkValidInterval(Command command) throws InvalidColorIntervalException {
		if (command.getSecondsToNextColor() < 1
				|| command.getSecondsToNextColor() > 1200) {
			throw new InvalidColorIntervalException();
		}
	}

	private void checkValidGroup(Command command) throws InvalidCommandGroupException {
		if (command.getGroup() == null) {
			throw new InvalidCommandGroupException();
		}
	}
}
