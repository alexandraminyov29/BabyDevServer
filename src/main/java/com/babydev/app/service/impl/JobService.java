package com.babydev.app.service.impl;

import com.babydev.app.domain.dto.ApplicantsDTO;
import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.*;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.ImageUtil;
import com.babydev.app.helper.Permissions;
import com.babydev.app.repository.CompanyRepository;
import com.babydev.app.repository.JobRepository;
import com.babydev.app.security.config.JwtService;
import com.babydev.app.service.facade.JobServiceFacade;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobService implements JobServiceFacade {

    private final JobRepository jobRepository;
    private final UserService userService;
    private final CompanyRepository companyRepository;

    private final JwtService jwtService;

    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).get();
    }

    public JobPageDTO getJobPageById(String token, Long id) {

        User user = userService.getUserFromToken(token);
        return jobRepository.findJobDetails(id, user.getUserId());
    }

    public List<JobListViewTypeDTO> getAllRecruiterJobs(String token) throws NotAuthorizedException {
        User user = userService.getUserFromToken(token);
        final Company userCompany = user.getCompany();
        if(!Permissions.isRecruiter(user)) {
            throw new NotAuthorizedException();
        }

        return jobRepository.findAllRecruiterJobs(userCompany.getCompanyId().toString());
    }

    private JobListViewTypeDTO mapJobToDTO(Job job) {
        final Company company = job.getCompany();
        return JobListViewTypeDTO.builder()
                .id(job.getJobId())
                .title(job.getTitle())
                .location(job.getLocation())
                .type(job.getType())
                .postedDate(job.getPostDate().toString())
                .experienceRequired(job.getExperienceRequired())
                .companyId(company.getCompanyId())
                .name(company.getName())
                .image(company.getImage()).
                build();
    }

    private JobPageDTO mapJobPageToDTO(Job job) {
        return new JobPageDTO(job);
    }

    private Job mapJobPageDTOToJob(JobPageDTO jobPageDTO, Company company) {
        return Job.builder()
                .jobId(jobPageDTO.getId())
                .title(jobPageDTO.getTitle())
                .description(jobPageDTO.getDescription())
                .location(jobPageDTO.getLocation())
                .type(jobPageDTO.getType())
                .promotedUntil(LocalDateTime.of(2023, 12, 31, 1, 2))
                .postDate(LocalDate.now())
                .experienceRequired(jobPageDTO.getExperienceRequired())
                .company(company)
                .build();
    }

    public void addJob(String token, JobPageDTO jobPageDTO) throws NotAuthorizedException {
        final User user = userService.getUserFromToken(token);
        final Company userCompany = user.getCompany();
        if(!Permissions.isRecruiter(user)) {
            throw  new NotAuthorizedException();
        }
        jobRepository.save( mapJobPageDTOToJob(jobPageDTO, userCompany));
    }

    public void editJob(String token, Job job, Long jobId) throws NotAuthorizedException {
        final User user = userService.getUserFromToken(token);
        final Company userCompany = user.getCompany();
        if(!Permissions.isRecruiter(user)) {
            throw  new NotAuthorizedException();
        }
        Job updatedJob = jobRepository.findById(jobId).get();
        updatedJob.setTitle(job.getTitle());
        updatedJob.setDescription(job.getDescription());
        updatedJob.setLocation(job.getLocation());
        updatedJob.setType(job.getType());
        updatedJob.setExperienceRequired(job.getExperienceRequired());
        jobRepository.save(updatedJob);
    }
    public List<JobListViewTypeDTO> getFavoriteJobs(String token) {
        Long userId = jwtService.extractUserIdFromToken(token);
        User user = userService.findById(userId).get();
        List<Job> favoriteJobs = user.getFavoriteJobs();
        List <JobListViewTypeDTO> jobResult = new ArrayList<>();
        JobListViewTypeDTO jobItem;
        for (Job job : favoriteJobs) {
            jobItem = mapJobToDTO(job);
            jobItem.setImage(ImageUtil.decompressImage(job.getCompany().getImage()));
            jobItem.setFavorite(true);
            jobResult.add(jobItem);
        }
        return jobResult;
    }

    public List<JobListViewTypeDTO> getAppliedJobs(String token) {
        Long userId = jwtService.extractUserIdFromToken(token);
        User user = userService.findById(userId).get();
        List<Job> appliedJobs = user.getAppliedJobs();
        List<JobListViewTypeDTO> jobResult = new ArrayList<>();
        JobListViewTypeDTO jobItem;
        for(Job job : appliedJobs) {
            jobItem = mapJobToDTO(job);
            jobItem.setImage(ImageUtil.decompressImage(job.getCompany().getImage()));
            jobResult.add(jobItem);
        }
        return jobResult;
    }
    public List<String> findSkillInJobDescription(String inputText) throws IOException, InterruptedException {
        String scriptPath = "files/scripts/skillScript.py";

        ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath, inputText);
        processBuilder.redirectErrorStream(true);

        List<String> output = new ArrayList<>()  {};

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
    public List<String> findLocationInJobDescription(String input_text) throws IOException, InterruptedException {
        String scriptPath = "files/scripts/helloWorld.py";

        ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptPath, input_text);
        processBuilder.redirectErrorStream(true);

        List<String> output = new ArrayList<>()  {};

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    public int matchLocation(User user, Job job ) {
        try {

            List<String> jobLocation = findLocationInJobDescription(job.getDescription());
            String userLocation = user.getLocation().toString();
            userLocation = userLocation.trim().toLowerCase();

            for (String jobLoc : jobLocation) {
                jobLoc = jobLoc.trim().toLowerCase();

                if (jobLoc.contains(userLocation)) {
                    return 1;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        return 0;
    }

    public int matchSkills(User user, Job job) {
        int score = 0;

        try {

            List<String> jobSkill = findSkillInJobDescription(job.getDescription());



            for (Skill skill : user.getSkills()) {
                for (String skillRequired : jobSkill) {
                    if (skill.getSkillName().toString().equalsIgnoreCase(skillRequired)) {
                        score += skill.getSkillExperience().ordinal() + 1;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        return score;
    }

    public CompletableFuture<Integer> calculateScore(User user, Job job) {
        CompletableFuture<Integer> locationScoreFuture = CompletableFuture.supplyAsync(() -> matchLocation(user, job) * 5);
        CompletableFuture<Integer> skillScoreFuture = CompletableFuture.supplyAsync(() -> matchSkills(user, job));

        return locationScoreFuture.thenCombine(skillScoreFuture, Integer::sum);
    }

    public List<JobListViewTypeDTO> sortByScore(String token) {
        List<Job> jobs = jobRepository.findAll();
        Long userId = jwtService.extractUserIdFromToken(token);
        Optional<User> user = userService.findById(userId);
        List <JobListViewTypeDTO> jobsResult = new ArrayList<>();

        for (Job job : jobs) {
            final JobListViewTypeDTO newJob = mapJobToDTO(job);
            CompletableFuture<Integer> newScore = calculateScore(user.get(), job);

            newScore.thenAccept( score -> {
                newJob.setScore(score);
                newJob.setImage(ImageUtil.decompressImage(job.getCompany().getImage()));
                jobsResult.add(newJob);
            }).join();
        }

        return jobsResult.stream()
                .sorted(Comparator.comparingInt(JobListViewTypeDTO::getScore).reversed())
                .collect(Collectors.toList());
    }

    public List<JobListViewTypeDTO> searchJobs(String keyword, String token) {
        String userId;
        if (token != "") {
            userId = jwtService.extractUserIdFromToken(token).toString();
        } else {
            userId = "";
        }

        return jobRepository.searchJobs(keyword, userId);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public JobPageDTO findJobById(Long jobId, Long userId) {
        return jobRepository.findJobDetails(jobId, userId);
    }

    public List<JobListViewTypeDTO> getJobsByLocation(String token, String location) {
        List<JobListViewTypeDTO> jobs = getAllJobs(token);

        List<JobListViewTypeDTO> filteredJobs = jobs.stream()
                .filter(j -> j.getLocation() == Location.valueOf(location))
                .collect(Collectors.toList());
        if(filteredJobs.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find job");
        } else {
            return filteredJobs;
        }
    }

    public List<JobListViewTypeDTO> getJobsByType(String token, String jobType) {
        List<JobListViewTypeDTO> jobs = getAllJobs(token);

        List<JobListViewTypeDTO> filteredJobs = jobs.stream()
                .filter(j -> j.getType() == JobType.valueOf(jobType))
                .collect(Collectors.toList());
        if(filteredJobs.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find job");
        } else {
            return filteredJobs;
        }
    }

    public List<JobListViewTypeDTO> getAllJobs(String token) {
        Long userId = userService.getUserFromToken(token).getUserId();
        return jobRepository.findAll(userId.toString());
    }


    public void applyJob(String token, Long jobId) throws RuntimeException {
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }
        Long userId = jwtService.extractUserIdFromToken(token);
        Optional<User> user = userService.findById(userId);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find user");
        }
        List<User> applicants = job.get().getApplicants();
        // Check if user already applied to the given job
        for (User applicant : applicants) {
            if (applicant.getUserId() == userId) {
                throw new RuntimeException("You've already applied!");
            }
        }
        job.get().getApplicants().add(user.get());
        user.get().getAppliedJobs().add(job.get());
        userService.save(user.get());
        jobRepository.save(job.get());
    }

    @Transactional
    public boolean addJobToFavorites(String token, Long jobId) {

        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }

        Long userId = jwtService.extractUserIdFromToken(token);
        Optional<User> user = userService.findById(userId);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find user");
        }
        boolean isFavorite = user.get().getFavoriteJobs().contains(job.get());

        if (isFavorite) {
            user.get().getFavoriteJobs().remove(job.get());
            job.get().getUsersFavorites().remove(user.get());
        } else {
            user.get().getFavoriteJobs().add(job.get());
            job.get().getUsersFavorites().add(user.get());
        }
        userService.save(user.get());
        jobRepository.save(job.get());

        return isFavorite;
    }

    public List<ApplicantsDTO> getApplicants(String token, Long jobId) throws NotAuthorizedException {
        final User user = userService.getUserFromToken(token);
        final Company userCompany = user.getCompany();
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }
        if(Permissions.isStandard(user) || job.get().getCompany().getCompanyId() != userCompany.getCompanyId()) {
            throw  new NotAuthorizedException();
        }
        final List<User> applicants = job.get().getApplicants();
        List<ApplicantsDTO> result = new ArrayList<ApplicantsDTO>();
        for (User applicant : applicants) {
            result.add(ApplicantsDTO.builder()
                    .id(applicant.getUserId())
                    .firstName(applicant.getFirstName())
                    .lastName(applicant.getLastName())
                    .email(applicant.getEmail())
//                            .location(applicant.getLocation().getName())
                    .image(ImageUtil.decompressImage(applicant.getImageData()))
                    .build());
        }
        return result;
    }

}