package org.devlord.onehand.User;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class CreateUserDTO {
    private String Firstname,Lastname,username,email,password;
}
