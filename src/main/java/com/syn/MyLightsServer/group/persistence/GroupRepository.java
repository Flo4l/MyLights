package com.syn.MyLightsServer.group.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

	Group findById(int id);

	Group findByGroupName(String groupName);
}
