package org.devlord.onehand.User;



import org.devlord.onehand.ObjectMappers.UserMapper;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public List<UserDTO> GetAll(){
        List<UserEntity> users =userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (UserEntity u: users){
            dtos.add(userMapper.toDTO(u));
        }
        return dtos;
    }
    public boolean Registeruser(CreateUserDTO dto){
        UserEntity user= userMapper.toUser(dto);
        userRepository.save(user);
        return true;
    }

    public boolean ExistsEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public boolean ExistsUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public UserDTO GetUserByUsername(String username){
        return userMapper.toDTO(userRepository.findByUsername(username));
    }

    public UserEntity findById(UUID id){
        return userRepository.findByid(id);
    }

    public void Save(UserEntity entity) {
        userRepository.save(entity);

    }


}
