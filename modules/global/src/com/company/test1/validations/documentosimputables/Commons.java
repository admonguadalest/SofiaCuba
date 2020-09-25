package com.company.test1.validations.documentosimputables;

import com.company.test1.NumberUtils;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;

import javax.validation.ConstraintValidatorContext;

public class Commons {

    public static boolean applyCommonValidations(DocumentoProveedor value, ConstraintValidatorContext context){
        boolean isvalid = true;
        if (value.getImputacionesDocumentoImputable().size()==0){
            context.buildConstraintViolationWithTemplate("Debe asociar al menos una imputacion").addConstraintViolation();
            isvalid = false;
        }

        double cumPerc = 0.0;
        double cumImporte = 0.0;
        for (int i = 0; i < value.getImputacionesDocumentoImputable().size(); i++) {
            ImputacionDocumentoImputable idi = value.getImputacionesDocumentoImputable().get(i);
            cumPerc += idi.getPorcentajeImputacion();
            cumImporte += idi.getImporteImputacion();
        }
        cumPerc = NumberUtils.roundTo2Decimals(cumPerc);
        cumImporte = NumberUtils.roundTo2Decimals(cumImporte);

        if ((cumPerc != 1.0) || (cumImporte!=value.getImporteTotalBase().doubleValue())){
            context.buildConstraintViolationWithTemplate("La suma de importes/porcentajes de las imputaciones asociadas no corresponden con el documento").addConstraintViolation();
            isvalid = false;
        }

        ColeccionArchivosAdjuntos caa = value.getColeccionArchivosAdjuntos();
        if (caa.getArchivos().size()==0){
            context.buildConstraintViolationWithTemplate("Aportar escaneo como archivo adjunto").addConstraintViolation();
            isvalid = false;
        }
        return isvalid;
    }
}
