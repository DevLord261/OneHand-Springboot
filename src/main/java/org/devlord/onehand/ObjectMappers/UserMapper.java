package org.devlord.onehand.ObjectMappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devlord.onehand.User.CreateUserDTO;
import org.devlord.onehand.User.UserEntity;
import org.devlord.onehand.User.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private final ObjectMapper objectMapper;

    public UserMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public UserEntity toUser(UserDTO dto){
        return objectMapper.convertValue(dto, UserEntity.class);
    }
    public UserEntity toUser(CreateUserDTO dto){
        return objectMapper.convertValue(dto, UserEntity.class);
    }

    public UserDTO toDTO(UserEntity entity){
        return objectMapper.convertValue(entity, UserDTO.class);
    }

    public UserDTO toDTO(CreateUserDTO entity){
        return objectMapper.convertValue(entity, UserDTO.class);
    }
}
