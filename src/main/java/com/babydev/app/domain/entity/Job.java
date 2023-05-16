package com.babydev.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job")
@Getter
@Setter
public class Job {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private Location location;

    @Column(name = "type")
    private JobType type;

    @Column(name = "promoted_until")
    private LocalDateTime promotedUntil;

    @Column(name = "post_date")
    private LocalDate postDate;

    @Column(name = "experience_required")
    private String experienceRequired;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "applicants", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> applicants;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "author_id")
    private User author;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
    private List<Skill> requiredSkill;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "company_id")
    private Company company;
}
