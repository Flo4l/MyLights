package com.syn.MyLightsServer.stripe.controller;

import com.syn.MyLightsServer.stripe.services.AssignStripeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/stripe")
public class StripeController {

	private final AssignStripeService assignStripeService;

	public StripeController(AssignStripeService assignStripeService) {
		this.assignStripeService = assignStripeService;
	}

	@PostMapping("/register")
	@ResponseBody
	public String register(String ip, String mac) {
		assignStripeService.registerStripe(ip, mac);
		return "";
	}

	@PostMapping("/setgroup")
	@ResponseBody
	public String setGroup(int stripeId, int groupId, HttpServletRequest request) {
		assignStripeService.assignStripeToGroup(stripeId, groupId);
		return "";
	}
}
