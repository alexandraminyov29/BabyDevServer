package com.babydev.app.service.impl;

import com.babydev.app.domain.entity.Company;
import com.babydev.app.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    @Autowired
    private final CompanyRepository companyRepository;

    public List<Company> getCompanies() {
        return  companyRepository.findAll();
    }

    public Company getCompany(Long id) {
        return companyRepository.findById(id).get();
    }

    public Company addCompany(Company company) {
       return  companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
