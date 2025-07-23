package org.devlord.onehand.Config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.devlord.onehand.Campaign.CampaignRepository;
import org.devlord.onehand.Campaign.CampaignService;
import org.devlord.onehand.ObjectMappers.CampaignMapper;
import org.devlord.onehand.ObjectMappers.UserMapper;
import org.devlord.onehand.Services.StorageSpaceService;
import org.devlord.onehand.User.UserRepository;
import org.devlord.onehand.User.UserService;
import org.devlord.onehand.Utills.JwtAuthenticationFilter;
import org.devlord.onehand.Utills.JwtService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.io.IOException;

import static org.mockito.Mockito.mock;


@TestConfiguration
@EnableMethodSecurity
public class SecurityConfigTest {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/api/campaigns/create","api/users/updateprofile").authenticated()
                        .anyRequest()
                        .permitAll())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
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
    public UserService userService(UserRepository userRepository, UserMapper userMapper){
        return new UserService(userRepository,userMapper);
    }

    @Bean
    CampaignService campaignService(CampaignRepository campaignRepository, CampaignMapper campaignMapper, StorageSpaceService spaceService){
        return new CampaignService(campaignRepository,campaignMapper,spaceService);
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
        return mock(PasswordEncoder.class);
    }

    @Bean ObjectMapper objectMapper(){
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .modules(new JavaTimeModule())
                .build();
    }

    @Bean
    public DataSource dataSource()  {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/onehand");
        config.setUsername("DevLord");
        config.setPassword("Alshb7");
        config.setMaxLifetime(1800000);
        config.setIdleTimeout(600000);
        config.setConnectionTimeout(30000);
        config.setMaximumPoolSize(1);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("sslmode","require");
        return new HikariDataSource(config);
    }

}
