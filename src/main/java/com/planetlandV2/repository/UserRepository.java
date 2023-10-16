package com.planetlandV2.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.planetlandV2.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);
}
