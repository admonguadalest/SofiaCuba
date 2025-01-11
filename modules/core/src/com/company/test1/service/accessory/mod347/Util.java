package com.company.test1.service.accessory.mod347;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.StringUtils;
import com.company.test1.entity.*;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.jmx.Cubatest1DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;


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
            if (valor==null) valor = 0;
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
            if (valor == null) valor = 0;
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
            if (valor == null) valor = "";
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
            if (valor == null) valor = 0;
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
            if (parteDecimal.length()>nDecimals){
                parteDecimal = parteDecimal.substring(0,nDecimals);
            }
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            Persona propietarioEntorno = p.getPersona();
            if (propietarioEntorno instanceof PersonaFisica){
                propietarioEntorno = AppBeans.get(DataManager.class).reload(propietarioEntorno, "personaFisica-view");
            }
            if (propietarioEntorno instanceof PersonaJuridica){
                propietarioEntorno = AppBeans.get(DataManager.class).reload(propietarioEntorno, "personaJuridica-view");
            }

            String telefonosDeContactoDeclarante = DatoDeContacto.getTelefonosDeContacto(propietarioEntorno);
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

            List allSuppliers = devuelveRegistrosProveedoresParaPresentacion(p, fechaDesde, fechaHasta) ;

            List negocios = devuelveRegistrosNegociosParaPresentacion(p,fechaDesde, fechaHasta);

            List allWithSuppliers = new ArrayList(all);
            allWithSuppliers.addAll(allSuppliers);

            Integer noPersonasYEntidades = cuentaDistintos(allWithSuppliers, 0); /* pendiente */

            Double importeAnualOperaciones = sumaCampo(allWithSuppliers, 6); /*pendiente sql */

            Integer noTotalInmuebles = all.size(); /* pendiente */

            Double importeTotalOpsArrendamientoNegocio = sumaCampo(negocios, 6);

            txt += Registro347_TR1.escribeRegistro(
                    ejercicio, nifDeclarante, razonSocialDeclarante, "T", telefonosDeContactoDeclarante,
                    nombreCompletoRepresentante, 347, " ", " ", noSustitutivoDeclAnterior, noPersonasYEntidades,
                    importeAnualOperaciones, noTotalInmuebles, importeTotalOpsArrendamientoNegocio, "", "");

            for (int i = 0; i < allWithSuppliers.size(); i++) {
                try{
                    Object get = allWithSuppliers.get(i);
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
                }catch(Exception exc){
                    int y = 2;
                }
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
                try{
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

                    Transaction t = AppBeans.get(Persistence.class).createTransaction();
                    Departamento d = (Departamento) AppBeans.get(Persistence.class).getEntityManager().find(Departamento.class, StringUtils.toUUID((String)l.get(11)));
                    d = AppBeans.get(DataManager.class).reload(d, "departamento-view");
                    t.close();
                    Direccion dir = d.getUbicacion().getDireccion();
                    dir.setPiso(d.getPiso());
                    dir.setPuerta((d.getPuerta()));
                    String direccionCompleta = Registro347_TR2_RI.formateaDireccion(dir);

                    txt += Registro347_TR2_RI.escribeRegistro(
                            ejercicio, nifDeclarante, nifDeclarado, nifRepresentanteLegal, nombreRazonSocialDeclarado,
                            "I",importeAnualOps,"1",refCatastral,direccionCompleta);

                }catch(Exception exc){
                    int y = 2;
                }


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
                "    sum(total_recibo_post_ccaa) as sumTotalReciboPostCCAA,\n" +
                "    sum(if(trimestre=1,total_recibo_post_ccaa,0)) as 1T,\n" +
                "    sum(if(trimestre=2,total_recibo_post_ccaa,0)) as 2T,\n" +
                "    sum(if(trimestre=3,total_recibo_post_ccaa,0)) as 3T,\n" +
                "    sum(if(trimestre=4,total_recibo_post_ccaa,0)) as 4T,\n" +
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
                "    total_recibo_post_ccaa,\n" +
                "    fecha_emision,\n" +
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
                "if(d.VIVIENDA_LOCAL = 1, \"V\",\"L\") as tipoDepartamento,\n" +
                "if(u.propietario_id is null, d.REFERENCIA_CATASTRAL,u.INFORMACION_CATASTRAL) as refCatastral,\n" +
                "if(p.DTYPE = \"PF\", \"PF\",\"PJ\") as tipoPersona,\n" +
                "p.id as inquilino_id, p.NIF_DNI as nifDniInquilino, p.nombre,p.apellido1, p.apellido2, p.RAZON_SOCIAL,\n" +
                "r.FECHA_EMISION, quarter(r.FECHA_EMISION) as trimestre, r.TOTAL_RECIBO_POST_CCAA,\n" +
                "concat(dir.NOMBRE_VIA, ' ', dir.NUMERO_VIA, ' ', d.piso, ' ', d.puerta) as direccion\n" +
                "\n" +
                "from recibo r\n" +
                "inner join serie s on r.serie_id = s.id \n" +
                "inner join contrato_inquilino ci on r.utilitario_contrato_inquilino_id = ci.id\n" +
                "inner join departamento d on ci.departamento_id = d.id\n" +
                "inner join ubicacion u on d.ubicacion_id = u.id\n" +
                "inner join persona p on ci.inquilino_id = p.id\n" +
                "inner join direccion dir on u.direccion_id = dir.id\n" +
                "left join propietario propd on d.propietario_id = propd.id\n" +
                "left join propietario propu on u.propietario_id = propu.id\n" +
                "\n" +
                "where r.FECHA_EMISION >= $P{fechaDesde} and r.FECHA_EMISION <= $P{fechaHasta} and (d.propietario_id = '$P{propietarioId}' or u.propietario_id = '$P{propietarioId}') and s.nombre_serie = 'ALQUILERES'\n" +
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

        sql = sql.replace("$P{propietarioId}", p.getId().toString().replace("-",""));
        sql = sql.replace("$P{fechaDesde}", "'" + sdf.format(fechaDesde) + "'");
        sql = sql.replace("$P{fechaHasta}", "'" + sdf.format(fechaHasta) + "'");
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List l = AppBeans.get(Persistence.class).getEntityManager().createNativeQuery(sql).getResultList();
        t.close();
        return l;
    }

    private static List devuelveRegistrosNegociosParaPresentacion(Propietario p, Date fechaDesde, Date fechaHasta) throws Exception{
        String sql = "select * From\n" +
                "(\n" +
                "select\n" +
                "\n" +
                "    nifDniInquilino, nombre, prov,tipoDepartamento, refCatastral, direccion, \n" +
                "    sum(total_recibo_post_ccaa) as sumTotalReciboPostCCAA,\n" +
                "    sum(if(trimestre=1,total_recibo_post_ccaa,0)) as 1T,\n" +
                "    sum(if(trimestre=2,total_recibo_post_ccaa,0)) as 2T,\n" +
                "    sum(if(trimestre=3,total_recibo_post_ccaa,0)) as 3T,\n" +
                "    sum(if(trimestre=4,total_recibo_post_ccaa,0)) as 4T,\n" +
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
                "    left(codigo_postal,2) as prov,\n" +
                "    total_recibo_post_ccaa,\n" +
                "    fecha_emision,\n" +
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
                "if(d.VIVIENDA_LOCAL = 1, \"V\",\"L\") as tipoDepartamento,\n" +
                "if(u.propietario_id is null, d.REFERENCIA_CATASTRAL,u.INFORMACION_CATASTRAL) as refCatastral,\n" +
                "if(p.DTYPE = \"PF\", \"PF\",\"PJ\") as tipoPersona,\n" +
                "p.id as inquilino_id, p.NIF_DNI as nifDniInquilino, p.nombre,p.apellido1, p.apellido2, p.RAZON_SOCIAL,\n" +
                "r.FECHA_EMISION, quarter(r.FECHA_EMISION) as trimestre, r.TOTAL_RECIBO_POST_CCAA,\n" +
                "concat(dir.NOMBRE_VIA, ' ', dir.NUMERO_VIA, ' ', d.piso, ' ', d.puerta) as direccion\n" +
                "\n" +
                "from recibo r\n" +
                "inner join serie s on r.serie_id = s.id \n" +
                "inner join contrato_inquilino ci on r.utilitario_contrato_inquilino_id = ci.id\n" +
                "inner join departamento d on ci.departamento_id = d.id\n" +
                "inner join ubicacion u on d.ubicacion_id = u.id\n" +
                "inner join persona p on ci.inquilino_id = p.id\n" +
                "inner join direccion dir on u.direccion_id = dir.id\n" +
                "left join propietario propd on d.propietario_id = propd.id\n" +
                "left join propietario propu on u.propietario_id = propu.id\n" +
                "\n" +
                "where r.FECHA_EMISION >= $P{fechaDesde} and r.FECHA_EMISION <= $P{fechaHasta} and (d.propietario_id = '$P{propietarioId}' or u.propietario_id = '$P{propietarioId}') and s.nombre_serie = 'ALQUILERES'\n" +
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
        sql = sql.replace("$P{propietarioId}", p.getId().toString().replace("-",""));
        sql = sql.replace("$P{fechaDesde}", "'" + sdf.format(fechaDesde) + "'");
        sql = sql.replace("$P{fechaHasta}", "'" + sdf.format(fechaHasta) + "'");

        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List l = AppBeans.get(Persistence.class).getEntityManager().createNativeQuery(sql).getResultList();
        t.close();
        return l;
    }

    private static List devuelveRegistrosProveedoresParaPresentacion(Propietario p, Date fechaDesde, Date fechaHasta) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String propietarioPersonaId = p.getPersona().getId().toString().replace("-","");
        String sql = "SELECT * FROM \n" +
                "(\n" +
                "select nombre_completo, nif_dni, (q1+q2+q3+q4) as anual, q1,q2,q3,q4\n" +
                "from\n" +
                "(\n" +
                "select nombre_completo, nif_dni, sum(q1) as q1, sum(q2) as q2, sum(q3) as q3, sum(q4) as q4\n" +
                "from\n" +
                "(\n" +
                "select nombre_completo, nif_dni,\n" +
                "if (Q=1,total,0) as q1,\n" +
                "if (Q=2,total,0) as q2,\n" +
                "if (Q=3,total,0) as q3,\n" +
                "if (Q=4,total,0) as q4\n" +
                "from\n" +
                "(\n" +
                "select p2.nombre_completo as titular, p.nombre_completo, p.nif_dni, di.fecha_emision, QUARTER(di.fecha_emision) as Q, di.num_documento, di.importe_post_ccaa as total \n" +
                "from documento_imputable di inner join proveedor prov on di.proveedor_id = prov.id\n" +
                "inner join persona p on prov.persona_id = p.id \n" +
                "inner join persona p2 on di.titular_persona_id = p2.id\n" +
                "where di.fecha_emision >= '" + sdf.format(fechaDesde) + "' and di.fecha_emision <= '" + sdf.format(fechaHasta) + "' \n" +
                "AND di.dtype = 'FP'\n" +
                "and p2.id like '" + propietarioPersonaId + "'\n" +
                "order by p.nombre_completo asc, di.fecha_emision asc\n" +
                ")\n" +
                "as st1\n" +
                ") as st2\n" +
                "\n" +
                "group by nombre_completo, nif_dni\n" +
                "order by nombre_completo\n" +
                ") as st3\n" +
                ") AS ST4 where anual > 3005";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List lProv = AppBeans.get(Persistence.class).getEntityManager().createNativeQuery(sql).getResultList();
        t.close();
        //obteniendo las direcciones

        String nifs = "";
        for (int i = 0; i < lProv.size(); i++) {
            Object[] r = (Object[])lProv.get(i);
            String nif = (String) r[1];
            if (i==0) {
                nifs += "'" + nif + "'";
            }else{
                nifs += ", '" + nif + "'";
            }

        }
        String sqldirs = "SELECT p.nif_dni, min(d.nombre_via) as nombre_via, min(d.numero_via) as numero_via, " +
                " min(d.piso) as piso, min(d.puerta) as puerta, min(d.codigo_postal) as codigo_postal FROM direccion d " +
                " inner join persona p on d.persona_id = p.id where p.nif_dni in ([nifs]) group by p.nif_dni;";
        sqldirs = sqldirs.replace("[nifs]", nifs);
        t = AppBeans.get(Persistence.class).createTransaction();
        List lDirs = AppBeans.get(Persistence.class).getEntityManager().createNativeQuery(sqldirs).getResultList();
        t.close();
        //fin obtencion de direcciones

        //construyendo la lista resultado
        List l = new ArrayList();
        for (int i = 0; i < lProv.size(); i++) {
            Object[] rprov = (Object[]) lProv.get(i);
            Object[] rdir = devuelveRegistroDireccionParaNif((String) rprov[1], lDirs);
            ArrayList record = new ArrayList();
            record.add(rprov[1]);
            record.add(rprov[0]);
            if (rdir[5]!=null){
                String s = (String)rdir[5];
                if (s.length()>2){
                    s = s.substring(0,2);
                    record.add(((String)rdir[5]).substring(0,2));
                }else{
                    record.add("NA");
                }

            }else{
                record.add("NA");
            }
            record.add("");
            record.add("");

            String direccion = "";
            if (rdir[1]!=null){
                direccion += " " + ((String)rdir[1]);
            }else{
                direccion += " NA";
            }
            if (rdir[2]!=null){
                direccion += " " + ((String)rdir[2]);
            }else{
                direccion += " NA";
            }
            if (rdir[3]!=null){
                direccion += " " + ((String)rdir[3]);
            }else{
                direccion += " NA";
            }
            if (rdir[4]!=null){
                direccion += " " + ((String)rdir[4]);
            }else{
                direccion += " NA";
            }
            record.add(direccion);
            if (rprov[2]!=null){
                record.add(rprov[2]);
            }else record.add("NA");
            if (rprov[3]!=null){
                record.add(rprov[3]);
            }else record.add("NA");
            if (rprov[4]!=null){
                record.add(rprov[4]);
            }else record.add("NA");
            if (rprov[5]!=null){
                record.add(rprov[5]);
            }else record.add("NA");
            if (rprov[6]!=null){
                record.add(rprov[6]);
            }else record.add("NA");
            /*Object[] res = new Object[]{
                    rprov[1],
                    rprov[0],
                    ((String)rdir[6]).substring(0,2),
                    "",
                    "",
                    rdir[1],
                    rdir[2],
                    rdir[3],
                    rdir[4],
                    rprov[2],
                    rprov[3],
                    rprov[4],
                    rprov[5],
                    rprov[6]


            };*/
            Object[] res = record.toArray();
            l.add(res);
        }
        return l;
    }

    /**
     * deveulvo registro con valores N/A en caso de no hallar el nif
     * @param nif
     * @param registros;
     * @return
     */
    private static Object[] devuelveRegistroDireccionParaNif(String nif, List registros){
        for (int i = 0; i < registros.size(); i++) {
            Object[] r = (Object[]) registros.get(i);
            String nif_ = (String) r[0];
            if (nif.compareTo(nif_)==0){
                return r;
            }
        }
        return new Object[]{nif, "NA", "NA", "NA", "NA", "NA" };
    }

}