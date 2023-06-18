package com.babydev.app.repository;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job,Long> {
    Optional<Job> findJobByJobId(Long jobId);

    Optional<Job> findByTitle(String title);

    Optional<Job> findByDescription(String description);

    Optional<Job> findByRequiredSkill(String requiredSkill);



    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobPageDTO(job.jobId, job.title, job.description, " +
            "job.location, job.type, job.postDate, job.experienceRequired, " +
            "CASE WHEN (SELECT count(*) from job.applicants app WHERE app.id =:userId) > 0 THEN TRUE ELSE FALSE END, " +
            "company.companyId, company.webPage, company.name, " +
            "company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "WHERE job.jobId =:jobId", nativeQuery = false)
    JobPageDTO findJobDetails(@Param("jobId") long jobId, @Param("userId") long userId);

    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobListViewTypeDTO(job.id, job.title, job.location, job.postDate, job.type, " +
            "CASE WHEN uf.userId =:userId AND :userId != '' THEN TRUE ELSE FALSE END, job.experienceRequired, company.id, company.name, company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "LEFT JOIN job.usersFavorites uf " +
            "WHERE (job.title LIKE %:keyword%)")
    List<JobListViewTypeDTO> searchJobs(@Param("keyword") String keyword, @Param("userId") String userId);

    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobListViewTypeDTO(job.id, job.title, job.location, job.postDate, job.type, " +
            "CASE WHEN uf.userId =:userId AND :userId != '' THEN TRUE ELSE FALSE END, job.experienceRequired, company.id, company.name, company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "LEFT JOIN job.usersFavorites uf")
    List<JobListViewTypeDTO> findAll(@Param("userId") String userId);

    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobListViewTypeDTO(job.id, job.title, job.location, job.postDate, job.type, " +
            "false, job.experienceRequired, company.id, company.name, company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "WHERE company.id = :companyId")
    List<JobListViewTypeDTO> findAllRecruiterJobs(@Param("companyId") String companyId);

}
