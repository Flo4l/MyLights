package com.syn.MyLightsServer.stripe.persistence;

import com.syn.MyLightsServer.group.persistence.Group;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stripe")
public class Stripe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String ip;

	@NotNull
	private String mac;

	@NotNull
	private String name;

	@ManyToOne(targetEntity = Group.class, cascade = CascadeType.DETACH)
	private Group group;

	public Stripe() {
		this("", "");
	}

	public Stripe(@NotNull String ip, @NotNull String mac) {
		this.ip = ip;
		this.mac = mac;
		this.name = "New Stripe";
		this.group = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
