package com.babydev.app.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "job")
@Getter
@Setter

public class Notification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "seen")
    private Boolean seen;
    
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;
}
