package com.company.test1.validations.departamentosyubicacioneas;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DepartamentoBeanValidator implements ConstraintValidator<DepartamentoBean, Departamento> {
    @Override
    public boolean isValid(Departamento value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        Ubicacion u = value.getUbicacion();
        if (!u.getEsPropiedadVertical()){
            if (value.getReferenciaCatastral()==null){
                context.buildConstraintViolationWithTemplate("Aportar informacion catastral en caso de Propiedad Vertical").addConstraintViolation();
                isvalid = false;
            }
        }else{
            if (value.getReferenciaCatastral()!=null){
                context.buildConstraintViolationWithTemplate("Anular informacion catastral en caso de Propiedad Horizontal").addConstraintViolation();
                isvalid = false;
            }
        }
        if (!u.getEsPropiedadVertical()){
            if (value.getPropietario()==null){
                context.buildConstraintViolationWithTemplate("Aportar Propietario").addConstraintViolation();
                isvalid = false;
            }
        }else{
            if (value.getPropietario()!=null){
                context.buildConstraintViolationWithTemplate("Anular valor de Propietario en caso de Propiedad Horizontal").addConstraintViolation();
                isvalid = false;
            }
        }

        return isvalid;
    }
}
