package com.company.test1.validations.modeloscontratosinquilinos;

import com.company.test1.entity.modeloscontratosinquilinos.Clausula;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClausulaBeanValidator implements ConstraintValidator<ClausulaBean, Clausula> {
    @Override
    public boolean isValid(Clausula value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getVersiones().size()==0){
            context.buildConstraintViolationWithTemplate("Adjuntar al menos una version de la clausula").addConstraintViolation();
            isvalid = false;
        }
        return isvalid;
    }
}
