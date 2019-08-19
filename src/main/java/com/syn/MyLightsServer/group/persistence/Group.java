package com.syn.MyLightsServer.group.persistence;

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

	public Group() {
		this("");
	}

	public Group(@NotNull String groupName) {
		this.groupName = groupName;
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

}
