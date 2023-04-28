package com.jcgalvezv.springusuariosrestfull.customValidators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class CustomPasswordValidator implements ConstraintValidator<CustomPassword, String> {
    private static String regex;
    private static String message;
    private static Pattern pattern;

    // "@Value" para campos "static" se definen en el "setter", no en el campo.
    @Value("${PasswordRegularExpression:^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,20}$}")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Value("${PasswordValidationMessage:Password invalido}")
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void initialize(CustomPassword customPassword) {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        //deshabilitar el mensaje actual de validación
        context.disableDefaultConstraintViolation();
        //construir un nuevo mensaje de validación.
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return pattern.matcher(password).matches();
    }
}
