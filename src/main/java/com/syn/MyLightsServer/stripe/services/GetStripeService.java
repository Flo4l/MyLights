package com.syn.MyLightsServer.stripe.services;

import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.services.GroupService;
import com.syn.MyLightsServer.stripe.persistence.Stripe;
import com.syn.MyLightsServer.stripe.persistence.StripeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetStripeService {

	private final StripeRepository stripeRepository;
	private final GroupService groupService;

	public GetStripeService(StripeRepository stripeRepository, GroupService groupService) {
		this.stripeRepository = stripeRepository;
		this.groupService = groupService;
	}

	public List<Stripe> getAllStripes() {
		return stripeRepository.findAll();
	}

	public List<Stripe> getUnassignedStripes() {
		return stripeRepository.getByGroupIsNull();
	}

	public List<Stripe> getStripeByGroup(int groupId) {
		Group group = groupService.getGroupById(groupId);
		return stripeRepository.getByGroup(group);
	}

	public String getAllStripesAsJSON() {
		return getStripeListAsJSON(getAllStripes());
	}

	public String getUnasignedStripesAsJSON() {
		return getStripeListAsJSON(getUnassignedStripes());
	}

	public String getStripeByGroupAsJSON(int groupId) {
		return getStripeListAsJSON(getStripeByGroup(groupId));
	}

	private String getStripeListAsJSON(List<Stripe> stripes) {
		String json = "\"stripes\":[";
		for (Stripe s : stripes) {
			json += s.toJSON();
			if (stripes.indexOf(s) < stripes.size() - 1) {
				json += ",";
			}
		}
		json += "]";
		return json;
	}
}
