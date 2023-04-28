package com.jcgalvezv.springusuariosrestfull.services;

import com.jcgalvezv.springusuariosrestfull.exceptions.UserNotFoundException;
import com.jcgalvezv.springusuariosrestfull.dao.UsuarioDao;
import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    UsuarioDao usuarioDAO;

    public UsuarioServiceImpl(UsuarioDao usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioDAO.findAll();
    }

    @Override
    public Usuario crear(Usuario usuario) {
        // Evitar que envien datos que no deben. Inicializarlos con nulos.
        usuario.setId(null);
        usuario.setLastLogin(null);
        usuario.setModified(null);
        usuario.setCreated(null);
        usuario.setJwt(null);
        usuario.getPhones().forEach(telefono -> telefono.setId(null));

        return usuarioDAO.save(usuario);
    }
    @Override
    public Usuario actualizar(Usuario usuario, UUID id) throws UserNotFoundException {
        Usuario usuarioBuscado = usuarioDAO.findById(id).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        usuarioBuscado.setName(usuario.getName());
        usuarioBuscado.setEmail(usuario.getEmail());
        usuarioBuscado.setPassword(usuario.getPassword());
        usuarioBuscado.setIsactive(usuario.getIsactive());
        // Evitar que envien datos que no deben. Inicializarlos con nulos.
        usuario.getPhones().forEach(telefono -> telefono.setId(null));
        usuarioBuscado.setPhones(usuario.getPhones());

        return usuarioDAO.save(usuarioBuscado);
    }
    @Override
    public Usuario actualizarJwt(String email, String jwt) throws UserNotFoundException {
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        usuario.setJwt(jwt);

        return usuarioDAO.save(usuario);
    }
    @Override
    public Usuario buscarPorId(UUID id) throws UserNotFoundException {
        Usuario usuario = usuarioDAO.findById(id).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        return usuario;
    }

    @Override
    public Usuario buscarPorEmail(String email) throws UserNotFoundException {
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        return usuario;
    }

    @Override
    public void borrarPorId(UUID id) throws UserNotFoundException {
        Usuario usuario = usuarioDAO.findById(id).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        usuarioDAO.delete(usuario);
    }

    @Override
    public void borrarPorEmail(String email) throws UserNotFoundException {
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new UserNotFoundException();
        }

        usuarioDAO.delete(usuario);
    }
}
