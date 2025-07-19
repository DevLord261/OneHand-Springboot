package org.devlord.onehand.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>  {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    UserEntity findByid(UUID id);
    UserEntity findByUsername(String username);
}
