package com.syn.MyLightsServer.stripe.services;

import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.services.GroupService;
import com.syn.MyLightsServer.stripe.persistence.Stripe;
import com.syn.MyLightsServer.stripe.persistence.StripeRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignStripeService {

	private final StripeRepository stripeRepository;
	private final GroupService groupService;

	public AssignStripeService(StripeRepository stripeRepository, GroupService groupService) {
		this.stripeRepository = stripeRepository;
		this.groupService = groupService;
	}

	public void assignStripeToGroup(int stripeId, int groupId) {
		Stripe stripe = stripeRepository.getById(stripeId);
		Group group = groupService.getGroupById(groupId);
		if (group != null && stripe != null) {
			stripe.setGroup(group);
			stripeRepository.save(stripe);
		}
	}
}
