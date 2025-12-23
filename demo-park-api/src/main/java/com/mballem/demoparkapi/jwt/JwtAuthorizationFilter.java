package com.mballem.demoparkapi.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter  extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetaisService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token =request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if(token ==null || !token.startsWith(JwtUtils.JWT_BEARER)){
            logger.info("JWT Token está nulo, vazio ou não iniciado com 'Bearer'");
            filterChain.doFilter(request,response);
            return;//para sair daqui
        }
        if(!JwtUtils.isTokenValid(token)){
            logger.warn("Jwt Token está inválido ou expirado.");
            filterChain.doFilter(request,response);
            return;
        }
        //Se chegou aqui é porque existe e é válido
        String username = JwtUtils.getUserNameFromToken(token);
        toAuthentication(request, username);
        filterChain.doFilter(request,response);

    }

    private void toAuthentication (HttpServletRequest request, String username){
        UserDetails userDetails =detailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken= UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
