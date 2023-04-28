package com.jcgalvezv.springusuariosrestfull.dao;

import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioDao extends JpaRepository<Usuario, UUID> {
    public List<Usuario> findAll();
    public Optional<Usuario> findById(UUID id);
    public Usuario findByEmail(String email);
    public Usuario save(Usuario usuario);
    public void delete(Usuario usuario);
}
