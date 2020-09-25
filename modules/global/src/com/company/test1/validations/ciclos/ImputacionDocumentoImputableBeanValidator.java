package com.company.test1.validations.ciclos;

import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImputacionDocumentoImputableBeanValidator implements ConstraintValidator<ImputacionDocumentoImputableBean, ImputacionDocumentoImputable> {
    @Override
    public boolean isValid(ImputacionDocumentoImputable value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getEvento()==null){
            context.buildConstraintViolationWithTemplate("Debe asociar un Evento").addConstraintViolation();
            isvalid = false;
        }
        return isvalid;
    }
}
