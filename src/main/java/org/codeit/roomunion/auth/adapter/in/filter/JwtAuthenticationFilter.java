package org.codeit.roomunion.auth.adapter.in.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.application.service.JwtService;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.auth.domain.model.LoginUserDetails;
import org.codeit.roomunion.auth.domain.model.UnknownUserDetails;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (isNotBearerToken(authHeader)) {
            CustomUserDetails unknownUserDetails = UnknownUserDetails.getInstance();
            UsernamePasswordAuthenticationToken unknownToken = new UsernamePasswordAuthenticationToken(unknownUserDetails, null);
            SecurityContextHolder.getContext().setAuthentication(unknownToken);
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String jwtToken = authHeader.substring(7);
            String email;

            try {
                email = extractEmailFromJwt(jwtToken);
            } catch (ExpiredJwtException e) {
                setErrorResponse(response, AuthErrorCode.EXPIRED_JWT);
                return;
            } catch (MalformedJwtException e) {
                setErrorResponse(response, AuthErrorCode.MALFORMED_JWT);
                return;
            } catch (SignatureException e) {
                setErrorResponse(response, AuthErrorCode.INVALID_JWT_SIGNATURE);
                return;
            } catch (Exception e) {
                setErrorResponse(response, AuthErrorCode.INVALID_JWT);
                return;
            }

            LoginUserDetails userDetails = (LoginUserDetails) userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenInvalid(jwtToken, userDetails)) {
                setErrorResponse(response, AuthErrorCode.INVALID_JWT);
                return;
            }

            UsernamePasswordAuthenticationToken authToken = createUserPasswordAuthenticationToken(userDetails);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String extractEmailFromJwt(String jwtToken) {
        return jwtService.extractUsername(jwtToken);
    }

    private UsernamePasswordAuthenticationToken createUserPasswordAuthenticationToken(CustomUserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
    }

    private boolean isNotBearerToken(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private void setErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(createErrorResponse(errorCode));
        response.setStatus(errorCode.getStatusValue());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(errorResponse);
    }

    private Map<String, Object> createErrorResponse(BaseErrorCode errorCode) {
        return Map.of(
            "code", errorCode.getCode(),
            "message", errorCode.getMessage()
        );
    }
}