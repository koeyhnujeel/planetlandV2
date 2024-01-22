package com.planetlandV2.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
	Optional<Session> findByAccessToken(String accessToken);

	Optional<Session> findByUser(User user);
}
