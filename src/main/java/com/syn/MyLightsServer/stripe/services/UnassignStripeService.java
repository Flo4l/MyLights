package com.syn.MyLightsServer.stripe.services;

import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.stripe.persistence.Stripe;
import com.syn.MyLightsServer.stripe.persistence.StripeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnassignStripeService {

	private final StripeRepository stripeRepository;

	@Autowired
	public UnassignStripeService(StripeRepository stripeRepository) {
		this.stripeRepository = stripeRepository;
	}

	public void unassignStripesFromGroup(Group group) {
		stripeRepository.getByGroup(group).forEach(s -> {
			s.setGroup(null);
			stripeRepository.save(s);
		});
	}

	public void unassignStripe(int stripeId) {
		Stripe stripe = stripeRepository.getById(stripeId);
		stripe.setGroup(null);
		stripeRepository.save(stripe);
	}
}
