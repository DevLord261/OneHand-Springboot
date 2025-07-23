package org.devlord.onehand.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devlord.onehand.ObjectMappers.CampaignMapper;
import org.devlord.onehand.ObjectMappers.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper(ObjectMapper objectMapper) {
        return new UserMapper(objectMapper);
    }

    @Bean
    public CampaignMapper campaignMapper(ObjectMapper objectMapper,UserMapper userMapper) {
        return new CampaignMapper(objectMapper,userMapper);
    }
}
