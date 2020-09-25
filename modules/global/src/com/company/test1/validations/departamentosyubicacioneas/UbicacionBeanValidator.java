package com.company.test1.validations.departamentosyubicacioneas;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.haulmont.cuba.core.global.AppBeans;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UbicacionBeanValidator implements ConstraintValidator<UbicacionBean, Ubicacion> {
    @Override
    public boolean isValid(Ubicacion value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getEsPropiedadVertical()){
            if (value.getInformacionCatastral()==null){
                context.buildConstraintViolationWithTemplate("Aportar informacion catastral en caso de Propiedad Vertical").addConstraintViolation();
                isvalid = false;
            }
        }else{
            if ((value.getInformacionCatastral()!=null)&&(value.getInformacionCatastral().trim().length()>0)){
                context.buildConstraintViolationWithTemplate("Anular informacion catastral en caso de Propiedad Horizontal").addConstraintViolation();
                isvalid = false;
            }
        }
        if (value.getEsPropiedadVertical()){
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
        if (value.getEsPropiedadVertical()){
            if (value.getInformacionCatastral()==null){
                context.buildConstraintViolationWithTemplate("Aportar Informacion Catastral").addConstraintViolation();
                isvalid = false;
            }
        }
        if (value.getEsPropiedadVertical()){
            //verifico que todos los departamentos esten sin valor de Propietario
            List<Departamento> dd = value.getDepartamentos();
            for (int i = 0; i < dd.size(); i++) {
                Departamento d = dd.get(i);
                if (d.getPropietario()!=null){
                    context.buildConstraintViolationWithTemplate("Uno o más departamentos asociados a esta Ubicación tienen como Propietario un valor no nulo").addConstraintViolation();
                    isvalid = false;
                }
            }
        }
        return isvalid;
    }
}
