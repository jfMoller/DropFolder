package me.code.dropfolder.security;

import me.code.dropfolder.repository.UserRepository;
import me.code.dropfolder.service.user.UserRegistrationValidator;
import me.code.dropfolder.service.user.UserService;
import me.code.dropfolder.util.JpQueryUtil;
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

/**
 * Configuration class for defining security-related beans and settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the system.
     *
     * @param security           The HttpSecurity object to configure.
     * @param userDetailsService The UserDetailsService implementation for loading user details.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, UserDetailsService userDetailsService) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new JwtValidationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/user/register", "/api/login").permitAll()
                        .anyRequest().authenticated());
        return security.build();
    }

    /**
     * Configures the authentication provider for the system.
     *
     * @param userService The UserDetailsService implementation for loading user details.
     * @param encoder     The PasswordEncoder implementation for encoding and verifying passwords.
     * @return The configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authProvider(UserDetailsService userService, PasswordEncoder encoder) {
        var dao = new DaoAuthenticationProvider();

        dao.setUserDetailsService(userService);
        dao.setPasswordEncoder(encoder);

        return dao;
    }


    /**
     * Creates the UserDetailsService bean for loading user details.
     *
     * @param userRepository            The repository for user data access.
     * @param passwordEncoder           The PasswordEncoder implementation for encoding and verifying passwords.
     * @param userRegistrationValidator The validator for user registration.
     * @return The configured UserDetailsService bean.
     */
    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserRegistrationValidator userRegistrationValidator,
            JpQueryUtil query) {
        return new UserService(userRepository, passwordEncoder, userRegistrationValidator, query);
    }

    /**
     * Creates the PasswordEncoder bean for encoding and verifying passwords.
     *
     * @return The configured PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
