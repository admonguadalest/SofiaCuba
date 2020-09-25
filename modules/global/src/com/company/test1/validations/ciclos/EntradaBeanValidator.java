package com.company.test1.validations.ciclos;

import com.company.test1.entity.ciclos.Entrada;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EntradaBeanValidator implements ConstraintValidator<EntradaBean, Entrada> {


    @Override
    public boolean isValid(Entrada value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getEvento()==null){
            context.buildConstraintViolationWithTemplate("Debe asociar un Evento").addConstraintViolation();
            isvalid = false;
        }
        return isvalid;
    }
}
