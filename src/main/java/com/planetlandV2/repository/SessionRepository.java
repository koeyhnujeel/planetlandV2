package com.planetlandV2.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;

public interface SessionRepository extends CrudRepository<Session, Long> {
	Optional<Session> findByAccessToken(String accessToken);
}
