package com.company.test1.validations.documentosimputables;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.documentosImputables.FacturaProveedor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FacturaProveedorBeanValidator implements ConstraintValidator<FacturaProveedorBean, FacturaProveedor> {


    @Override
    public boolean isValid(FacturaProveedor value, ConstraintValidatorContext context) {
        boolean isvalid = true;

        isvalid = Commons.applyCommonValidations(value, context);

        return isvalid;
    }
}
