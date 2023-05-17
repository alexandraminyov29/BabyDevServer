package com.babydev.app.repository;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    Optional<Job> findJobByJobId(Long jobId);

    Optional<Job> findByTitle(String title);

    Optional<Job> findByDescription(String description);

    Optional<Job> findByRequiredSkill(String requiredSkill);



    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobPageDTO(job.jobId, job.title, job.description, job.postDate, " +
            "job.experienceRequired, " +
            "CASE WHEN (SELECT count(*) from job.applicants app WHERE app.id =:userId) > 0 THEN TRUE ELSE FALSE END, " +
            "company.companyId, company.name, " +
            "company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "WHERE job.jobId =:jobId", nativeQuery = false)
    JobPageDTO findJobDetails(@Param("jobId") long jobId, @Param("userId") long userId);

    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobListViewTypeDTO(job.id, job.title, job.location, job.type,  " +
            "job.experienceRequired, company.id, company.name, company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "WHERE (job.title LIKE %:keyword% OR job.location LIKE %:keyword% )")
    List<JobListViewTypeDTO> searchJobs(@Param("keyword") String keyword);

}
