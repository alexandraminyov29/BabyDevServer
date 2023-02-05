package com.babydev.app.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "promoted_until")
    private LocalDateTime promotedUntil;

    @Column(name = "post_date")
    private LocalDateTime postDate;

    @Column(name = "experience_required")
    private String experienceRequired;

    @ManyToMany
    @JoinTable(name = "applicants", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> applicants;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "author_id")
    private User author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "job")
    private List<Skill> requiredSkill;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "company_id")
    private Company company;
}
