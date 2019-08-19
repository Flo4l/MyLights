package com.syn.MyLightsServer.group.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

	List<Group> findAllByIdIsNotNull();

	Group findById(int id);
}
