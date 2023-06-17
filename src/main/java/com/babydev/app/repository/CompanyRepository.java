package com.babydev.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.babydev.app.domain.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findCompanyByCompanyId(Long companyId);

    Optional<Company> findByName(String name);
    
    @Modifying
    @Query("DELETE Company c WHERE c.id = ?1")
    void deleteById(Long id);
}
