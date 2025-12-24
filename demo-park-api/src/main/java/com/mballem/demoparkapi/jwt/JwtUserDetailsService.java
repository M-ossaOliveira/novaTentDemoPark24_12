package com.mballem.demoparkapi.jwt;

import com.mballem.demoparkapi.entity.Usuario;
import com.mballem.demoparkapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor @Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;
    @Override
    public JwtUserDetails loadUserByUsername (String username) throws UsernameNotFoundException{
        Usuario usuario=usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthentication (String username){
        Usuario.Role role = usuarioService.buscarRolePorUsername(username);
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }

}
