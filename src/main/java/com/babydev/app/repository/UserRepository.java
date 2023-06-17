package com.babydev.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.babydev.app.domain.entity.User;
import com.babydev.app.domain.entity.UserRole;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAllByRoleAndIsActive(UserRole role, Boolean isActive);

	Optional<User> findByEmail(String email);
	
    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.isActive = ?2 " +
            "WHERE u.id = ?1")
    int updateActive(Long id,
                          boolean isActive);

    @Modifying
    @Query("DELETE User u " +
    		"WHERE u.id = ?1")
    void deleteById(Long id);
    
	List<User> findAllByIsActive(boolean b);
	
}
