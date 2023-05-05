package com.babydev.app.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.entity.RegistrationToken;
import com.babydev.app.repository.ConfirmationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
	
	@Autowired
	private final ConfirmationTokenRepository confirmationTokenRepository;
	
	public void saveConfirmationToken(RegistrationToken token) {
		confirmationTokenRepository.save(token);
	}
	
    public Optional<RegistrationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
