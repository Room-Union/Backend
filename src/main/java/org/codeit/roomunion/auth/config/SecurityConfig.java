package org.codeit.roomunion.auth.config;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.adapter.in.filter.JwtAuthenticationFilter;
import org.codeit.roomunion.auth.adapter.in.filter.LoginFilter;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Security Config 설정
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {"/v1/users/sign-up", "/v1/auth/login", "/v1/auth/email/send", "/v1/auth/email/verify", "/v1/auth/email/extend"};
    private static final String[] SWAGGER_ENDPOINTS = {"/swagger-ui/**", "/v3/api-docs/**"};

    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(jwtUtil, authenticationManager(authenticationConfiguration));
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(SecurityConfig::authorizeHttpRequests)
            .addFilterAfter(loginFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static void authorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        request
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
            .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
            .anyRequest()
            .authenticated();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}