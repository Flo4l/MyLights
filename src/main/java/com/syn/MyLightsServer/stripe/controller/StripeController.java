package com.syn.MyLightsServer.stripe.controller;

import com.syn.MyLightsServer.stripe.services.AssignStripeService;
import com.syn.MyLightsServer.stripe.services.GetStripeService;
import com.syn.MyLightsServer.stripe.services.UnassignStripeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stripe")
public class StripeController {

	private final AssignStripeService assignStripeService;
	private final GetStripeService getStripeService;
	private final UnassignStripeService unassignStripeService;

	public StripeController(AssignStripeService assignStripeService, GetStripeService getStripeService, UnassignStripeService unassignStripeService) {
		this.assignStripeService = assignStripeService;
		this.getStripeService = getStripeService;
		this.unassignStripeService = unassignStripeService;
	}

	@PostMapping("/register")
	@ResponseBody
	public String register(String ip, String mac) {
		assignStripeService.registerStripe(ip, mac);
		return "";
	}

	@PostMapping("/set/group")
	@ResponseBody
	public String setGroup(int stripeId, int groupId) {
		assignStripeService.assignStripeToGroup(stripeId, groupId);
		return "";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(int stripeId, String stripeName) {
		assignStripeService.updateStripeName(stripeId, stripeName);
		return "";
	}

	@PostMapping("/unassign")
	@ResponseBody
	public String update(int stripeId) {
		unassignStripeService.unassignStripe(stripeId);
		return "";
	}

	@PostMapping("/get/all")
	@ResponseBody
	public String getAll() {
		return "{" + getStripeService.getAllStripesAsJSON() + "}";
	}

	@PostMapping("/get/group")
	@ResponseBody
	public String getByGroup(int groupId) {
		return "{" + getStripeService.getStripeByGroupAsJSON(groupId) + "}";
	}

	@PostMapping("/get/unassigned")
	@ResponseBody
	public String getUnassigned() {
		return "{" + getStripeService.getUnasignedStripesAsJSON() + "}";
	}
}
