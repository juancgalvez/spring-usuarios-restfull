package com.jcgalvezv.springusuariosrestfull.customValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomEmail {
    String message() default "Email invalido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
