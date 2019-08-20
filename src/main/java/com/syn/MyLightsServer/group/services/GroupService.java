package com.syn.MyLightsServer.group.services;

import com.syn.MyLightsServer.group.exceptions.GroupAlreadyExistingException;
import com.syn.MyLightsServer.group.exceptions.InvalidGroupNameException;
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

	public void createGroup(String groupName) {
		try {
			Group group = new Group(groupName);
			checkGroupName(group);
			checkGroupAlredyExisting(group);
			groupRepository.save(group);
		} catch (Exception e) {

		}
	}

	public void deleteGroup(int groupId) {
		Group group = groupRepository.findById(groupId);
		groupRepository.delete(group);
	}

	public void updateGroupName(int groupId, String groupName) {
		try {
			Group group = new Group(groupName);
			group.setId(groupId);
			checkGroupName(group);
			checkGroupAlredyExisting(group);
			groupRepository.save(group);
		} catch (Exception e) {

		}
	}

	private void checkGroupName(Group group) throws InvalidGroupNameException {
		if (group.getGroupName().equals("")) {
			throw new InvalidGroupNameException();
		}
	}

	private void checkGroupAlredyExisting(Group group) throws GroupAlreadyExistingException {
		if (groupRepository.findByGroupName(group.getGroupName()) != null) {
			throw new GroupAlreadyExistingException();
		}
	}
}
