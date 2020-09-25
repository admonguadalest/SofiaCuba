package com.company.test1.validations.contratos;

import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.contratosinquilinos.CotitularContratoInquilino;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.TipoContratoInquilinoEnum;
import com.company.test1.service.ContratosService;
import com.haulmont.cuba.core.global.AppBeans;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ContratoInquilinoBeanValidator implements ConstraintValidator<ContratoInquilinoBean, ContratoInquilino> {
    @Override
    public boolean isValid(ContratoInquilino value, ConstraintValidatorContext context) {
        boolean isvalid = true;
        if (estadoContratoSuperiorOIgualAEstado(value, EstadoContratoInquilinoEnum.AUTORIZADO)){
            if (value.getCiclo()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Especificar ciclo asociado").addConstraintViolation();
            }
        }
        if (estadoContratoSuperiorOIgualAEstado(value, EstadoContratoInquilinoEnum.REDACCION)){
            if (value.getFianza()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Adjuntar informacion de Fianza").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.RENUNCIADO){
            if (value.getFechaDesocupacion()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Especificar fecha de renuncia").addConstraintViolation();
            }
        }
        if (estadoContratoSuperiorOIgualAEstado(value, EstadoContratoInquilinoEnum.VIGENTE)){
            if (value.getDetalleEntregaLlaves()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Especificar Detalle de Entrega de Llaves").addConstraintViolation();
            }
        }
        if (estadoContratoSuperiorOIgualAEstado(value, EstadoContratoInquilinoEnum.VIGENTE)){
            if (value.getEscaneoContrato()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Aportar escaneo de contrato").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.VIGENTE){
            if (value.getImplementacionModelo()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Debe adjuntar la implementacion de un Modelo de Clausulado").addConstraintViolation();
            }
        }
        if (value.getDepartamento().getPropietarioEfectivo().getPersona() instanceof PersonaJuridica){
            if (value.getRepresentanteArrendador()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Debe asignar una representacion para el Propietario").addConstraintViolation();
            }else if(value.getRepresentanteArrendador() instanceof PersonaJuridica){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Debe asignar una persona fisica como representacion de la Propiedad").addConstraintViolation();
            }
        }
        if (value.getInquilino() instanceof PersonaJuridica){
            if (value.getRepresentanteArrendatario()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Debe asignar una representacion para el Arrendatario").addConstraintViolation();
            }else if(value.getRepresentanteArrendatario() instanceof PersonaJuridica){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Debe asignar una persona fisica como representacion del Arrendatario").addConstraintViolation();
            }
        }
        if (value.getTipoContrato() == TipoContratoInquilinoEnum.NUEVA_LAU){
            if (value.getFechaVencimientoPrevisto()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Los contratos de nueva LAU deben tener fecha de vencimiento previsto").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.VIGENTE){
            if ((value.getFechaMandato()==null) || (value.getReferenciaMandato()==null)){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Debe asignar fecha y referencia de mandato").addConstraintViolation();
            }
        }
        if (value.getComunicacionRenuncia()!=null) {
            if ((value.getComunicacionRenuncia()) && (value.getFechaPrevistaRenuncia() == null)) {
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Indicar Fecha Prevista de Renuncia").addConstraintViolation();
            }
        }
        if (value.getComunicacionRenuncia()!=null) {
            if ((value.getComunicacionRenuncia()) && (value.getFechaComunicacion() == null)) {
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Indicar Fecha de Comunicacion de Renuncia").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.RENUNCIADO){
            if (value.getArchivoAdjuntoRenuncia()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Adjuntar escaneo de la renuncia del contrato firmada por el Arrendatario").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.VIGENTE){
            if (value.getCarpetaDocumentoFotograficoFirma()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Adjuntar carpeta de documentos fotograficos de firma").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.RENUNCIADO){
            if (value.getLiquidacionExtincion()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Adjuntar informacion de Liquidacion Extincion de Contrato").addConstraintViolation();
            }
        }
        if (value.getEstadoContrato() == EstadoContratoInquilinoEnum.VIGENTE){
            if (value.getLiquidacionSuscripcion()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Adjuntar informacion de Liquidacion de Suscripcion de Contrato").addConstraintViolation();
            }
        }

        if(!value.getElPagadorEsElTitular()){
            if (value.getPagador()==null){
                isvalid = false;
                context.buildConstraintViolationWithTemplate("Si el Pagador no es el titular, especificar Pagador alternativo").addConstraintViolation();
            }
        }

        if (value.getCotitulares().size()>0){
            for (int i = 0; i < value.getCotitulares().size(); i++) {
                CotitularContratoInquilino cci = value.getCotitulares().get(i);
                if ((cci.getContratoInquilino()==null)||(cci.getCotitular()==null)){
                    isvalid = false;
                    context.buildConstraintViolationWithTemplate("Corregir informacion de Cotitulares (VALOR NULO: CONTRATO O COTITULAR)").addConstraintViolation();
                }
            }
        }

        if (value.getFianza()!=null){
            FianzaBeanValidator fbv = new FianzaBeanValidator();
            if (!fbv.isValid(value.getFianza(), context)){
                isvalid = false;
            }

        }
        if (value.getLiquidacionSuscripcion()!=null){
            LiquidacionSuscripcionBeanValidator fbv = new LiquidacionSuscripcionBeanValidator();
            if (!fbv.isValid(value.getLiquidacionSuscripcion(), context)){
                isvalid = false;
            }

        }
        if (value.getLiquidacionExtincion()!=null){
            LiquidacionExtincionBeanValidator fbv = new LiquidacionExtincionBeanValidator();
            if (!fbv.isValid(value.getLiquidacionExtincion(), context)){
                isvalid = false;
            }

        }

        if (value.getProgramacionRecibo()!=null){
            ProgramacionReciboBeanValidator fbv = new ProgramacionReciboBeanValidator();
            if (!fbv.isValid(value.getProgramacionRecibo(), context)){
                isvalid = false;
            }

        }

        if (value.getEstadoContrato()==EstadoContratoInquilinoEnum.VIGENTE){
            //mirar que no haya otro contrato vigente
            try {
                ContratoInquilino ci = AppBeans.get(ContratosService.class).getContratoVigente(value.getDepartamento());
                if (ci.getId().compareTo(value.getId())!=0){
                    context.buildConstraintViolationWithTemplate("Ya existe un contrato vigente sobre este departamento").addConstraintViolation();
                    isvalid = false;
                }
            }catch(Exception ex){

            }
        }




        return isvalid;
    }

    private boolean estadoContratoSuperiorOIgualAEstado(ContratoInquilino c, EstadoContratoInquilinoEnum estado){
        EstadoContratoInquilinoEnum ec = c.getEstadoContrato();
        return comparator.compare(ec, estado) >= 0;
    }

    Comparator<EstadoContratoInquilinoEnum> comparator = new Comparator<EstadoContratoInquilinoEnum>() {
        EstadoContratoInquilinoEnum[] estados = new EstadoContratoInquilinoEnum[]{
                EstadoContratoInquilinoEnum.REDACCION,
                EstadoContratoInquilinoEnum.EVALUACION,
                EstadoContratoInquilinoEnum.CANCELADO,
                EstadoContratoInquilinoEnum.AUTORIZADO,
                EstadoContratoInquilinoEnum.VIGENTE,
                EstadoContratoInquilinoEnum.RENUNCIADO,
                EstadoContratoInquilinoEnum.DESHAUCIADO
        };

        @Override
        public int compare(EstadoContratoInquilinoEnum o1, EstadoContratoInquilinoEnum o2) {
            List<EstadoContratoInquilinoEnum> l = Arrays.asList(estados);
            Integer io1 = l.indexOf(o1);
            Integer io2 = l.indexOf(o2);
            return io1.compareTo(io2);
        }
    };
}
