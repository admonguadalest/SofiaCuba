package com.company.test1.validations.modeloscontratosinquilinos;

import com.company.test1.entity.modeloscontratosinquilinos.Seccion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SeccionBeanValidator implements ConstraintValidator<SeccionBean, Seccion> {
    @Override
    public boolean isValid(Seccion value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getClausulas().size()==0){
            context.buildConstraintViolationWithTemplate("Adjuntar al menos una clausula").addConstraintViolation();
            isvalid = false;
        }
        return isvalid;
    }
}
