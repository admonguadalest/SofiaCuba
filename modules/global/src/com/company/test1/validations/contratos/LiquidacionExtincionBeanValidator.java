package com.company.test1.validations.contratos;

import com.company.test1.entity.contratosinquilinos.LiquidacionExtincion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LiquidacionExtincionBeanValidator implements ConstraintValidator<LiquidacionSuscripcionBean, LiquidacionExtincion> {
    @Override
    public boolean isValid(LiquidacionExtincion value, ConstraintValidatorContext context) {
        boolean isvalid = true;

        return isvalid;
    }
}
