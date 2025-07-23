package org.devlord.onehand.User;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String Firstname,Lastname,username,email;
    private String Avatar;
    private Boolean isVerified;
}
