package com.mballem.demoparkapi.jwt;

import com.mballem.demoparkapi.entity.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


public class JwtUserDetails extends User {

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario=usuario;
    }

    public Usuario usuario;

    public Long getId(){
        return this.usuario.getId();
    }

}
