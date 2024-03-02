package com.planetlandV2.Reader;

import org.springframework.stereotype.Component;

import com.planetlandV2.domain.User;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader{

	private final UserRepository userRepository;

	@Override
	public User read(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFound::new);
	}

	@Override
	public User read(String userNickname) {
		return userRepository.findByNickname(userNickname)
			.orElseThrow(UserNotFound::new);
	}
}
