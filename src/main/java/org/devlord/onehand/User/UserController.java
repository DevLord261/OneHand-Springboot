package org.devlord.onehand.User;

import org.devlord.onehand.Utills.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager=authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> GetAllUsers(){
        List<UserDTO> users = userService.GetAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("create")
    public ResponseEntity<String> CreateUser(@RequestBody CreateUserDTO dto){
        if(userService.ExistsEmail(dto.getEmail())){
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }
        if(userService.ExistsUsername(dto.getUsername())){
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        boolean result = userService.Registeruser(dto);
        if(result){
            return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.CONFLICT);
    }

    @PostMapping("login")
    public ResponseEntity<Object> SignIn(
            @RequestBody Map<String ,String> body
    ){
        try{
            Authentication response = SignIn(body.get("userName"),body.get("password"));
            if(response.isAuthenticated()){
                String token = jwtService.GenerateToken(response);
                return ResponseEntity.ok(Map.of("token",token));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Login error");
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found ");
        }
    }

    @GetMapping("/Getuser")
    public ResponseEntity<?> GetUser(Authentication authentication){
        try{
            if(authentication==null || !authentication.isAuthenticated()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your not authorized");
            }
            String username = authentication.getName();
            UserDTO user = userService.GetUserByUsername(username);
            return ResponseEntity.ok(user);
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong " + e.getMessage());
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized");
        }
    }


    @PutMapping("updateprofile")
    public ResponseEntity<?> UpdateProfile(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody CreateUserDTO dto
    ){
        try{
            UserEntity entity = userService.findById(user.getId());
            entity.setFirstname(dto.getFirstname());
            entity.setLastname(dto.getLastname());
            entity.setUsername(dto.getUsername());
            entity.setEmail(dto.getEmail());
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            userService.Save(entity);
            Authentication response = SignIn(entity.getUsername(),dto.getPassword());
            if(!response.isAuthenticated()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed to update profile");
            }
            String token = jwtService.GenerateToken(response);
            return ResponseEntity.ok(Map.of("token",token));
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong " + e.getMessage());
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized");
        }
    }

    private Authentication SignIn(String username,String password){
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(username,password);
        return this.authenticationManager.authenticate(authenticationRequest);
    }


}
