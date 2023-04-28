package com.jcgalvezv.springusuariosrestfull.customValidators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class CustomEmailValidator  implements ConstraintValidator<CustomEmail, String>  {
    private static String regex;
    private static String message;

    private static Pattern pattern;

    // "@Value" para campos "static" se definen en el "setter", no en el campo.
    @Value("${EmailRegularExpression:^.*@.*.[cl|CL]$}")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Value("${EmailValidationMessage:Email invalido}")
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void initialize(CustomEmail customEmail) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }

        //deshabilitar el mensaje actual de validación
        context.disableDefaultConstraintViolation();
        //construir un nuevo mensaje de validación.
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return pattern.matcher(email).matches();
    }
}
