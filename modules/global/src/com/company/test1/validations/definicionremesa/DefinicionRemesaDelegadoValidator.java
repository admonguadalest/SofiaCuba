package com.company.test1.validations.definicionremesa;

import com.company.test1.entity.enums.recibos.DefinicionRemesaModoPresentacionEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.recibos.DefinicionRemesa;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DefinicionRemesaDelegadoValidator implements ConstraintValidator<DefinicionRemesaDelegado, DefinicionRemesa> {

    @Override
    public void initialize(DefinicionRemesaDelegado constraintAnnotation) {
        int y = 2;
    }

    @Override
    public boolean isValid(DefinicionRemesa value, ConstraintValidatorContext context) {
        boolean finalRes = true;
        ConstraintValidatorContextImpl cvci = (ConstraintValidatorContextImpl) context;
        if (value.getModoPresentacion()== DefinicionRemesaModoPresentacionEnum.DELEGADO){
            if (value.getDelegado()==null){
                cvci.buildConstraintViolationWithTemplate("Si se especifica modo DELEGADO, debe aportarse la persona asignada como Delegado").addConstraintViolation();
                finalRes = false;
            }
        }
        if (value.getTipoGiro()== DefinicionRemesaTipoGiroEnum.BANCARIA){
            if (value.getCuentaBancaria()==null){
                cvci.buildConstraintViolationWithTemplate("Si el modo de presentacion es Bancario, aportar la Cuenta Bancaria").addConstraintViolation();
                finalRes = false;
            }
        }
        return finalRes;
    }
}