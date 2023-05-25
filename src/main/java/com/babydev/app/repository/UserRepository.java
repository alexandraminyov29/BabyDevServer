package com.babydev.app.repository;

import com.babydev.app.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.isActive = ?2 " +
            "WHERE u.id = ?1")
    int updateActive(Long id,
                          boolean isActive);

	List<User> findAllByIsActive(boolean b);
	
}
