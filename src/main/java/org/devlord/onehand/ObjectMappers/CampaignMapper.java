package org.devlord.onehand.ObjectMappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devlord.onehand.Campaign.CampaignDTO;
import org.devlord.onehand.Campaign.CampaignEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper {

    @Autowired
    private final ObjectMapper objectMapper;

    public CampaignMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CampaignEntity toEntity(CampaignDTO dto){
        return objectMapper.convertValue(dto,CampaignEntity.class);
    }
    public CampaignDTO toDTO(CampaignEntity entity){
        return objectMapper.convertValue(entity,CampaignDTO.class);
    }
}
