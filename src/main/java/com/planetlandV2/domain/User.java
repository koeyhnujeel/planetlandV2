package com.planetlandV2.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.planetlandV2.requset.Signup;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String password;

	private String nickname;

	private LocalDateTime createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Session> sessions = new ArrayList<>();

	@Builder
	public User(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.createdAt = LocalDateTime.now();
	}

	public Session addSession() {
		Session session = Session.builder()
			.user(this)
			.build();
		sessions.add(session);

		return session;
	}
}
