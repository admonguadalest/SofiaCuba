package com.company.test1.validations.contratos;

import com.company.test1.entity.contratosinquilinos.LiquidacionSuscripcion;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LiquidacionSuscripcionBeanValidator implements ConstraintValidator<LiquidacionSuscripcionBean, LiquidacionSuscripcion> {
    @Override
    public boolean isValid(LiquidacionSuscripcion value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getContratoInquilino().getEstadoContrato() == EstadoContratoInquilinoEnum.VIGENTE){
            if (value.getEscaneoLiquidacion()==null){
                context.buildConstraintViolationWithTemplate("Pendiente escanear Liquidacion de Contrato").addConstraintViolation();
                isvalid = false;
            }
        }
        return isvalid;
    }
}
