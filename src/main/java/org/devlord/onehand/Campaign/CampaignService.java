package org.devlord.onehand.Campaign;

import org.devlord.onehand.ObjectMappers.CampaignMapper;
import org.devlord.onehand.Services.StorageSpaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final StorageSpaceService spaceService;

    public CampaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper, StorageSpaceService spaceService) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.spaceService = spaceService;
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


    public void Create(CampaignEntity campaign, MultipartFile heroimage) {
        CampaignEntity camp = campaignRepository.save(campaign);
        try{
            String id = camp.getOrganizer().getId()+"/"+camp.getId().toString();
            String heroimgurl = spaceService.uploadFile(id,heroimage);
            camp.setHeroimage(heroimgurl);
            campaignRepository.save(camp);
        }catch (IOException e) {
            throw new RuntimeException("Failed to read image bytes "+e.getMessage());
        }
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


    public String upload(String userid,MultipartFile file){
        try{
            return spaceService.uploadFile(userid,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
