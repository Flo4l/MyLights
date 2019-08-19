package com.syn.MyLightsServer.command.persistence;

import com.syn.MyLightsServer.group.persistence.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer> {

	Command findById(int id);

	Command findByGroup(Group group);
}
