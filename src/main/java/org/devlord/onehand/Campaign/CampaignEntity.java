package org.devlord.onehand.Campaign;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.devlord.onehand.Entities.BaseEntity;
import org.devlord.onehand.User.UserEntity;


import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "campaigns")
public class CampaignEntity extends BaseEntity {


    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String shortDescription;
    private String location;
    private String heroimage;

    private boolean featured = false;
    private double donationGoal;

    private LocalDate date;

    @Column(nullable = false)
    private double currentDonation = 0.0;

    private String category;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int volunteersCount = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int donaterCount = 0;

    @Column(nullable = false)
    private boolean isvolunteer = false;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity organizer;

    @OneToMany
    @JoinColumn(name = "volunteers")
    private List<UserEntity> volunteers;
}
