package com.planetlandV2.Reader;

import com.planetlandV2.domain.User;

public interface UserReader {

	User read(Long userId);

	User read(String userNickname);
}
