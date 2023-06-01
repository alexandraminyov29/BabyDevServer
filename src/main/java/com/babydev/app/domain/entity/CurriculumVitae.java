package com.babydev.app.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cv")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumVitae {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String fileName;
}
