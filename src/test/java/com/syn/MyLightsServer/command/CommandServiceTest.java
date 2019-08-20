package com.syn.MyLightsServer.command;

import com.syn.MyLightsServer.command.persistence.CommandRepository;
import com.syn.MyLightsServer.command.services.SetCommandService;
import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.services.GroupService;
import com.syn.MyLightsServer.stripe.services.MessageStripeService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CommandServiceTest {

	@Mock
	private GroupService groupService;

	@Mock
	private CommandRepository commandRepository;

	@Mock
	private MessageStripeService messageStripeService;

	@InjectMocks
	private SetCommandService setCommandService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	String generateValidComandJsonString() {
		return "{\n" +
				"\t\"command\":\n" +
				"\t{\n" +
				"\t\t\"mode\":\"m\",\n" +
				"\t\t\"secondsToNextColor\":12,\n" +
				"\t\t\"groupId\":1,\n" +
				"\t\t\"colors\":\n" +
				"\t\t[\n" +
				"\t\t\t{\n" +
				"\t\t\t\t\"red\":255,\n" +
				"\t\t\t\t\"blue\":0,\n" +
				"\t\t\t\t\"green\":0\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\t\"red\":0,\n" +
				"\t\t\t\t\"blue\":255,\n" +
				"\t\t\t\t\"green\":0\n" +
				"\t\t\t},\n" +
				"\t\t\t{\n" +
				"\t\t\t\t\"red\":0,\n" +
				"\t\t\t\t\"blue\":0,\n" +
				"\t\t\t\t\"green\":255\n" +
				"\t\t\t}\n" +
				"\t\t]\n" +
				"\t}\n" +
				"}";
	}

	@Test
	@DisplayName("When the JSON String is valid the command should be saved")
	public void whenJsonDataValidCommandShouldBeSaved() {
		// given
		String json = generateValidComandJsonString();
		given(groupService.getGroupById(anyInt())).willReturn(new Group());

		// when
		setCommandService.setCommand(json);

		//then
		then(messageStripeService).should(times(1)).sendCommand(any());
		then(commandRepository).should(times(1)).save(any());
	}
}
