package com.company.test1.validations.modeloscontratosinquilinos;

import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.validations.recibos.ConceptoBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ModeloContratoBeanValidator implements ConstraintValidator<ModeloContratoBean, ModeloContrato> {
    @Override
    public boolean isValid(ModeloContrato value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getSecciones().size()==0){
            context.buildConstraintViolationWithTemplate("Adjuntar al menos una seccion").addConstraintViolation();
            isvalid = false;
        }
        return isvalid;
    }
}
