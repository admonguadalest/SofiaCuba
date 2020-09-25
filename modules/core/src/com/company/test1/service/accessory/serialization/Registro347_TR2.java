package com.company.test1.service.accessory.serialization;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Calendar;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Carlos Conti
 */
public class Registro347_TR2 {

    Integer tipoRegistro = 2;
    String modeloDeclaracion = "347";
    String ejercicio = "2017";
    String nifDeclarante = "";
    String nifDeclarado = "";
    String nifRepresentanteLegal = "";
    String razonSocialDeclarado = "";
    String tipoHoja = "";
    String codigoProvincia = "";
    String codigoPais = "";
    String claveOperacion = "";
    String importeAnualOps = "";
    String operacionSeguro = "";
    String arrendamientoLocalNegocio = "";
    String importePercibidoMetalico = "";
    String importeAnualPercibidoTransmisionesInmueblesSujetasIva = "";
    String ejercicio2 = "";
    String importeOperacionesPrimerTrimestre_ = "";
    String importeTransmisionesInmueblesSujetasIvaPrimerTrimestre = "";
    String importeOperacionesSegundoTrimestre = "";
    String importeTransmisionesInmueblesSujetasIvaSegundoTrimestre = "";
    String importeOperacionesTercerTrimestre = "";
    String importeTransmisionesInmueblesSujetasIvaTercerTrimestre = "";
    String importeOperacionesCuartoTrimestre = "";
    String importeTransmisionesInmueblesSujetasIvaCuartoTrimestre = "";
    String nifOperadorComunitario = "";
    String operacionesRegimenEspecialCriterioCajaIva = "";
    String operacionInversionSujetoPasivo = "";
    String operacionBienesVinculados = "";
    String importeAnualOperacionesCriterioCajaIva = "";



    public static String escribeRegistro(
            Object ejercicio,
            Object nifDeclarante,
            Object nifDeclarado,
            Object nifRepresentanteLegal,
            Object razonSocialDeclarado,
            Object tipoHoja,
            Object codigoProvincia,
            Object codigoPais,
            Object claveOperacion,
            Object importeAnualOps,
            Object operacionSeguro,
            Object arrendamientoLocalNegocio,
            Object importePercibidoMetalico,
            Object importeAnualPercibidoTransmisionesInmueblesSujetasIva,
            Object ejercicio2,
            Object importeOperacionesPrimerTrimestre,
            Object importeTransmisionesInmueblesSujetasIvaPrimerTrimestre,
            Object importeOperacionesSegundoTrimestre,
            Object importeTransmisionesInmueblesSujetasIvaSegundoTrimestre,
            Object importeOperacionesTercerTrimestre,
            Object importeTransmisionesInmueblesSujetasIvaTercerTrimestre,
            Object importeOperacionesCuartoTrimestre,
            Object importeTransmisionesInmueblesSujetasIvaCuartoTrimestre,
            Object nifOperadorComunitario,
            Object operacionesRegimenEspecialCriterioCajaIva,
            Object operacionInversionSujetoPasivo,
            Object operacionBienesVinculados,
            Object importeAnualOperacionesCriterioCajaIva
    ) throws Exception{

        String registro = "";

        registro += "2347"; /* tipo registro 2, tipo modelo 247 */
        registro += ejercicio;
        registro += Util.escribeCampo(17, 8, nifDeclarante, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(26, 17, nifDeclarado, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(35, 26, nifRepresentanteLegal, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(75, 35, razonSocialDeclarado, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(76, 75, "D", Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(78, 76, codigoProvincia, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(80, 78, codigoPais, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += " ";
        registro += claveOperacion;
        registro += Util.escribeCampo(98, 82, importeAnualOps, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(99, 98, operacionSeguro, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(100, 99, arrendamientoLocalNegocio, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(115, 100, importePercibidoMetalico, Util.TipoValor.NUMERICO_UNSIGNED);
        registro += Util.escribeCampo(131, 115, importeAnualPercibidoTransmisionesInmueblesSujetasIva, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += ejercicio2;
        registro += Util.escribeCampo(151, 135, importeOperacionesPrimerTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(167, 151, importeTransmisionesInmueblesSujetasIvaPrimerTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(183, 167, importeOperacionesSegundoTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(199, 183, importeTransmisionesInmueblesSujetasIvaSegundoTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(215, 199, importeOperacionesTercerTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(231, 215, importeTransmisionesInmueblesSujetasIvaTercerTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(247, 231, importeOperacionesCuartoTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(263, 247, importeTransmisionesInmueblesSujetasIvaCuartoTrimestre, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(280, 263, nifOperadorComunitario, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(281, 280, operacionesRegimenEspecialCriterioCajaIva, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(282, 281, operacionInversionSujetoPasivo, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(283, 282, operacionBienesVinculados, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(299, 283, importeAnualOperacionesCriterioCajaIva, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += StringUtils.repeat(" ", 500-299);
        registro += System.getProperty("line.separator");
        return registro;
    }



}