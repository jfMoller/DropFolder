package me.code.dropfolder.security;

import me.code.dropfolder.repository.UserRepository;
import me.code.dropfolder.service.user.UserRegistrationValidator;
import me.code.dropfolder.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, UserDetailsService userDetailsService) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new JwtValidationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/user/sign-up", "/api/login").permitAll()
                        .anyRequest().authenticated());
        return security.build();
    }

    @Bean
    public AuthenticationProvider authProvider(UserDetailsService userService, PasswordEncoder encoder) {
        var dao = new DaoAuthenticationProvider();

        dao.setUserDetailsService(userService);
        dao.setPasswordEncoder(encoder);

        return dao;
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserRegistrationValidator userRegistrationValidator,
            JwtTokenUtil jwtTokenUtil) {
        return new UserService(userRepository, passwordEncoder, userRegistrationValidator, jwtTokenUtil);
    }

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
