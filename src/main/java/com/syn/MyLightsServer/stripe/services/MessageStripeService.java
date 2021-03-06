package com.syn.MyLightsServer.stripe.services;

import com.syn.MyLightsServer.command.persistence.Command;
import com.syn.MyLightsServer.group.services.GroupService;
import com.syn.MyLightsServer.stripe.persistence.Stripe;
import com.syn.MyLightsServer.stripe.persistence.StripeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

@Service
public class MessageStripeService {

	private final StripeRepository stripeRepository;
	private final GroupService groupService;

	public final int STRIPE_PORT = 8881;

	@Autowired
	public MessageStripeService(StripeRepository stripeRepository, GroupService groupService) {
		this.stripeRepository = stripeRepository;
		this.groupService = groupService;
	}

	public void sendCommand(Command command) {
		List<Stripe> targetStripes = stripeRepository.getByGroup(command.getGroup());
		targetStripes.stream().forEach(s -> {
			sendMessage(s, "{\"command\":" + command.toJSON() + "}");
		});
	}

	private void sendMessage(Stripe stripe, String message) {
		new Thread(() -> {
			try {
				Socket socket = new Socket(stripe.getIp(), STRIPE_PORT);
				OutputStream os = socket.getOutputStream();
				os.write(message.getBytes());
				os.flush();
				Thread.sleep(50);
				os.close();
				socket.close();
			} catch (IOException | InterruptedException e) {}
		}).start();
	}
}
