package com.syn.MyLightsServer.group;

import com.syn.MyLightsServer.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "commandgroup")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupID;

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

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
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
