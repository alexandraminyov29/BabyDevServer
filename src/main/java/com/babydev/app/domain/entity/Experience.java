package com.babydev.app.domain.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "experience")
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Experience {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @Column(name = "title")
    private String title;
    
    @Column(name = "companyName")
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "dateFrom")
    private LocalDate dateFrom;

    @Column(name = "dateTo")
    private LocalDate dateTo;
    
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;
    
    Experience(){};
}
