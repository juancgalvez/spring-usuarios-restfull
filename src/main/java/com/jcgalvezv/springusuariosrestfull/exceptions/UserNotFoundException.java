package com.jcgalvezv.springusuariosrestfull.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super("El usuario no esta registrado");
    }
}
