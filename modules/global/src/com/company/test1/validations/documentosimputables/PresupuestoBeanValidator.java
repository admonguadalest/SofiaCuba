package com.company.test1.validations.documentosimputables;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PresupuestoBeanValidator implements ConstraintValidator<PresupuestoBean, Presupuesto> {


    @Override
    public boolean isValid(Presupuesto value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        isvalid = Commons.applyCommonValidations(value, context);
        return isvalid;
    }
}
