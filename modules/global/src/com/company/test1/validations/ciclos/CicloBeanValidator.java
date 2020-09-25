package com.company.test1.validations.ciclos;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.service.ContratosService;
import com.haulmont.cuba.core.global.AppBeans;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CicloBeanValidator implements ConstraintValidator<CicloBean, Ciclo> {

    @Override
    public boolean isValid(Ciclo value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        //si el depto tiene un contrato vigente, hay que asociarlo
        Departamento d = value.getDepartamento();
        ContratoInquilino ci = null;
        try{
            ci = AppBeans.get(ContratosService.class).devuelveContratoVigenteParaDepartamento(d);
        }catch(Exception exc){
            //No reporto el fallo de la validacion. No es un error firme, es una incomplecion de datos pero no critica.
        }
        if (ci!=null){
            if (value.getContratoInquilino()==null){
                context.buildConstraintViolationWithTemplate("Se ha detectado la existencia de un Contrato Vigente pero el ciclo no tiene Contrato Asociado").addConstraintViolation();
                isvalid = false;
            }
        }

        return isvalid;

    }
}
