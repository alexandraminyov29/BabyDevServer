package com.babydev.app.repository;

import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    Optional<Job> findJobByJobId(Long jobId);

    @Query(value = "SELECT NEW com.babydev.app.domain.dto.JobPageDTO(job.jobId, job.title, job.description, job.postDate, " +
            "job.experienceRequired, " +
            "company.companyId, company.name, " +
            "company.image) " +
            "FROM Job job " +
            "LEFT JOIN job.company company " +
            "WHERE job.jobId =:jobId", nativeQuery = false)
    JobPageDTO findJobDetails(@Param("jobId") long jobId);
}
