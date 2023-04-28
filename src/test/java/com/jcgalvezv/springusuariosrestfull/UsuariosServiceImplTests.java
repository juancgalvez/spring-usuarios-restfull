package com.jcgalvezv.springusuariosrestfull;

import com.jcgalvezv.springusuariosrestfull.config.RsaJwtKeyProperties;
import com.jcgalvezv.springusuariosrestfull.dao.UsuarioDao;
import com.jcgalvezv.springusuariosrestfull.entities.Telefono;
import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import com.jcgalvezv.springusuariosrestfull.exceptions.UserNotFoundException;
import com.jcgalvezv.springusuariosrestfull.services.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@EnableConfigurationProperties(RsaJwtKeyProperties.class)
@SpringBootTest(properties = {
        "rsa.jwt.keys.private-key=classpath:certs/private.pem",
        "rsa.jwt.keys.public-key=classpath:certs/public.pem"
})
public class UsuariosServiceImplTests {
    @Mock
    private UsuarioDao usuarioDao;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private List<Usuario> usuarios = new ArrayList<Usuario>();

    private UUID uuid1 = UUID.randomUUID();
    private UUID uuid2 = UUID.randomUUID();
    private UUID uuid3 = UUID.randomUUID();
    private UUID uuid4 = UUID.randomUUID();

    private Usuario usuarioAValidar = null;
    private Usuario usuarioARetornar = null;

    @BeforeEach
    public void initialSetup() throws Exception {
        Usuario usuario1 = new Usuario(
                "Juan Carlos Galvez Villegas",
                "jc@aqui.com.cl",
                "P@assw0rd",
                new ArrayList<Telefono>() {{
                    add(new Telefono("12345679", "2", "56"));
                    add(new Telefono("87654321", "55", "56"));
                }});
        usuario1.setId(uuid1);

        Usuario usuario2 = new Usuario(
                "Isabel Allende",
                "isabel.allende@escritoras.cl",
                "P@assw0rd",
                new ArrayList<Telefono>() {{
                    add(new Telefono("12345679", "2", "56"));
                    add(new Telefono("87654321", "55", "56"));
                }});
        usuario2.setId(uuid2);

        usuarios.clear();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        // usuarioARetornar y usuarioAValidar deben tener los mismos datos sin ser el mismo objeto

        usuarioAValidar = new Usuario(
                "Usuario a validar",
                "usuario.validar@pruebas.cl",
                "P@assw0rd",
                new ArrayList<Telefono>() {{
                    add(new Telefono("12345679", "2", "56"));
                    add(new Telefono("87654321", "55", "56"));
                }});
        usuarioAValidar.setId(uuid3);

        usuarioARetornar = new Usuario(
                "Usuario a validar",
                "usuario.validar@pruebas.cl",
                "P@assw0rd",
                new ArrayList<Telefono>() {{
                    add(new Telefono("12345679", "2", "56"));
                    add(new Telefono("87654321", "55", "56"));
                }});
        usuarioARetornar.setId(uuid4);
    }

    private void validacionesGeneralesUsuario(Usuario usuario) {
        //assertEquals(uuid3, usuario.getId());
        assertEquals(usuarioAValidar.getName(), usuario.getName());
        assertEquals(usuarioAValidar.getEmail(), usuario.getEmail());
        List<Telefono> listaTelefonos = usuario.getPhones();
        assertEquals(2, listaTelefonos.size());
        assertEquals(usuarioAValidar.getPhones().get(0).getNumber(), listaTelefonos.get(0).getNumber());
        assertEquals(usuarioAValidar.getPhones().get(0).getCitycode(), listaTelefonos.get(0).getCitycode());
        assertEquals(usuarioAValidar.getPhones().get(0).getCountrycode(), listaTelefonos.get(0).getCountrycode());
        assertEquals(usuarioAValidar.getPhones().get(1).getNumber(), listaTelefonos.get(1).getNumber());
        assertEquals(usuarioAValidar.getPhones().get(1).getCitycode(), listaTelefonos.get(1).getCitycode());
        assertEquals(usuarioAValidar.getPhones().get(1).getCountrycode(), listaTelefonos.get(1).getCountrycode());
    }

