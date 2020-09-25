package com.company.test1.validations.contratos;

import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.recibos.ConceptoRecibo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConceptoReciboBeanValidator implements ConstraintValidator <ConceptoReciboBean, ConceptoRecibo> {
    @Override
    public boolean isValid(ConceptoRecibo value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getVigencia()== ConceptoReciboVigenciaEnum.NUMERO_EMISIONES){
            if (value.getNumEmisiones()==null){
                context.buildConstraintViolationWithTemplate("Indicar numero de emisiones").addConstraintViolation();
                isvalid = false;
            }
        }
        if (value.getVigencia()== ConceptoReciboVigenciaEnum.ENTRE_FECHAS){
            if ((value.getFechaDesde()==null)||(value.getFechaHasta()==null)){
                context.buildConstraintViolationWithTemplate("Indicar rango de fechas").addConstraintViolation();
                isvalid = false;
            }
        }

        return isvalid;
    }
}
