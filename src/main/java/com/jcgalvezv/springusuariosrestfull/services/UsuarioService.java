package com.jcgalvezv.springusuariosrestfull.services;

import com.jcgalvezv.springusuariosrestfull.exceptions.UserNotFoundException;
import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsuarioService {
    public List<Usuario> listarTodos();
    public Usuario crear(Usuario usuario);
    public Usuario actualizar(Usuario usuario, UUID id) throws UserNotFoundException;
    public Usuario actualizarJwt(String email, String jwt) throws UserNotFoundException;
    public Usuario buscarPorId(UUID id) throws UserNotFoundException;
    public Usuario buscarPorEmail(String email) throws UserNotFoundException;
    public void borrarPorId(UUID id) throws UserNotFoundException;
    public void borrarPorEmail(String email) throws UserNotFoundException;
}
