package com.company.test1.validations.personas;

import com.company.test1.entity.DatoDeContacto;
import com.company.test1.entity.Persona;
import com.company.test1.entity.enums.TipoDeDatoDeContactoEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PersonaBeanValidator implements ConstraintValidator<PersonaBean, Persona> {
    @Override
    public boolean isValid(Persona value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (value.getDirecciones().size()==0){
            context.buildConstraintViolationWithTemplate("Ingresar al menos una direccion postal").addConstraintViolation();
            isvalid = false;
        }
        if (value.getDatosDeContacto().size()==0){
            context.buildConstraintViolationWithTemplate("Aportar al menos un dato de contacto").addConstraintViolation();
            isvalid = false;
        }
        List<DatoDeContacto> mobiles = DatoDeContacto.getDatosDeContactoParaTipo(value, TipoDeDatoDeContactoEnum.MOBIL);
        List<DatoDeContacto> telefonos = DatoDeContacto.getDatosDeContactoParaTipo(value, TipoDeDatoDeContactoEnum.TELEFONO);
        List<DatoDeContacto> correos = DatoDeContacto.getDatosDeContactoParaTipo(value, TipoDeDatoDeContactoEnum.CORREO_ELECTRONICO);
        if (correos.size()==0){
            context.buildConstraintViolationWithTemplate("Aportar al menos un email como dato de contacto").addConstraintViolation();
            isvalid = false;
        }
        if ((telefonos.size()==0) && (mobiles.size()==0)){
            context.buildConstraintViolationWithTemplate("Aportar al menos un telefono fijo o mobil").addConstraintViolation();
            isvalid = false;
        }
        if (value.getCuentasBancarias().size()==0){
            context.buildConstraintViolationWithTemplate("Aportar al menos una cuenta bancaria").addConstraintViolation();
            isvalid = false;
        }
        if (value.getProveedor()!=null){
            if ((value.getProveedor().getDescripcionActividad()==null)||(value.getProveedor().getDescripcionActividad().trim().length()==0)){
                context.buildConstraintViolationWithTemplate("Aportar descripción de actividad para Proveedor").addConstraintViolation();
                isvalid = false;
            }
            if ((value.getProveedor().getNombreComercial()==null)||(value.getProveedor().getNombreComercial().trim().length()==0)){
                context.buildConstraintViolationWithTemplate("Aportar Nombre Comercial").addConstraintViolation();
                isvalid = false;
            }
            if (value.getProveedor().getModoDePagoTelematico()){
                if (value.getProveedor().getCuentaBancaria()==null){
                    context.buildConstraintViolationWithTemplate("Aportar cuenta bancaria para modo de pago telemático").addConstraintViolation();
                    isvalid = false;
                }
            }
            if (value.getProveedor().getModoDePagoDomiciliado()){
                if (value.getProveedor().getCuentaBancariaDomiciliado()==null){
                    context.buildConstraintViolationWithTemplate("Aportar cuenta bancaria para modo de pago domiciliado").addConstraintViolation();
                    isvalid = false;
                }
            }
        }
        return isvalid;

    }
}
