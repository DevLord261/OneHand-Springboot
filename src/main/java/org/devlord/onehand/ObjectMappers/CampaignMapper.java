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
    @Autowired
    private final UserMapper userMapper;

    public CampaignMapper(ObjectMapper objectMapper, UserMapper userMapper) {
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }

    public CampaignDTO toDTO(CampaignEntity entity){
        CampaignDTO dto = objectMapper.convertValue(entity,CampaignDTO.class);
        dto.setOrganizer(userMapper.toDTO(entity.getOrganizer()));
        return dto;
    }
}
