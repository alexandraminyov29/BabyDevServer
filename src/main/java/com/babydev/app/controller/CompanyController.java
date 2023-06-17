package com.babydev.app.controller;

import com.babydev.app.domain.entity.Company;
import com.babydev.app.service.impl.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/add")
    @Deprecated
    public ResponseEntity<Company> addCompany(@RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.save(company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body("Company with id " + id + " was deleted.");
    }
}
