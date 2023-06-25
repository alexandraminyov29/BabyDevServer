package com.babydev.app;

import com.babydev.app.domain.auth.AuthenticationRequest;
import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.JobType;
import com.babydev.app.domain.entity.Location;
import com.babydev.app.exception.*;
import com.babydev.app.service.impl.AuthenticationService;
import com.babydev.app.service.impl.CompanyService;
import com.babydev.app.service.impl.JobService;
import com.babydev.app.service.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class JobTests {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;

    private String recruiterToken;
    private String standardToken;

    @BeforeEach
    public void prepareData() {
        try {
            recruiterToken = "Bearer " + authService.authenticate(AuthenticationRequest.builder()
                    .email("recruiter2998@gmail.com")
                    .password("Parola1!")
                    .build()).getToken();
            standardToken = "Bearer " + authService.authenticate(AuthenticationRequest.builder()
                    .email("alexandraminyov773@gmail.com")
                    .password("Parola1!")
                    .build()).getToken();
            jobService.addJob(recruiterToken, new JobPageDTO(Job
                    .builder()
                    .title("Junior Java Developer")
                    .location(Location.TIMISOARA)
                    .type(JobType.Full_time)
                    .postDate(LocalDate.now())
                    .experienceRequired("2-5 years")
                    .description("sample description")
                    .company(companyService.getCompany((long) 1))
                    .build())
            );
        } catch (EmailNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (WrongPasswordException e) {
            e.printStackTrace();
            fail();
        } catch (EmptyFieldException e) {
            e.printStackTrace();
            fail();
        } catch (RegistrationTokenNotValidException e) {
            e.printStackTrace();
            fail();
        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @AfterEach
    public void destroyData() {
        JobListViewTypeDTO jobToDestroy = jobService.getAllJobs(standardToken).get(0);
        jobService.deleteJob(jobToDestroy.getId());
    }

    @Test
    public void testAddJobToFavorites() {
        List<Job> favoriteJobs = userService.getFavoriteJobs(standardToken);
        int favoriteJobsInitialLength = favoriteJobs.size();
        List<JobListViewTypeDTO> jobList = jobService.getAllJobs(standardToken);

        // add job to favorites
        jobService.addJobToFavorites(standardToken, jobList.get(0).getId());
        favoriteJobs = userService.getFavoriteJobs(standardToken);
        assertEquals(favoriteJobsInitialLength + 1, favoriteJobs.size());

        // remove job from favorites
        jobService.addJobToFavorites(standardToken, jobList.get(0).getId());
        favoriteJobs = userService.getFavoriteJobs(standardToken);
        assertEquals(favoriteJobsInitialLength, favoriteJobs.size());

    }
}
