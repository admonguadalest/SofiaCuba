package com.company.test1.validations.modeloscontratosinquilinos;

import com.company.test1.validations.definicionremesa.DefinicionRemesaDelegadoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ModeloContratoBeanValidator.class)

public @interface ModeloContratoBean {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
