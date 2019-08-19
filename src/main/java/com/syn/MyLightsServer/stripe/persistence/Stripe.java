package com.syn.MyLightsServer.stripe.persistence;

import com.syn.MyLightsServer.group.persistence.Group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stripe")
public class Stripe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int stripeID;

	@NotNull
	private String mac;

	@NotNull
	private String name;

	@ManyToOne(targetEntity = Group.class, cascade = CascadeType.DETACH)
	private Group group;

	public Stripe() {
		this("", "");
	}

	public Stripe(@NotNull String mac, @NotNull String name) {
		this.mac = mac;
		this.name = name;
	}

	public int getStripeID() {
		return stripeID;
	}

	public void setStripeID(int stripeID) {
		this.stripeID = stripeID;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
