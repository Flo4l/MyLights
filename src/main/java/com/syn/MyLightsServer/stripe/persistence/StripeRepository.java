package com.syn.MyLightsServer.stripe.persistence;

import com.syn.MyLightsServer.group.persistence.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StripeRepository extends JpaRepository<Stripe, Integer> {

	Stripe getById(int id);

	Stripe getByMac(String mac);

	List<Stripe> getByGroupIsNull();

	List<Stripe> getByGroup(Group group);
}
