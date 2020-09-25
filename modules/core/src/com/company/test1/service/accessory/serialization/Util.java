package com.company.test1.service.accessory.serialization;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.Direccion;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.StringUtils;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Carlos Conti
 */
public class Util {

    public enum TipoValor {NUMERICO_SIGNED, NUMERICO_UNSIGNED, ALFANUMERICO_TEXTO, ALFANUMERICO_IMPORTE}

    public static String escribeCampo(int posHasta, int posDesde, Object valor, TipoValor tipo ) throws Exception{
        DecimalFormat df = new DecimalFormat("#,##0.00");
        if (tipo == TipoValor.NUMERICO_SIGNED){
            String s = devuelveNumeroFormateado((Number) valor, 2);
            int relleno = posHasta - posDesde - s.length();
            relleno-=1;
            if (relleno > 0){
                s = StringUtils.repeat("0", relleno) + s;
            }else{
                s = s.substring(0, posHasta - posDesde-1);
            }
            if (((Number) valor).doubleValue() > 0.0){
                s = " " + s;
            }else{
                s = "N" + s;
            }
            return s;
        }
        if (tipo == TipoValor.NUMERICO_UNSIGNED){
            String s = devuelveNumeroFormateado((Number) valor, 0);
            int relleno = posHasta - posDesde - s.length();

            if (relleno > 0){
                s = StringUtils.repeat("0", relleno) + s;
            }else{
                s = s.substring(0, posHasta - posDesde-1);
            }

            return s;
        }
        if (tipo == TipoValor.ALFANUMERICO_TEXTO){
            String s = (String) valor;
            s = traduceACadenaTextoValida(s);
            int relleno = posHasta - posDesde - s.length();
            if (relleno > 0){
                s = s + StringUtils.repeat(" ", relleno);
            }else{
                s = s.substring(0, posHasta - posDesde);
            }
            return s;

        }
        if (tipo == TipoValor.ALFANUMERICO_IMPORTE){
            Object valorSinSigno = new Double(Math.abs(((Number)valor).doubleValue()));
            String s = devuelveNumeroFormateado((Number) valorSinSigno, 2);
            String parteEntera = s.substring(0, s.length()-2);
            String parteDecimal = s.substring(s.length()-2);
            int rellenoParteEntera = (posHasta-2) - (posDesde+1) - parteEntera.length(); //le quito uno del caracter de signo y dos de los decimales
            int rellenoParteDecimal = 2 - parteDecimal.length();
            if (rellenoParteEntera > 0){
                parteEntera = StringUtils.repeat("0", rellenoParteEntera) + parteEntera;
            }else{
                parteEntera = parteEntera.substring(0, (posHasta-2) - (posDesde-1));
            }
            if (rellenoParteDecimal > 0){
                parteDecimal = StringUtils.repeat("0", rellenoParteDecimal) + parteDecimal;
            }else{
                parteDecimal = parteDecimal.substring(0, 2);
            }
            s = parteEntera + parteDecimal;
            if (((Number) valor).doubleValue() >= 0.0){
                s = " " + s;
            }else{
                s = "N" + s;
            }
            return s;
        }
        return null;
    }

    public static int parteEntera(Number n){
        return new Double(Math.floor(n.doubleValue())).intValue();
    }

    public static int parteDecimal(Number n, int nDecimals){
        double rem = n.doubleValue() - Math.floor(n.doubleValue());
        double factor = Math.pow(10, nDecimals);
        rem *= factor;
        return new Double(Math.rint(rem)).intValue();
    }

    public static void main(String[] args){
        int pd = parteDecimal(8839.05, 2);
        int y = 2;
    }

    public static String infoSigno(Number n){
        if (n.doubleValue()< 0.0){
            return "N";
        }else{
            return " ";
        }
    }

    public static String devuelveNumeroFormateado(Number n, int nDecimals){
        int pe = parteEntera(n);
        int pd = parteDecimal(n, nDecimals);
        String res = "";
        if (nDecimals > 0){
            String parteDecimal = new Integer(pd).toString();
            parteDecimal = StringUtils.repeat("0", nDecimals - parteDecimal.length()) + parteDecimal;
            res = new Integer(pe).toString() + parteDecimal;
        }else{
            res = new Integer(pe).toString();
        }

        return res;
    }

