package com.company.test1.service.accessory.mod347;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Arrays;

import com.company.test1.entity.Direccion;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Carlos Conti
 */
public class Registro347_TR2_RI {

//    Integer tipoRegistro = 2;
//    String modeloDeclaracion = "347";
//    String ejercicio = "2017";
//    String nifDeclarante = "";
//    String nifDeclarado = "";
//    String nifRepresentanteLegal = "";
//    String razonSocialDeclarado = "";
//    String tipoHoja = "I";
//
//    String importeAnualOps = "";
//    String situacionInmueble = "1";
//
//
//
//
//
//
//    String codigoProvincia = "";
//    String codigoPais = "";
//    String claveOperacion = "";
//
//    String operacionSeguro = "";
//    String arrendamientoLocalNegocio = "";
//    String importePercibidoMetalico = "";
//    String importeAnualPercibidoTransmisionesInmueblesSujetasIva = "";
//    String ejercicio2 = "";
//    String importeOperacionesPrimerTrimestre_ = "";
//    String importeTransmisionesInmueblesSujetasIvaPrimerTrimestre = "";
//    String importeOperacionesSegundoTrimestre = "";
//    String importeTransmisionesInmueblesSujetasIvaSegundoTrimestre = "";
//    String importeOperacionesTercerTrimestre = "";
//    String importeTransmisionesInmueblesSujetasIvaTercerTrimestre = "";
//    String importeOperacionesCuartoTrimestre = "";
//    String importeTransmisionesInmueblesSujetasIvaCuartoTrimestre = "";
//    String nifOperadorComunitario = "";
//    String operacionesRegimenEspecialCriterioCajaIva = "";
//    String operacionInversionSujetoPasivo = "";
//    String operacionBienesVinculados = "";
//    String importeAnualOperacionesCriterioCajaIva = "";



    public static String escribeRegistro(
            Object ejercicio,
            Object nifDeclarante,
            Object nifDeclarado,
            Object nifRepresentanteLegal,
            Object razonSocialDeclarado,
            Object tipoHoja,
            Object importeAnualOps,
            Object situacionInmueble,
            Object referenciaCatastral,
            Object direccionInmueble
    ) throws Exception{

        String registro = "";

        registro += "2347"; /* tipo registro 2, tipo modelo 247 */
        registro += ejercicio;
        registro += Util.escribeCampo(17, 8, nifDeclarante, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(26, 17, nifDeclarado, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(35, 26, nifRepresentanteLegal, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(75, 35, razonSocialDeclarado, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(76, 75, tipoHoja, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += StringUtils.repeat(" ", 98-76);
        registro += Util.escribeCampo(114, 98, importeAnualOps, Util.TipoValor.ALFANUMERICO_IMPORTE);
        registro += Util.escribeCampo(115, 114, situacionInmueble, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += Util.escribeCampo(140, 115, referenciaCatastral, Util.TipoValor.ALFANUMERICO_TEXTO);

        registro += Util.escribeCampo(333, 140, direccionInmueble, Util.TipoValor.ALFANUMERICO_TEXTO);
        registro += StringUtils.repeat(" ", 500-333);

        registro += System.getProperty("line.separator");
        return registro;
    }

    public static String formateaDireccion(Direccion d) throws Exception{
        String[] nn = new String[]{"Calle", "Avda.", "Pza.", "Paseo", "Otros", "Carretera"};
        String[] nnt = new String[]{"CALLE", "AVDA ", "PLAZA", "PASEO", "CALLE", "CTRA "};
        String s = "";
        //tipo de via
        s+= nnt[Arrays.asList(nn).indexOf(d.getVia())];
        //nombre via publica
        String nvp = Util.traduceACadenaTextoValida(d.getNombreVia());

        s += Util.escribeCampo(195, 145, nvp, Util.TipoValor.ALFANUMERICO_TEXTO);

        //tipo de numeracion
        s += "NUM";
        //numero de casa
        //trato de convertirlo a numero, pues la serializacion exige que lo trate como numero
        Double db = null;
        try{
            db = new Double(d.getNumeroVia());
        }catch(Exception exc){
            db = 0.0;
        }

        s += Util.escribeCampo(203, 198, db, Util.TipoValor.NUMERICO_UNSIGNED);

        //calificador del numero
        s += Util.escribeCampo(206, 203, "", Util.TipoValor.ALFANUMERICO_TEXTO);
        //bloque
        s += Util.escribeCampo(209, 206, "", Util.TipoValor.ALFANUMERICO_TEXTO);;
        //portal
        s += Util.escribeCampo(212, 209, "", Util.TipoValor.ALFANUMERICO_TEXTO);;
        //escalera
        s += Util.escribeCampo(215, 212, "", Util.TipoValor.ALFANUMERICO_TEXTO);
        //planta/piso
        s += Util.escribeCampo(218, 215, d.getPiso(), Util.TipoValor.ALFANUMERICO_TEXTO);
        //puerta
        s += Util.escribeCampo(221, 218, d.getPuerta(), Util.TipoValor.ALFANUMERICO_TEXTO);
        //complemento
        s += Util.escribeCampo(261, 221, "", Util.TipoValor.ALFANUMERICO_TEXTO);
        //localidad o poblacion
        s += Util.escribeCampo(291, 261, d.getPoblacion(), Util.TipoValor.ALFANUMERICO_TEXTO);
        //municipio
        s += Util.escribeCampo(321, 291, d.getPoblacion(), Util.TipoValor.ALFANUMERICO_TEXTO);
        //codigo municipio
        s += Util.escribeCampo(326, 321, "00019", Util.TipoValor.ALFANUMERICO_TEXTO);
        //codigo provincia
        s += Util.escribeCampo(328, 326, "08", Util.TipoValor.ALFANUMERICO_TEXTO);
        //codigo postal
        s += Util.escribeCampo(333, 328, d.getCodigoPostal(), Util.TipoValor.ALFANUMERICO_TEXTO);
        return s;
    }


}