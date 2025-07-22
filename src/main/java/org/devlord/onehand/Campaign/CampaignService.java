package org.devlord.onehand.Campaign;

import org.devlord.onehand.ObjectMappers.CampaignMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    public CampaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    public List<CampaignDTO> GetAll(){
        var campaigns = campaignRepository.findAll();
        List<CampaignDTO> dtos = new ArrayList<>();
        for (var dto:campaigns){
            dtos.add(campaignMapper.toDTO(dto));
        }
        return dtos;
    }

    public PagedModel<CampaignDTO> GetAll(Pageable pageable){
        Page<CampaignEntity> campaigns = campaignRepository.findAll(pageable);
        List<CampaignDTO> content = campaigns.getContent().stream()
                .map(campaignMapper::toDTO)
                .toList();
        Page<CampaignDTO> page = new PageImpl<>(content,pageable,campaigns.getTotalElements());
        return new PagedModel<>(page);
    }


    public void Create(CampaignEntity campaign) {
        campaignRepository.save(campaign);
    }

    public List<CampaignDTO> MyCampaigns(UUID id){
        var campaigns = campaignRepository.findByOrganizerId(id);
        List<CampaignDTO> dtos = new ArrayList<>();
        for (var dto:campaigns){
            dtos.add(campaignMapper.toDTO(dto));
        }
        return dtos;
    }

    public List<CampaignDTO> FeaturedCampaigns(){
        var campaigns = campaignRepository.findByFeatured(true);
        return campaigns.stream().map(campaignMapper::toDTO).toList();
    }

    public CampaignDTO GetByID(String id){
        Optional<CampaignEntity> campaign = campaignRepository.findById(UUID.fromString(id));
        return campaign.map(campaignMapper::toDTO).orElse(null);
    }


}
