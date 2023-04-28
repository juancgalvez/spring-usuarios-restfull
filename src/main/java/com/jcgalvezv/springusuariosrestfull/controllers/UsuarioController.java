package com.jcgalvezv.springusuariosrestfull.controllers;

import com.jcgalvezv.springusuariosrestfull.exceptions.UserNotFoundException;
import com.jcgalvezv.springusuariosrestfull.entities.Usuario;
import com.jcgalvezv.springusuariosrestfull.services.UsuarioServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/api/usuarios")
@Tag(name = "Students", description = "Controlador REST para la actualizacion de Usuarios")
public class UsuarioController {
    UsuarioServiceImpl usuarioServiceImpl;

    public UsuarioController(UsuarioServiceImpl usuarioServiceImpl) {
        this.usuarioServiceImpl = usuarioServiceImpl;
    }

    //********************************
    // Manejadores de excepciones.
    //********************************

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Object> handleValidationExceptions(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("mensaje", "el cuerpo del requerimiento debe ser en formato JSON");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("mensaje", "Hay errores en los datos. corrijalos e intente nuevamente");
        response.put("errores", errores);
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleValidationExceptions(Exception ex) {
        System.out.println("Exception " + ex.getClass().getName());
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("mensaje", "Excepcion: message : " + ex.getMessage() + " cause : " + ex.getCause());
        return response;
    }

    /*
    Referencia: https://www.baeldung.com/spring-dataIntegrityviolationexception

    "There is a single JPA exception that may trigger a DataIntegrityViolationException to be thrown – the javax.persistence.EntityExistsException."
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, Object> handleValidationExceptions(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("mensaje", "El correo ya esta registrado");
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, Object> handleValidationExceptions(UserNotFoundException ex) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("mensaje", ex.getMessage());
        return response;
    }

    //********************************
    // Endpoints
    //********************************

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Operation(summary = "Crea un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public ResponseEntity<?> crearUsuario(
            @Parameter(description = "JSON Con los datos del usuario a crear.")
            @Valid @RequestBody Usuario usuario) {
        Usuario usuarioCreado = usuarioServiceImpl.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    //******************************************************************

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional(Transactional.TxType.NEVER)
    @Operation(summary = "Obtiene una lista con todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Usuarios (Puede estar vacia)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public List<Usuario> listaUsuarios() {
        return usuarioServiceImpl.listarTodos();
    }

    //******************************************************************

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional(Transactional.TxType.NEVER)
    @Operation(summary = "Busca al usuario con el Id (UUID) indicado en el URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna una estructura JSON con los datos del Usuario.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public ResponseEntity<?> buscarUsuarioPorId(
            @Parameter(description = "Id (UUID) del usuario a consultar.")
            @PathVariable("id") UUID id) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioServiceImpl.buscarPorId(id));
    }

    //******************************************************************

    @GetMapping("/email/{email}")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional(Transactional.TxType.NEVER)
    @Operation(summary = "Busca al usuario con el Email indicado en el URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna una estructura JSON con los datos del Usuario.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public ResponseEntity<?> buscarUsuarioPorEmail(
            @Parameter(description = "EMail del usuario a consultar")
            @PathVariable("email") String email) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioServiceImpl.buscarPorEmail(email));
    }

    //******************************************************************

    @PutMapping(path="/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(summary = "Actualiza los datos del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna una estructura JSON con los datos del Usuario.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public ResponseEntity<?> actualizarUsuario(
            @Parameter(description = "Formato JSON con los datos del usuario a actualizar")
            @Valid @RequestBody Usuario usuario,
            @Parameter(description = "Id (UUID) del usuario a actualizar")
            @PathVariable UUID id) throws UserNotFoundException {
        return ResponseEntity.ok().body(usuarioServiceImpl.actualizar(usuario, id));
    }

    //******************************************************************

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Elimina al usuario por su Id (UUID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No retorna nada",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public void borrarUsuario(
            @Parameter(description = "Id (UUID) del usuario a eliminar")
            @PathVariable UUID id) throws UserNotFoundException {
        usuarioServiceImpl.borrarPorId(id);
    }

    //******************************************************************

    @DeleteMapping("/email/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Elimina al usuario por su Id (UUID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No retorna nada",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Usuario.class))
                    }
            )
    })
    public void borrarUsuarioPorEmail(
            @Parameter(description = "Email del usuario a eliminar")
            @PathVariable String email) throws UserNotFoundException {
        usuarioServiceImpl.borrarPorEmail(email);
    }

}
