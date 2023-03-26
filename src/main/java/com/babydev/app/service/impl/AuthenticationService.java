package com.babydev.app.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.auth.AuthenticationRequest;
import com.babydev.app.domain.auth.AuthenticationResponse;
import com.babydev.app.domain.auth.RegisterRequest;
import com.babydev.app.domain.entity.User;
import com.babydev.app.domain.entity.UserRole;
import com.babydev.app.exception.EmailIsTakenException;
import com.babydev.app.exception.EmailNotFoundException;
import com.babydev.app.exception.EmailNotValidException;
import com.babydev.app.exception.EmptyFieldException;
import com.babydev.app.exception.PasswordConditionsException;
import com.babydev.app.exception.PhoneNumberFormatException;
import com.babydev.app.exception.WrongPasswordException;
import com.babydev.app.helper.Cstl;
import com.babydev.app.repository.UserRepository;
import com.babydev.app.security.config.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationResponse register(RegisterRequest request) throws EmptyFieldException, EmailIsTakenException, EmailNotValidException, PhoneNumberFormatException, PasswordConditionsException {
		checkRegisterRequest(request);
		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(UserRole.STANDARD)
				.build();
		
		repository.save(user);
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request)
			throws EmailNotFoundException, WrongPasswordException, EmptyFieldException {
		
		if (request.getEmail().length() == 0 || request.getPassword().length() == 0) {
			throw new EmptyFieldException();
		}
		
		try {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch (BadCredentialsException e){
		Optional<User> userRec = repository.findByEmail(request.getEmail());
		if (userRec.isEmpty()) {
			throw new EmailNotFoundException();
		} else {
			throw new WrongPasswordException();
		}
		}
		User user = repository.findByEmail(request.getEmail()).orElseThrow();
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	private void checkRegisterRequest(RegisterRequest request) throws EmptyFieldException, EmailIsTakenException, EmailNotValidException, PhoneNumberFormatException, PasswordConditionsException {
		if (request.getFirstName() == null || request.getLastName() == null || request.getEmail() == null ||
				request.getPhoneNumber() == null || request.getPassword() == null) {
			throw new EmptyFieldException();
		}
		if (request.getFirstName().isEmpty() || request.getLastName().isEmpty() || request.getEmail().isEmpty() ||
				request.getPhoneNumber().isEmpty() || request.getPassword().isEmpty()) {
			throw new EmptyFieldException();
		} 
		if (!request.getPhoneNumber().matches("[0-9]+")) {
			throw new PhoneNumberFormatException();
		}
		
		if (!isPasswordValid(request.getPassword())) {
			throw new PasswordConditionsException();
		}
		checkEmail(request.getEmail());
	}
	
	private boolean isPasswordValid(final String password) {
		boolean hasUppercase = false;
		boolean hasLowercase = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;
		
		if(password.length() < Cstl.minimumPasswordLength) {
			return false;
		}
		
		for (char c : password.toCharArray()) {
			if (!hasUppercase) {
				hasUppercase = Character.isUpperCase(c);
			}
			if (!hasLowercase) {
				hasLowercase = Character.isLowerCase(c);
			}
			if (!hasDigit) {
				hasDigit = Character.isDigit(c);
			}
			if (!hasSpecialChar) {
				hasSpecialChar = !Character.isLetterOrDigit(c);
			}
		}
		return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
	}
	
	private void checkEmail(String email) throws EmailIsTakenException, EmailNotValidException {
		if (repository.findByEmail(email).isPresent()) {
			throw new EmailIsTakenException();
		} else if (!isValidEmail(email)) {
			throw new EmailNotValidException();
		}
	}
	
	private boolean isValidEmail(String email) {
		
        if (email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
            return false;
        }

        String[] parts = email.split("@");
        String recipient = parts[0];
        String domain = parts[1];
        
        if (recipient.length() == 0) {
        	return false;
        }

        for (char c : recipient.toCharArray()) {
            if (!Cstl.recipientAllowedCharacters.contains(c)) {
                return false;
            }
        }

        if (domain.indexOf('.') == -1 || domain.indexOf('.') == 0 || domain.indexOf('.') == domain.length() - 1) {
            return false;
        }

        String[] domainParts = domain.split("\\.");
        String topLevelDomain = domainParts[domainParts.length - 1];

        if (!retrieveAcceptedTopLevelDomain().contains(topLevelDomain)) {
            return false;
        }

        for (int i = 0; i < domainParts.length - 1; i++) {
            String domainSegment = domainParts[i];
            if (domainSegment.length() < 2) {
                return false;
            }
            for (char c : domainSegment.toCharArray()) {
                if (!Cstl.domainAllowedCharacters.contains(c)) {
                    return false;
                }
            }
        }

        return true;
	}
	
	private List<String> retrieveAcceptedTopLevelDomain() {
		String url = "https://data.iana.org/TLD/tlds-alpha-by-domain.txt";
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        List<String> tlds = new ArrayList<String>();
		try {
			response = client.execute(request);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		        
		        String line;
		        while ((line = reader.readLine()) != null) {
		            if (!line.startsWith("#") && !line.trim().isEmpty()) {
		                tlds.add(line.trim().toLowerCase());
		            }
		        }
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return tlds;
	}
	
	
}
