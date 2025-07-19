package org.devlord.onehand.Campaign;


import org.devlord.onehand.User.UserEntity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns/")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignDTO>> GetAllCampaigns(){
        var campaigns = campaignService.GetAll();
        return ResponseEntity.ok(campaigns);
    }
    @GetMapping("pages")
    public ResponseEntity<PagedModel<CampaignDTO>> GetAllCampaigns(
            @RequestParam int pagenumber,
            @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(pagenumber,size);
        var campaigns = campaignService.GetAll(pageable);
        return ResponseEntity.ok(campaigns);
    }

    @PostMapping("create")
    public ResponseEntity<String> CreateCampaign(
            @ModelAttribute CampaignEntity campaign,
            @AuthenticationPrincipal UserEntity user
    ){
        try{
            campaign.setOrganizer(user);
            campaignService.Create(campaign);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created Succesfully");
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Need to login");
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized");
        }
    }

    @GetMapping("mycampaigns")
    public ResponseEntity<?> MyCampaigns(
            @AuthenticationPrincipal UserEntity user
    ){
        try{
            return ResponseEntity.ok(campaignService.MyCampaigns(user.getId()));
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized");
        }
    }

    @GetMapping("featured")
    public ResponseEntity<List<CampaignDTO>> FeaturedCampaigns(){
        return ResponseEntity.ok(campaignService.FeaturedCampaigns());
    }


}
