package org.devlord.onehand.Config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.devlord.onehand.Campaign.CampaignRepository;
import org.devlord.onehand.Campaign.CampaignService;
import org.devlord.onehand.ObjectMappers.CampaignMapper;
import org.devlord.onehand.ObjectMappers.UserMapper;
import org.devlord.onehand.Services.StorageSpaceService;
import org.devlord.onehand.User.UserRepository;
import org.devlord.onehand.User.UserService;
import org.devlord.onehand.Utills.JwtAuthenticationFilter;
import org.devlord.onehand.Utills.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors ->cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/api/campaigns/create","api/users/updateprofile").authenticated()
                        .requestMatchers("/api/users/login","/api/users/create","/api/campaigns/featured"
                        ,"/api/campaigns/pages").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:3000/**"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH","CREATE"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        return new JwtAuthenticationFilter(jwtService, userService);
    }
    @Bean
    public JwtService jwtService(){
        return new JwtService();
    }

    @Bean
    public UserService userService(UserRepository userRepository,UserMapper userMapper){
        return new UserService(userRepository,userMapper);
    }

    @Bean
    CampaignService campaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper, StorageSpaceService spaceConfig){
        return new CampaignService(campaignRepository,campaignMapper,spaceConfig);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userService,
                                                       PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    @Primary
    ObjectMapper objectMapper(){
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .modules(new JavaTimeModule())
                .build();
    }

}
