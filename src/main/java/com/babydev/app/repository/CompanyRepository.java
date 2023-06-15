package com.babydev.app.repository;

import com.babydev.app.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findCompanyByCompanyId(Long companyId);

    Optional<Company> findByName(String name);
}
