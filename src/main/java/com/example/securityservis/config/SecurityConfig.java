package com.example.securityservis.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {


    private final CustomUserDetailsService userDetailsService;

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((req -> req
                        .requestMatchers("/api/users/login", "/", "/pic/**", "/api/users/forgot-password", "/api/users/register").permitAll()
                        .anyRequest().authenticated()
                ))
                .formLogin((form -> form
                        .loginPage("/api/users/login")
                        .defaultSuccessUrl("/", true) // URL после успешного входа

                        .permitAll()
                ))

                .logout((log -> log
                        .logoutUrl("/api/users/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                ))
                .exceptionHandling((ex) -> ex
                        .accessDeniedPage("/access-denied") // Перенаправление на страницу для заблокированных пользователей

                )
                .csrf().disable(); // Отключение CSRF, если это необходимо
        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


