package com.uhu.saluhud.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author juald
 */
@Configuration
@EnableWebSecurity
public class SaluhudWebSecurityConfiguration
{

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        //http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http
                .authorizeHttpRequests(auth
                        -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Permite recursos estáticos
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/security/login", "/security/login**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login
                        -> login
                        .loginPage("/security/login") // Página de login
                        .defaultSuccessUrl("/welcome", true) // Redirigir después del login
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
                .sessionManagement(session
                        -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Asegura que se mantiene la sesión
                )
                .userDetailsService(userDetailsService); // Usa el servicio para cargar usuarios

        return http.build();
    }

    @Bean
    public static SecurityContextHolderStrategy securityContextHolderStrategy()
    {
        return SecurityContextHolder.getContextHolderStrategy();
    }
}
