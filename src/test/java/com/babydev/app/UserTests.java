package com.babydev.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.babydev.app.domain.auth.AuthenticationRequest;
import com.babydev.app.domain.dto.CompanyInfoDTO;
import com.babydev.app.domain.dto.RecruiterRequest;
import com.babydev.app.domain.dto.RecruiterRequestListViewType;
import com.babydev.app.exception.EmailNotFoundException;
import com.babydev.app.exception.EmptyFieldException;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.exception.RegistrationTokenNotValidException;
import com.babydev.app.exception.WrongPasswordException;
import com.babydev.app.service.impl.AuthenticationService;
import com.babydev.app.service.impl.CompanyService;
import com.babydev.app.service.impl.UserService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class UserTests {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationService authService;
	@Autowired 
	private CompanyService companyService;
	
	private String adminToken;
	
	@BeforeEach
	public void prepareData() {
		try {
			adminToken = "Bearer " + authService.authenticate(AuthenticationRequest.builder()
					.email("babydev.master@gmail.com")
					.password("dodelnicoLita12!")
					.build()).getToken();
		} catch (EmailNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RegistrationTokenNotValidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createRecruiterRequest(String firstName, String email) {
		userService.requestRecruiterAccount(RecruiterRequest.builder()
				.firstName(firstName)
				.lastName("Popescu")
				.email(email)
				.password("Bxb@1aaa")
				.phoneNumber("0404040404")
				.company(CompanyInfoDTO.builder()
						.existent(false)
						.name("newCompany")
						.webPage("url-somewhere")
						.build())
				.build());
	}
	@Test
	public void testInsertRecruiterRequest() {
		int initialLength = 0;
		List<RecruiterRequestListViewType> requests = null;
		String dummyEmail = "john.cena29@yahoo.com";
		try {
			initialLength = userService.displayRequestListData(adminToken).size();
			createRecruiterRequest("John", dummyEmail);

			requests = userService.displayRequestListData(adminToken);
			assertEquals(requests.size(), initialLength + 1);
			userService.deleteRequestFromRecruiter(adminToken, dummyEmail);
		} catch (NotAuthorizedException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	
	@Test
	public void testResolveRecruiterRequests() {
		int initialLength = 0;
		try {
			initialLength = userService.displayRequestListData(adminToken).size();
			createRecruiterRequest("John", "john.cena29@yahoo.com");
			userService.requestRecruiterAccount(RecruiterRequest.builder()
					.firstName("Daniel")
					.lastName("Popescu")
					.email("daniel112@yahoo.com")
					.password("Bxb@1aaa")
					.phoneNumber("0404040404")
					.company(CompanyInfoDTO.builder()
							.existent(true)
							.name("newCompany")
							.build())
					.build());
			List<RecruiterRequestListViewType> requests = null;
			requests = userService.displayRequestListData(adminToken);

			assertEquals(requests.size(), initialLength + 2);
			userService.deleteRequestFromRecruiter(adminToken, "john.cena29@yahoo.com");
			assertNotNull(companyService.getCompanyByName("newCompany"));
			userService.deleteRequestFromRecruiter(adminToken, "daniel112@yahoo.com");
			companyService.getCompanyByName("newCompany");
			fail();
		} catch (NotAuthorizedException e) {
			e.printStackTrace();
			fail();
		} catch (EntityNotFoundException e) {
			try {
				assertEquals(userService.displayRequestListData(adminToken).size(), initialLength);
			} catch (NotAuthorizedException e1) {
				e.printStackTrace();
				fail();
			}
		}
	}
}
