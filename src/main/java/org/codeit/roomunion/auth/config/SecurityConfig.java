package org.codeit.roomunion.auth.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.adapter.in.filter.JwtAuthenticationFilter;
import org.codeit.roomunion.auth.adapter.security.JwtAuthenticationEntryPoint;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
        "/v1/users/sign-up",
        "/v1/auth/login", "/v2/auth/login",
        "/v1/auth/refresh",
        "/v1/auth/email/send", "/v1/auth/email/verify", "/v1/auth/email/extend"
    };

    private static final String[] PUBLIC_GET_ENDPOINTS = {
        "/v1/meetings",
        "/v1/meetings/*",
        "/v1/meetings/*/appointments",
        "/v1/meetings/*/members"
    };

    private static final String[] SWAGGER_ENDPOINTS = {"/swagger-ui/**", "/v3/api-docs/**"};

    // ✅ SSE 구독은 @AuthenticationPrincipal 쓰는 이상 authenticated로 두는 게 맞음
    private static final String[] AUTH_REQUIRED_GET_ENDPOINTS = {
        "/v1/meetings/mine",
        "/v1/notification/sse/subscribe"
    };

    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ✅ async 디스패치에서 SecurityContext 유지 안정화
            .securityContext(sc -> sc.requireExplicitSave(false))

            // ✅ 여기서 한 번에 끝내야 함 (anyRequest 이후에 matcher 추가하면 에러)
            .authorizeHttpRequests(SecurityConfig::authorizeHttpRequests)

            .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static void authorizeHttpRequests(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request
    ) {
        request
            // ✅ 반드시 anyRequest()보다 먼저!
            .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()

            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
            .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

            // GET 중 인증 필요한 것들
            .requestMatchers(HttpMethod.GET, AUTH_REQUIRED_GET_ENDPOINTS).authenticated()

            // GET 중 공개
            .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()

            .anyRequest().authenticated();
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
