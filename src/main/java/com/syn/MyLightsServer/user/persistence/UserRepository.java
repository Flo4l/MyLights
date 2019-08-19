package com.syn.MyLightsServer.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getById();

    User getByUsername(String username);
}
