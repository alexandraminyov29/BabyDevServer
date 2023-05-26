package com.babydev.app.domain.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "education")
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Education {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;
    
    @Column(name = "prio")
    private short priority;

    @Column(name = "institution")
    private String institution;

    @Column(name = "subject")
    private String subject;

    @Column(name = "dateFrom")
    private LocalDate dateFrom;

    @Column(name = "dateTo")
    private LocalDate dateTo;

    @Column(name = "degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;
    
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;
    
    Education(){}
}
