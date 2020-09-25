package com.company.test1.validations.contratos;

import com.company.test1.entity.contratosinquilinos.Fianza;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.EstadoFianzaEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FianzaBeanValidator implements ConstraintValidator<FianzaBean, Fianza> {
    @Override
    public boolean isValid(Fianza value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getEsAvalBancario()){
            if ((value.getIdentificadorAval()==null)){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Fianza: Aportar Identificador de Aval").addConstraintViolation();
            }
            if ((value.getEscaneoArchivoAdjunto()==null)){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Fianza: Aportar escaneo de Fianza").addConstraintViolation();
            }
        }
        if (value.getContratoInquilino().getEstadoContrato() == EstadoContratoInquilinoEnum.VIGENTE){
            if (value.getEstadoFianza()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Fianza: Indicar estado de Fianza").addConstraintViolation();
            }
        }
        if (value.getTienePolizaAlquiler()){
            if (value.getNumeroPoliza()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Fianza: Indicar No. de Poliza de Alquiler").addConstraintViolation();
            }
            if (value.getEscaneoSeguroArchivoAdjunto()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Fianza: Aportar escaneo de la Poliza de Seguro de Alquiler").addConstraintViolation();
            }
        }

        if (value.getEstadoFianza()!=null){
            if (value.getEstadoFianza()== EstadoFianzaEnum.EN_CAMARA){
                if (value.getFechaIngresoFianzaEnCamara()==null){
                    isvalid = false;
                    context.buildConstraintViolationWithTemplate("Fianza: Indicar Fecha de Ingreso de Fianza en Camara").addConstraintViolation();
                }
                if (value.getEscaneoFianza()==null){
                    isvalid = false;
                    context.buildConstraintViolationWithTemplate("Fianza: Aportar escaneo de Fianza en Camara").addConstraintViolation();
                }
            }
            if (value.getEstadoFianza()== EstadoFianzaEnum.DEVUELTA){
                if (value.getFechaRescateFianzaDeCamara()==null){
                    isvalid = false;
                    context.buildConstraintViolationWithTemplate("Fianza: Indicar Fecha de Rescate de Fianza en Camara").addConstraintViolation();
                }
            }
        }

        return isvalid;
    }
}
