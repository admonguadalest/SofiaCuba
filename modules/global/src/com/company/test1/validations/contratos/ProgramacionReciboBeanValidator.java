package com.company.test1.validations.contratos;

import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.ProgramacionRecibo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProgramacionReciboBeanValidator implements ConstraintValidator<ProgramacionReciboBean, ProgramacionRecibo> {
    @Override
    public boolean isValid(ProgramacionRecibo value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getDefinicionRemesa()==null){
            context.buildConstraintViolationWithTemplate("ProgramacionRecibo: Indicar la definicion de remesa").addConstraintViolation();
            isvalid = false;
        }
        if(value.getConceptosRecibo().size()==0){
            context.buildConstraintViolationWithTemplate("ProgramacionRecibo: Indicar al menos un concepto en la Programacion del Recibo").addConstraintViolation();
            isvalid = false;
        }else{
            boolean hayConceptoReciboRenta = false;
            for (int i = 0; i < value.getConceptosRecibo().size(); i++) {
                ConceptoRecibo cr = value.getConceptosRecibo().get(i);
                ConceptoReciboBeanValidator crbv = new ConceptoReciboBeanValidator();
                if (!crbv.isValid(cr, context)){
                    isvalid = false;
                }
                if (cr.getConcepto().getTituloConcepto().compareTo("RENTA")==0){
                    hayConceptoReciboRenta = true;
                }
            }
            if (!hayConceptoReciboRenta){
                context.buildConstraintViolationWithTemplate("ProgramacionRecibo: Debe definir un concepto RENTA en la Programacion del Recibo").addConstraintViolation();
                isvalid = false;
            }
        }

        return isvalid;
    }
}