    @Test
    void listarTodos_DebeRetornar2Usuarios() {
        when(usuarioDao.findAll()).thenReturn(usuarios);
        List<Usuario> listaUsuarios = usuarioService.listarTodos();
        Assertions.assertNotNull(listaUsuarios);
        assertEquals(listaUsuarios.size(), 2);
    }

    @Test
    void buscarUsuarioPorId_DebeRetornarElUsuarioDePrueba() throws UserNotFoundException {
        when(usuarioDao.findById(any(UUID.class))).thenReturn(Optional.ofNullable(usuarioARetornar));
        Usuario usuarioBuscado = usuarioService.buscarPorId(uuid1);
        validacionesGeneralesUsuario(usuarioBuscado);
    }

    @Test
    void buscarUsuarioPorId_DebeGenerarNotFoundException() {
        when(usuarioDao.findById(any())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            usuarioService.buscarPorId(UUID.randomUUID());
        });
    }

    @Test
    void buscarUsuarioPorEmail_DebeRetornarElUsuarioDePrueba() throws UserNotFoundException {
        when(usuarioDao.findByEmail(anyString())).thenReturn(usuarioARetornar);
        Usuario usuarioBuscado = usuarioService.buscarPorEmail("aqui@alla.cl");
        validacionesGeneralesUsuario(usuarioBuscado);
    }

    @Test
    void buscarUsuarioPorEmail_DebeGenerarUserNotFoundException() {
        when(usuarioDao.findByEmail(anyString())).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            usuarioService.buscarPorEmail("aqui@alla.cl");
        });
    }

    @Test
    void crearUsuario_DebeRetornarElusuarioCreado() {
        when(usuarioDao.save(usuarioAValidar)).thenReturn(usuarioARetornar);

        Usuario usuario = usuarioService.crear(usuarioAValidar);
        validacionesGeneralesUsuario(usuario);
    }

    @Test
    void crearUsuario_DebeGenerarEntityExistsException() {
        when(usuarioDao.save(usuarioAValidar)).thenThrow(EntityExistsException.class);

        Assertions.assertThrows(EntityExistsException.class, () -> {
            usuarioService.crear(usuarioAValidar);
        });
    }

    @Test
    void actualizarUsuario_DebeRetornarElusuarioModificado() throws UserNotFoundException {
        when(usuarioDao.findById(any())).thenReturn(Optional.ofNullable(usuarioARetornar));
        when(usuarioDao.save(any())).thenReturn(usuarioARetornar);
        Usuario usuario = usuarioService.actualizar(usuarioAValidar, usuarioAValidar.getId());
        validacionesGeneralesUsuario(usuario);
    }

    @Test
    void borrarUsuarioPorEmail_NoDebeRetornarNada() throws UserNotFoundException {
        when(usuarioDao.findByEmail(any())).thenReturn(usuarioARetornar);
        usuarioService.borrarPorEmail(usuarioAValidar.getEmail());
    }

    @Test
    void borrarUsuarioPorId_NoDebeRetornarNada() throws UserNotFoundException {
        when(usuarioDao.findById(any())).thenReturn(Optional.ofNullable(usuarioARetornar));
        usuarioService.borrarPorId(usuarioAValidar.getId());
    }

    @Test
    void borrarUsuarioPorEmail_DebeGenerarUserNotFoundException() throws UserNotFoundException {
        when(usuarioDao.findByEmail(anyString())).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            usuarioService.borrarPorEmail("aqui@alla.cl");
        });
    }

    @Test
    void borrarUsuarioPorId_DebeGenerarUserNotFoundException() throws UserNotFoundException {
        when(usuarioDao.findById(any())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            usuarioService.borrarPorId(UUID.randomUUID());
        });
    }
}
