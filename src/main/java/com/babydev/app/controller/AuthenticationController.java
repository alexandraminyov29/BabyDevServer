package com.babydev.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.babydev.app.domain.auth.AuthenticationRequest;
import com.babydev.app.domain.auth.AuthenticationResponse;
import com.babydev.app.domain.auth.RegisterRequest;
import com.babydev.app.exception.EmailIsTakenException;
import com.babydev.app.exception.EmailNotFoundException;
import com.babydev.app.exception.EmailNotValidException;
import com.babydev.app.exception.EmptyFieldException;
import com.babydev.app.exception.PasswordConditionsException;
import com.babydev.app.exception.PhoneNumberFormatException;
import com.babydev.app.exception.RegistrationTokenNotValidException;
import com.babydev.app.exception.WrongPasswordException;
import com.babydev.app.service.impl.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService service;
	
	@PostMapping("/register")
	public ResponseEntity<Object> register(
			@RequestBody RegisterRequest request
			) {
		try {
			service.register(request);
			return new ResponseEntity<Object>("OK", HttpStatus.OK);
		} catch (EmptyFieldException e) {
			return AdviceController.handleEmptyFieldException(e);
		} catch (EmailIsTakenException e) {
			return AdviceController.handleEmailIsTaken(e);
		} catch (EmailNotValidException e) {
			return AdviceController.handleEmailNotValid(e);
		} catch (PhoneNumberFormatException e) {
			return AdviceController.handlePhoneNumberFormat(e);
		} catch (PasswordConditionsException e) {
			return AdviceController.handlePasswordConditionsNotMet(e);
		} catch (RegistrationTokenNotValidException e) {
			return AdviceController.handleRegistrationTokenIsNotValid(e);
		}
	}
	
	@GetMapping("/confirm")
    public RedirectView confirm(@RequestParam("link") String token) {
        service.confirmToken(token);
        return new RedirectView("http://localhost:8081/login?activated=true");
    }
	
	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticate(
			@RequestBody AuthenticationRequest request
			) {
		
		AuthenticationResponse response;
		try {
			response = service.authenticate(request);
			return ResponseEntity.ok(response);
		} catch (EmailNotFoundException e) {
			return AdviceController.handleEmailNotFound(e);
		} catch (WrongPasswordException e) {
			return AdviceController.handleWrongPassword(e);
		} catch (EmptyFieldException e) {
			return AdviceController.handleEmptyFieldException(e);
		} catch (RegistrationTokenNotValidException e) {
			return AdviceController.handleRegistrationTokenIsNotValid(e);
		}
		
	}
}
