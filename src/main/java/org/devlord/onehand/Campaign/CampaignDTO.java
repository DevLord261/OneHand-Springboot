package org.devlord.onehand.Campaign;

import lombok.Getter;
import lombok.Setter;
import org.devlord.onehand.User.UserDTO;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
public class CampaignDTO {

    private UUID id;
    private String title;
    private String description;
    private String shortDescription;
    private String location;
    private String heroimage;

    private boolean featured = false;
    private double donationGoal;

    private LocalDate date;

    private double currentDonation = 0.0;

    private String category;

    private int volunteersCount = 0;

    private int donaterCount = 0;

    private boolean isvolunteer = false;
    private UserDTO organizer;

}
