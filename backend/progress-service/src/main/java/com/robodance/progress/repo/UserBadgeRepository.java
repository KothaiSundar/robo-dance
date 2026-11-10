package com.robodance.progress.repo;

import com.robodance.progress.entity.UserBadge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

	List<UserBadge> findByUserIdOrderByEarnedAtDesc(Long userId);

	boolean existsByUserIdAndBadgeCode(Long userId, String badgeCode);
}