    public static String traduceACadenaTextoValida(String input){
        input = input.toLowerCase();
        String v = "abcdefghijklmnñopqrstuvwxyz ,.0123456789-()üáéíóúàèìòùºª";
        String b = "abcdefghijklmnnopqrstuvwxyz ,.0123456789   uaeiouaeiouoa";
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int ioc = v.indexOf(c);
            if (ioc!=-1){
                output += b.charAt(v.indexOf(c));
            }else{
                output += " ";
            }

        }
        return output.toUpperCase();
    }

    public static String confeccionaImportable(Propietario p, Date fechaDesde, Date fechaHasta) throws Exception{
        String txt = "";
        try {

            Persistence persistence = AppBeans.get(Persistence.class);

            Transaction t = persistence.createTransaction();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            Persona propietarioEntorno = p.getPersona();
            if (propietarioEntorno instanceof PersonaFisica){
                propietarioEntorno = persistence.getEntityManager().reload(propietarioEntorno, "personaFisica-view");
            }
            if (propietarioEntorno instanceof PersonaJuridica){
                propietarioEntorno = persistence.getEntityManager().reload(propietarioEntorno, "personaJuridica-view");
            }

            t.close();
            String telefonosDeContactoDeclarante = "PENDIENTE";
            String nombreCompletoRepresentante = "";
            if (propietarioEntorno instanceof PersonaFisica) {
                nombreCompletoRepresentante = propietarioEntorno.getNombreCompleto();
            }
            if (propietarioEntorno instanceof PersonaJuridica) {

                nombreCompletoRepresentante = propietarioEntorno.getRepresentacionesLegales().get(0).getPersona_representante().getNombreCompleto();
            }

            String ejercicio = sdf.format(fechaDesde);
            String nifDeclarante = p.getPersona().getNifDni();
            String razonSocialDeclarante = p.getPersona().getNombreCompleto();
            Integer noSustitutivoDeclAnterior = 0;

            List all = devuelveTodosRegistrosParaPresentacion(p,fechaDesde, fechaHasta);
            List negocios = devuelveRegistrosNegociosParaPresentacion(p,fechaDesde, fechaHasta);

            Integer noPersonasYEntidades = cuentaDistintos(all, 0); /* pendiente */

            Double importeAnualOperaciones = sumaCampo(all, 6); /*pendiente sql */

            Integer noTotalInmuebles = all.size(); /* pendiente */

            Double importeTotalOpsArrendamientoNegocio = sumaCampo(negocios, 6);

            txt += Registro347_TR1.escribeRegistro(
                    ejercicio, nifDeclarante, razonSocialDeclarante, "T", telefonosDeContactoDeclarante,
                    nombreCompletoRepresentante, 347, " ", " ", noSustitutivoDeclAnterior, noPersonasYEntidades,
                    importeAnualOperaciones, noTotalInmuebles, importeTotalOpsArrendamientoNegocio, "", "");

            for (int i = 0; i < all.size(); i++) {
                Object get = all.get(i);
                List l = Arrays.asList((Object[]) get);
                String nifDeclarado = (String) l.get(0);
                String nifRepresentanteLegal = "";
                String nombreRazonSocialDeclarado = (String) l.get(1);
                String codProvincia = "08";
                String codPais = "";
                Double importeAnualOps = (Double) l.get(6);
                String arrendamientoLocalNegocio = "";
                Double importeAnualPercibidoSujetasIva = 0.0;
                Double importeOpsPrimerTrimSujetas = 0.0;
                Double importeOpsSegundoTrimSujetas = 0.0;
                Double importeOpsTercerTrimSujetas = 0.0;
                Double importeOpsCuartoTrimSujetas = 0.0;
                if (((String)l.get(3)).compareTo("L")==0){
                    arrendamientoLocalNegocio = "X";
                    importeAnualPercibidoSujetasIva = (Double) l.get(6);
                    importeOpsPrimerTrimSujetas = (Double) l.get(7);
                    importeOpsSegundoTrimSujetas = (Double) l.get(8);
                    importeOpsTercerTrimSujetas = (Double) l.get(9);
                    importeOpsCuartoTrimSujetas = (Double) l.get(10);
                }
                Double importeOpsPrimerTrim = (Double) l.get(7);
                Double importeOpsSegundoTrim = (Double) l.get(8);
                Double importeOpsTercerTrim = (Double) l.get(9);
                Double importeOpsCuartoTrim = (Double) l.get(10);

                String nifOperadorComunitario = "";

                txt += Registro347_TR2.escribeRegistro(
                        ejercicio,
                        nifDeclarante, nifDeclarado, nifRepresentanteLegal, nombreRazonSocialDeclarado, "D", codProvincia, codPais,
                        "B", importeAnualOps, "",
                        arrendamientoLocalNegocio, 0.0, importeAnualPercibidoSujetasIva,
                        ejercicio, importeOpsPrimerTrim, importeOpsPrimerTrimSujetas, importeOpsSegundoTrim, importeOpsSegundoTrimSujetas,
                        importeOpsTercerTrim, importeOpsTercerTrimSujetas, importeOpsCuartoTrim, importeOpsCuartoTrimSujetas,
                        nifOperadorComunitario, "", "", "", 0.0);
                System.out.println(i);
            }
            //locales: registros inmuebles
//            for (int i = 0; i < negocios.size(); i++) {
//                Object get = negocios.get(i);
//                List l = Arrays.asList((Object[]) get);
//                String nifDeclarado = (String) l.get(0);
//                String nifRepresentanteLegal = "";
//                String nombreRazonSocialDeclarado = (String) l.get(1);
//                String codProvincia = "08";
//                String codPais = "";
//                Double importeAnualOps = (Double) l.get(6);
//                String arrendamientoLocalNegocio = "";
//                Double importeAnualPercibidoSujetasIva = 0.0;
//                Double importeOpsPrimerTrimSujetas = 0.0;
//                Double importeOpsSegundoTrimSujetas = 0.0;
//                Double importeOpsTercerTrimSujetas = 0.0;
//                Double importeOpsCuartoTrimSujetas = 0.0;
//                if (((String)l.get(3)).compareTo("L")==0){
//                    arrendamientoLocalNegocio = "X";
//                    importeAnualPercibidoSujetasIva = (Double) l.get(6);
//                    importeOpsPrimerTrimSujetas = (Double) l.get(7);
//                    importeOpsSegundoTrimSujetas = (Double) l.get(8);
//                    importeOpsTercerTrimSujetas = (Double) l.get(9);
//                    importeOpsCuartoTrimSujetas = (Double) l.get(10);
//                }
//                Double importeOpsPrimerTrim = (Double) l.get(7);
//                Double importeOpsSegundoTrim = (Double) l.get(8);
//                Double importeOpsTercerTrim = (Double) l.get(9);
//                Double importeOpsCuartoTrim = (Double) l.get(10);
//
//                String nifOperadorComunitario = "";
//
//                txt += Registro347_TR2.escribeRegistro(
//                        ejercicio,
//                        nifDeclarante, nifDeclarado, nifRepresentanteLegal, nombreRazonSocialDeclarado, "D", codProvincia, codPais,
//                        "B", importeAnualOps, "",
//                        arrendamientoLocalNegocio, 0.0, importeAnualPercibidoSujetasIva,
//                        ejercicio, importeOpsPrimerTrim, importeOpsPrimerTrimSujetas, importeOpsSegundoTrim, importeOpsSegundoTrimSujetas,
//                        importeOpsTercerTrim, importeOpsTercerTrimSujetas, importeOpsCuartoTrim, importeOpsCuartoTrimSujetas,
//                        nifOperadorComunitario, "", "", "", 0.0);
//
//            }

            for (int i = 0; i < negocios.size(); i++) {
                Object get = negocios.get(i);
                List l = Arrays.asList((Object[]) get);
                String nifDeclarado = (String) l.get(0);
                String nifRepresentanteLegal = "";
                String nombreRazonSocialDeclarado = (String) l.get(1);
                String codProvincia = "08";
                String codPais = "";
                String refCatastral = (String) l.get(4);
                Double importeAnualOps = (Double) l.get(6);
                String arrendamientoLocalNegocio = "";
                Double importeAnualPercibidoSujetasIva = 0.0;
                Double importeOpsPrimerTrimSujetas = 0.0;
                Double importeOpsSegundoTrimSujetas = 0.0;
                Double importeOpsTercerTrimSujetas = 0.0;
                Double importeOpsCuartoTrimSujetas = 0.0;
                if (((String)l.get(3)).compareTo("L")==0){
                    arrendamientoLocalNegocio = "X";
                    importeAnualPercibidoSujetasIva = (Double) l.get(6);
                    importeOpsPrimerTrimSujetas = (Double) l.get(7);
                    importeOpsSegundoTrimSujetas = (Double) l.get(8);
                    importeOpsTercerTrimSujetas = (Double) l.get(9);
                    importeOpsCuartoTrimSujetas = (Double) l.get(10);
                }
                Double importeOpsPrimerTrim = (Double) l.get(7);
                Double importeOpsSegundoTrim = (Double) l.get(8);
                Double importeOpsTercerTrim = (Double) l.get(9);
                Double importeOpsCuartoTrim = (Double) l.get(10);

                String nifOperadorComunitario = "";

//                Departamento d = (Departamento) sl.getInstance(Departamento.class, (Integer) l.get(11));
                t = persistence.createTransaction();
                String uuid = (String)l.get(11);
                uuid = uuid.substring(0,8) + "-" +uuid.substring(9,13) + "-" +uuid.substring(13,17)+ "-" +uuid.substring(17,21)+ "-" +uuid.substring(21);
                Departamento d = persistence.getEntityManager().find(Departamento.class, UUID.fromString(uuid));
                t.close();
                Direccion dir = d.getUbicacion().getDireccion();

                dir.setPiso(d.getPiso());
                dir.setPuerta((d.getPuerta()));
                String direccionCompleta = Registro347_TR2_RI.formateaDireccion(dir);

                txt += Registro347_TR2_RI.escribeRegistro(
                        ejercicio, nifDeclarante, nifDeclarado, nifRepresentanteLegal, nombreRazonSocialDeclarado,
                        "I",importeAnualOps,"1",refCatastral,direccionCompleta);


            }

        } catch (Exception exception) {
            throw exception;
        }
        return txt;
    }

    private static double sumaCampo(List records, int fieldIndex){
        double d = 0.0;
        for (int i = 0; i < records.size(); i++) {
            Object record = records.get(i);
            Object get = ((Object[]) record)[fieldIndex];
            if (get instanceof Number){
                d += ((Number)get).doubleValue();
            }
        }
        return d;
    }

    private static int cuentaDistintos(List records, int fieldIndex){
        double d = 0.0;
        List uniques = new ArrayList();
        for (int i = 0; i < records.size(); i++) {
            Object record = records.get(i);
            Object get = ((Object[])record)[fieldIndex];
            if (uniques.indexOf(get.toString())==-1){
                uniques.add(get.toString());
            }
        }
        return uniques.size();
    }

    private static List devuelveTodosRegistrosParaPresentacion(Propietario p, Date fechaDesde, Date fechaHasta) throws Exception{
        String sql = "select * from\n" +
                "(\n" +
                "select\n" +
                "\n" +
                "    nifDniInquilino, nombre, prov,tipoDepartamento, refCatastral, direccion,\n" +
                "    sum(totalReciboPostCCAA) as sumTotalReciboPostCCAA,\n" +
                "    sum(if(trimestre=1,totalReciboPostCCAA,0)) as 1T,\n" +
                "    sum(if(trimestre=2,totalReciboPostCCAA,0)) as 2T,\n" +
                "    sum(if(trimestre=3,totalReciboPostCCAA,0)) as 3T,\n" +
                "    sum(if(trimestre=4,totalReciboPostCCAA,0)) as 4T,\n" +
                "    did \n" +
                "\n" +
                "\n" +
                "\n" +
                "from\n" +
                "\n" +
                "(\n" +
                "select\n" +
                "    nifDniInquilino,\n" +
                "    if (tipoPersona=\"PF\",concat(apellido1, ' ',apellido2, ', ',nombre),nombre) as nombre,\n" +
                "    left(CODIGO_POSTAL,2) as prov,\n" +
                "    totalReciboPostCCAA,\n" +
                "    fechaEmision,\n" +
                "    trimestre,\n" +
                "    tipoDepartamento,\n" +
                "    refCatastral,\n" +
                "    direccion, \n" +
                "    did \n" +
                "\n" +
                "\n" +
                "from\n" +
                "\n" +
                "(\n" +
                "select u.nombre as nombreUbicacion, d.id as did, d.piso, d.puerta, dir.CODIGO_POSTAL,\n" +
                "coalesce(d.propietario_id, u.propietario_id) as propietario_id,\n" +
                "if(d.VIVIENDA_LOCAL = 0, \"L\",\"V\") as tipoDepartamento,\n" +
                "if(u.propietario_id is null, d.REFERENCIA_CATASTRAL,u.INFORMACION_CATASTRAL) as refCatastral,\n" +
                "if(p.DTYPE = \"PJ\", \"PJ\",\"PF\") as tipoPersona,\n" +
                "p.id as inquilino_id, p.NIF_DNI as nifDniInquilino, p.nombre,p.apellido1, p.apellido2, p.RAZON_SOCIAL,\n" +
                "r.FECHA_EMISION as fechaEmision, quarter(r.FECHA_EMISION) as trimestre, r.TOTAL_RECIBO_POST_CCAA as totalReciboPostCCAA,\n" +
                "concat(dir.NOMBRE_VIA, ' ', dir.NUMERO_VIA, ' ', d.piso, ' ', d.puerta) as direccion\n" +
                "\n" +
                "from RECIBO r\n" +
                "inner join CONTRATO_INQUILINO ci on r.utilitario_contrato_inquilino_id = ci.id\n" +
                "inner join DEPARTAMENTO d on ci.departamento_id = d.id\n" +
                "inner join UBICACION u on d.ubicacion_id = u.id\n" +
                "inner join PERSONA p on ci.inquilino_id = p.id\n" +
                "inner join DIRECCION dir on u.direccion_id = dir.id\n" +
                "left join PROPIETARIO propd on d.propietario_id = propd.id\n" +
                "left join PROPIETARIO propu on u.propietario_id = propu.id\n" +
                "\n" +
                "where r.FECHA_EMISION >= $P{fechaDesde} and r.FECHA_EMISION <= $P{fechaHasta} and (d.propietario_id = \"$P{propietarioId}\" or u.propietario_id = \"$P{propietarioId}\")\n" +
                "order by u.nombre, piso, puerta, inquilino_id, r.FECHA_EMISION\n" +
                ") as st1\n" +
                "\n" +
                ") as st2\n" +
                "\n" +
                "group by nifDniInquilino, nombre, prov, tipoDepartamento, refCatastral, direccion\n" +
                ") as st3\n" +
                "\n" +
                "where sumTotalReciboPostCCAA > 3005.60";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sql = sql.replace("$P{propietarioId}", p.getId().toString().replace("-", ""));
        sql = sql.replace("$P{fechaDesde}", "'" + sdf.format(fechaDesde) + "'");
        sql = sql.replace("$P{fechaHasta}", "'" + sdf.format(fechaHasta) + "'");

        Persistence persistence = AppBeans.get(Persistence.class);

        Transaction t = persistence.createTransaction();

        persistence.getEntityManager().createNativeQuery("set sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'").executeUpdate();

        List l = persistence.getEntityManager().createNativeQuery(sql).getResultList();
        t.close();

        return l;
    }

    private static  List devuelveRegistrosNegociosParaPresentacion(Propietario p, Date fechaDesde, Date fechaHasta) throws Exception{
        String sql = "select * From\n" +
                "(\n" +
                "select\n" +
                "\n" +
                "    nifDniInquilino, nombre, prov,tipoDepartamento, refCatastral, direccion, \n" +
                "    sum(totalReciboPostCCAA) as sumTotalReciboPostCCAA,\n" +
                "    sum(if(trimestre=1,totalReciboPostCCAA,0)) as 1T,\n" +
                "    sum(if(trimestre=2,totalReciboPostCCAA,0)) as 2T,\n" +
                "    sum(if(trimestre=3,totalReciboPostCCAA,0)) as 3T,\n" +
                "    sum(if(trimestre=4,totalReciboPostCCAA,0)) as 4T,\n" +
                "    did \n" +
                "\n" +
                "\n" +
                "\n" +
                "from\n" +
                "\n" +
                "(\n" +
                "select\n" +
                "    nifDniInquilino,\n" +
                "    if (tipoPersona=\"PF\",concat(apellido1, ' ',apellido2, ', ',nombre),nombre) as nombre,\n" +
                "    left(codigoPostal,2) as prov,\n" +
                "    totalReciboPostCCAA,\n" +
                "    fechaEmision,\n" +
                "    trimestre,\n" +
                "    tipoDepartamento,\n" +
                "    refCatastral,\n" +
                "    direccion, \n" +
                "    did \n" +
                "\n" +
                "\n" +
                "from\n" +
                "\n" +
                "(\n" +
                "select u.nombre as nombreUbicacion, d.id as did, d.piso, d.puerta, dir.CODIGO_POSTAL as codigoPostal,\n" +
                "coalesce(d.propietario_id, u.propietario_id) as propietario_id,\n" +
                "if(d.VIVIENDA_LOCAL = \"0\", \"L\",\"V\") as tipoDepartamento,\n" +
                "if(u.propietario_id is null, d.REFERENCIA_CATASTRAL,u.INFORMACION_CATASTRAL) as refCatastral,\n" +
                "if(p.DTYPE = \"PJ\", \"PJ\",\"PF\") as tipoPersona,\n" +
                "p.id as inquilino_id, p.NIF_DNI as nifDniInquilino, p.nombre,p.apellido1, p.apellido2, p.RAZON_SOCIAL,\n" +
                "r.FECHA_EMISION as fechaEmision, quarter(r.FECHA_EMISION) as trimestre, r.TOTAL_RECIBO_POST_CCAA as totalReciboPostCCAA,\n" +
                "concat(dir.NOMBRE_VIA, ' ', dir.NUMERO_VIA, ' ', d.piso, ' ', d.puerta) as direccion\n" +
                "\n" +
                "from RECIBO r\n" +
                "inner join CONTRATO_INQUILINO ci on r.utilitario_contrato_inquilino_id = ci.id\n" +
                "inner join DEPARTAMENTO d on ci.departamento_id = d.id\n" +
                "inner join UBICACION u on d.ubicacion_id = u.id\n" +
                "inner join PERSONA p on ci.inquilino_id = p.id\n" +
                "inner join DIRECCION dir on u.direccion_id = dir.id\n" +
                "left join PROPIETARIO propd on d.propietario_id = propd.id\n" +
                "left join PROPIETARIO propu on u.propietario_id = propu.id\n" +
                "\n" +
                "where r.FECHA_EMISION >= $P{fechaDesde} and r.FECHA_EMISION <= $P{fechaHasta} and (d.propietario_id = \"$P{propietarioId}\" or u.propietario_id = \"$P{propietarioId}\")\n" +
                "order by u.nombre, piso, puerta, inquilino_id, r.FECHA_EMISION\n" +
                ") as st1\n" +
                "\n" +
                ") as st2\n" +
                "where tipoDepartamento like 'L'\n" +
                "\n" +
                "group by nifDniInquilino, nombre, prov, tipoDepartamento, refCatastral, direccion\n" +
                ") as st3\n" +
                "\n" +
                "where sumTotalReciboPostCCAA > 3005.60";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sql = sql.replace("$P{propietarioId}", p.getId().toString().replace("-", ""));
        sql = sql.replace("$P{fechaDesde}", "'" + sdf.format(fechaDesde) + "'");
        sql = sql.replace("$P{fechaHasta}", "'" + sdf.format(fechaHasta) + "'");


        Persistence persistence = AppBeans.get(Persistence.class);
        Transaction t = persistence.createTransaction();

        persistence.getEntityManager().createNativeQuery("set sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'").executeUpdate();

        List l = persistence.getEntityManager().createNativeQuery(sql).getResultList();
        t.close();

        return l;
    }
}
