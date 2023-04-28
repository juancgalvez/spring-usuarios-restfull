package com.jcgalvezv.springusuariosrestfull.controllers;

import com.jcgalvezv.springusuariosrestfull.exceptions.UserNotFoundException;
import com.jcgalvezv.springusuariosrestfull.services.GeneracionTokenService;
import com.jcgalvezv.springusuariosrestfull.services.UsuarioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@RestController
@ApiIgnore
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final GeneracionTokenService generacionTokenService;
    private UsuarioServiceImpl usuarioService;

    public AuthController(GeneracionTokenService generacionTokenService,
                          UsuarioServiceImpl usuarioService) {
        this.generacionTokenService = generacionTokenService;
        this.usuarioService = usuarioService;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, Object> handleValidationExceptions(UserNotFoundException ex) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("mensaje", ex.getMessage());
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

    @PostMapping("/token")
    public String token(Authentication authentication) throws UserNotFoundException {
        // Obtener el token
        String token = generacionTokenService.GenerarToken(authentication);

        // Actualizar el token en el usuario
        usuarioService.actualizarJwt(authentication.getName(), token);

        return token;
    }

}
