package com.babydev.app.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.babydev.app.domain.entity.RegistrationToken;

import jakarta.transaction.Transactional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<RegistrationToken, Long>{
	
    Optional<RegistrationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE RegistrationToken t " +
            "SET t.verifiedAt = ?2 " +
            "WHERE t.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}

