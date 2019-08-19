package com.syn.MyLightsServer.group.services;

import com.syn.MyLightsServer.group.persistence.Group;
import com.syn.MyLightsServer.group.persistence.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

	private final GroupRepository groupRepository;

	@Autowired
	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	public Group getGroupById(int id) {
		return groupRepository.findById(id);
	}
}
