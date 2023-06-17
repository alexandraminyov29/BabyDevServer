package com.babydev.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.entity.Company;
import com.babydev.app.repository.CompanyRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

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
    
    public Company getCompanyByName(String name) {
    	return companyRepository.findByName(name)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Company save(Company company) {
       return  companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
    
    public void deleteCompany(Company company) {
        companyRepository.delete(company);
    }
}
