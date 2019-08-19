package com.syn.MyLightsServer.group.persistence;

import com.syn.MyLightsServer.user.persistence.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "commandgroup")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String groupName;

	@NotNull
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private User user;

	public Group() {
		this("", new User());
	}

	public Group(@NotNull String groupName, @NotNull User user) {
		this.groupName = groupName;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
