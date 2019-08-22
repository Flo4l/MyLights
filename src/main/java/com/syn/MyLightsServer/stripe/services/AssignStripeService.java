package com.syn.MyLightsServer.stripe.services;

import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.services.GroupService;
import com.syn.MyLightsServer.stripe.exceptions.InvalidStripeIpException;
import com.syn.MyLightsServer.stripe.exceptions.InvalidStripeMacException;
import com.syn.MyLightsServer.stripe.persistence.Stripe;
import com.syn.MyLightsServer.stripe.persistence.StripeRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AssignStripeService {

	private final StripeRepository stripeRepository;
	private final GroupService groupService;

	public AssignStripeService(StripeRepository stripeRepository, GroupService groupService) {
		this.stripeRepository = stripeRepository;
		this.groupService = groupService;
	}

	public void registerStripe(String ip, String mac) {
		try {
			Stripe stripe = stripeRepository.getByMac(mac);
			checkStripeIp(ip);
			checkStripeMac(mac);
			if (stripe == null) {
				stripe = new Stripe(ip, mac);
				stripeRepository.save(stripe);
			} else {
				stripe.setIp(ip);
				stripeRepository.save(stripe);
			}
		} catch (Exception e) {

		}
	}

	public void assignStripeToGroup(int stripeId, int groupId) {
		Stripe stripe = stripeRepository.getById(stripeId);
		Group group = groupService.getGroupById(groupId);
		if (group != null && stripe != null) {
			stripe.setGroup(group);
			stripeRepository.save(stripe);
		}
	}

	private void checkStripeIp(String ip) {
		Pattern pattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		if (!pattern.matcher(ip).matches()) {
			throw new InvalidStripeIpException();
		}
	}

	private void checkStripeMac(String mac) {
		Pattern pattern = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
		if (!pattern.matcher(mac).matches()) {
			throw new InvalidStripeMacException();
		}
	}
}
