package com.company.test1.validations.recibos;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConceptoBeanValidator implements ConstraintValidator<ConceptoBean, com.company.test1.entity.recibos.Concepto> {

    @Override
    public boolean isValid(com.company.test1.entity.recibos.Concepto value, ConstraintValidatorContext context) {
        return true;

    }
}
