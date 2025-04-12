package com.uhu.saluhud.configuration.security;

import com.uhu.saluhud.mobileapp.filter.MobileAppAPIKeyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author juald
 */
@Configuration
@EnableWebSecurity
public class SaluhudWebSecurityConfiguration
{

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SaluhudWebSecurityConfiguration(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder)
    {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        //http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/saluhud-mobile-app/**")) //CSRF is not needed for mobile app interaction 
                .authorizeHttpRequests(auth
                        -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Allows static resources
                        .requestMatchers("/security/login", "/security/login**").permitAll()
                        .requestMatchers("/recipes/**").authenticated()
                        .requestMatchers("/allergenic/**").authenticated()
                        .requestMatchers("/ingredients/**").authenticated()
                        .requestMatchers("/elaborationSteps/**").authenticated()
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/search/**").authenticated()
                        .requestMatchers("/saluhud-mobile-app/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new MobileAppAPIKeyFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(login
                        -> login
                        .loginPage("/security/login") // Login page
                        .failureUrl("/security/login?error=true") // Redirect with error parameter in case of authentication error
                        .defaultSuccessUrl("/welcome", true) // Redirect after login
                        .permitAll()
                )
                .logout(logout
                        -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/welcome?logoutSuccess=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .userDetailsService(userDetailsService); //Uses the service to load users

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(authProvider())  // Usa el proveedor de autenticación
            .build();
    }
}
