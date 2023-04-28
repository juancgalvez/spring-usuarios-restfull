package com.jcgalvezv.springusuariosrestfull.services;

import com.jcgalvezv.springusuariosrestfull.dao.UsuarioDao;
import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetails implements UserDetailsService {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(UserDetails.class);

    UsuarioDao usuarioDao;

    public UserDetails(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByEmail(email);

        if (usuario == null ) {
            logger.error("Error en login. No existe usuario " + email + " en el sistema");
            throw new UsernameNotFoundException("Error en login. No existe el usuario con email " + email);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        String password = new BCryptPasswordEncoder().encode(usuario.getPassword());
        return new User(email, password, usuario.getIsactive(), true, true, true, authorities);
    }
}
