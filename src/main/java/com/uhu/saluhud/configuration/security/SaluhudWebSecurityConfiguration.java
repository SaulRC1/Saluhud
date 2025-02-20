package com.uhu.saluhud.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                .csrf(csrf -> csrf.disable()) // para evitar problemas con formularios
                .authorizeHttpRequests(auth
                        -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Permite recursos estáticos
                        .requestMatchers("/security/login", "/security/login**").permitAll()
                        .requestMatchers("/recipes/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login
                        -> login
                        .loginPage("/security/login") // Página de login
                        .failureUrl("/security/login?error=true") // Redirigir con parámetro error en caso de error de autenticación
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
                .userDetailsService(userDetailsService); // Usa el servicio para cargar usuarios

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
