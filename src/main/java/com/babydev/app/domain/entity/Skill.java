package com.babydev.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skill")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Skill {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "skill_experience")
    @Enumerated(EnumType.ORDINAL)
    private ExperienceRating skillExperience;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "job_id")
    private Job job;

}
