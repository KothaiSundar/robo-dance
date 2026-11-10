package com.robodance.progress.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(
		name = "user_badges",
		uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "badgeCode"}))
public class UserBadge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, length = 60)
	private String badgeCode;

	@Column(nullable = false, length = 120)
	private String title;

	@Column(nullable = false, length = 255)
	private String description;

	@Column(nullable = false)
	private Instant earnedAt = Instant.now();

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBadgeCode() {
		return badgeCode;
	}

	public void setBadgeCode(String badgeCode) {
		this.badgeCode = badgeCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getEarnedAt() {
		return earnedAt;
	}

	public void setEarnedAt(Instant earnedAt) {
		this.earnedAt = earnedAt;
	}
}
