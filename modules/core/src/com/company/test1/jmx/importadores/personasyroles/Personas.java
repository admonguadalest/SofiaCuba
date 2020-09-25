package com.company.test1.jmx.importadores.personasyroles;

import com.company.test1.entity.*;
import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.enums.EstadoCivilEnum;
import com.company.test1.entity.enums.ModoPagoProveedor;
import com.company.test1.entity.enums.NombreTipoDireccion;
import com.company.test1.entity.enums.TipoDeDatoDeContactoEnum;
import com.company.test1.entity.extroles.ComercialOfertas;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ArchivosAdjuntos;
import com.company.test1.jmx.importadores.CuentasBancarias;
import com.company.test1.jmx.importadores.conceptosadicionales.ConceptosAdicionales;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Personas {

    ArrayList entitiesToPersist = new ArrayList();

    public void realizaImportaciones(DataManager dataManager, Persistence persistence) throws Exception{
        String sql = "select * from personas";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Integer id = r.getInt("id");
            Persona p = realizaImportacionPersona(id, persistence, entitiesToPersist);

            dataManager.commit(new CommitContext(entitiesToPersist));
            entitiesToPersist.clear();
        }
        r.close();

    }

    public void realizaImportacionRepresentacionesLegales(DataManager dm, Persistence persistence) throws Exception{
        String sql = "select * from representaciones_legales_personas";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Persona representado = devuelvePersonaDesdeSofiaId(r.getInt("persona_id"), persistence);
            Persona representante = devuelvePersonaDesdeSofiaId(r.getInt("persona_representante_id"), persistence);
            String enCalidadDe = r.getString("enCalidadDe");
            RepresentacionLegal rl = new RepresentacionLegal();
            rl.setPersona(representado);
            rl.setPersona_representante(representante);
            if (enCalidadDe.compareTo("Administrador")==0){
                rl.setEnCalidadDe(RepresentacionEnCalidadDe.ADMINISTRADOR);
            }
            if (enCalidadDe.compareTo("Representante")==0){
                rl.setEnCalidadDe(RepresentacionEnCalidadDe.REPRESENTANTE);
            }
            if (enCalidadDe.compareTo("Administradora Solidaria")==0){
                rl.setEnCalidadDe(RepresentacionEnCalidadDe.ADMINISTRADOR_SOLIDARIO);
            }
            dm.commit(rl);

        }
    }

    public Persona realizaImportacionPersona(Integer personaId, Persistence persistence, List entitiesToPersist) throws Exception{
        String sqlf = "SELECT * FROM personas p inner join personas_fisicas pf on p.id = pf.id WHERE p.id = " + personaId;
        String sqlj = "SELECT * FROM personas p inner join personas_juridicas pj on p.id = pj.id WHERE p.id = " + personaId;
        int npf = Rentamaster2DB.getNumberOfRecords(sqlf, Rentamaster2DB.getConnection());
        int npj = Rentamaster2DB.getNumberOfRecords(sqlj, Rentamaster2DB.getConnection());
        Persona p = null;
        if (npf==1){
            PersonaFisica pf = new PersonaFisica();
            p = pf;
            ResultSet r = Rentamaster2DB.getResultSet(sqlf, Rentamaster2DB.getConnection());
            while(r.next()){
                pf.setNifDni(r.getString("nifDni"));
                pf.setNombre(r.getString("nombre"));
                pf.setNombreCompleto(r.getString("nombreCompleto"));
                pf.setRm2id(r.getInt("id"));
                pf.setApellido1(r.getString("apellido1"));
                pf.setApellido2(r.getString("apellido2"));
                pf.setFechaNacimiento(r.getDate("fechaNacimiento"));
                pf.setEstadoCivil(EstadoCivilEnum.fromId(r.getString("estadoCivil")));
            }
            r.close();
        }

        if (npj==1){
            PersonaJuridica pj = new PersonaJuridica();
            p = pj;
            ResultSet r = Rentamaster2DB.getResultSet(sqlj, Rentamaster2DB.getConnection());
            while(r.next()){
                pj.setNifDni(r.getString("nifDni"));
                pj.setNombre(r.getString("nombre"));
                pj.setNombreCompleto(r.getString("nombreCompleto"));
                pj.setRm2id(r.getInt("id"));
                pj.setDescripcionActividad(r.getString("descripcionActividad"));
                pj.setRazonSocial(r.getString("razonSocial"));
            }
            r.close();
        }
        entitiesToPersist.add(p);
        //direcciones
        String sqld = "select * from direcciones where persona_id = " + p.getRm2id();
        ResultSet rd = Rentamaster2DB.getResultSet(sqld);
        while(rd.next()){
            Direccion d = new Direccion();
            entitiesToPersist.add(d);
            p.getDirecciones().add(d);
            d.setPersona(p);
            d.setCodigoPostal(rd.getString("codigoPostal"));
            d.setDireccionCompleta(rd.getString("direccionCompleta"));
            d.setEscalera(rd.getString("escalera"));
            String nombreDireccion = rd.getString("nombre");
            if (nombreDireccion!=null) {
                if (nombreDireccion.compareTo("Domicilio Administrador") == 0) {
                    d.setNombre(NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId());
                } else if (nombreDireccion.compareTo("Domicilio Inquilino") == 0) {
                    d.setNombre(NombreTipoDireccion.DOMICILIO_INQUILINO.getId());
                } else if (nombreDireccion.compareTo("Domicilio Social") == 0) {
                    d.setNombre(NombreTipoDireccion.DOMICILIO_SOCIAL.getId());
                } else {
                    d.setNombre(rd.getString("nombre"));
                }
            }
            d.setNombreVia(rd.getString("nombreVia"));
            d.setNumeroVia(rd.getString("numeroVia"));
            d.setObservaciones(rd.getString("observaciones"));
            d.setPais(rd.getString("pais"));
            d.setPiso(rd.getString("piso"));
            d.setPoblacion(rd.getString("poblacion"));
            d.setProvincia(rd.getString("provincia"));
            d.setPuerta(rd.getString("puerta"));
            d.setRegion(rd.getString("region"));
            d.setVia(rd.getString("via"));
            d.setPersona(p);
            d.setEnviarCorrespondenciaAEstaDireccion(rd.getBoolean("enviarCorrespondenciaAEstaDireccion"));


        }

        rd.close();
        //datos de contacto
        sqld = "select * from datos_de_contacto where persona_id = " + p.getRm2id();
        rd = Rentamaster2DB.getResultSet(sqld);
        while(rd.next()){
            DatoDeContacto ddc = new DatoDeContacto();
            entitiesToPersist.add(ddc);
            ddc.setPersona(p);
            p.getDatosDeContacto().add(ddc);
            ddc.setDato(rd.getString("dato"));
            ddc.setDescripcionDato(rd.getString("descripcionDato"));
            if (rd.getString("tipoDeDato").compareTo("Móvil")==0){
                ddc.setTipoDeDato(TipoDeDatoDeContactoEnum.MOBIL);
            }
            if (rd.getString("tipoDeDato").compareTo("Teléfono Fijo")==0){
                ddc.setTipoDeDato(TipoDeDatoDeContactoEnum.TELEFONO);
            }
            if (rd.getString("tipoDeDato").compareTo("Email")==0){
                ddc.setTipoDeDato(TipoDeDatoDeContactoEnum.CORREO_ELECTRONICO);
            }
            if (rd.getString("tipoDeDato").compareTo("Otro")==0){
                ddc.setTipoDeDato(TipoDeDatoDeContactoEnum.OTRO);
            }

        }
        rd.close();

        //Cuentas Bancarias
        CuentasBancarias ccbb  = new CuentasBancarias();
        ccbb.setPersona(p);
        ccbb.setProveedor(p.getProveedor());
        String sqlcb = "select * from cuentas_bancarias where persona_id = " + p.getRm2id();
        ResultSet rcb = Rentamaster2DB.getResultSet(sqlcb);
        while(rcb.next()){
            CuentaBancaria cb = ccbb.realizaImportacion(rcb.getInt("id"), persistence);
            entitiesToPersist.add(cb);
            p.getCuentasBancarias().add(cb);
        }
        rcb.close();


        String sqlprov = "SELECT * FROM proveedores WHERE persona_id = "+ personaId;
        ResultSet r = Rentamaster2DB.getResultSet(sqlprov, Rentamaster2DB.getConnection());
        while(r.next()){
            Proveedor prov = new Proveedor();
            entitiesToPersist.add(prov);
            prov.setRm2id(r.getInt("id"));
            prov.setDescripcionActividad(r.getString("descripcionActividad"));
            prov.setObservaciones(r.getString("observaciones"));
            prov.setPersona(p);
            p.setProveedor(prov);
            prov.setNombreComercial(r.getString("nombreComercial"));
            prov.setEnviarCorreoConfirmacionAlAprobarFactura(r.getBoolean("enviarCorreoConfirmacionAlAprobarFactura"));

            //debido a que existen en origen 2 entornos como minimo, recorro el resultset y luego consolido para nuevo modelo sin entornos
            prov.setModoDePago(null);
            prov.setCuentaBancaria(null);
            String sqlprovmp = "select * from proveedores_modos_pagos where proveedor_id = " + prov.getRm2id();
            ResultSet rmp = Rentamaster2DB.getResultSet(sqlprovmp);
            ArrayList<String> modosPagos = new ArrayList();
            ArrayList<CuentaBancaria> ccbbDom = new ArrayList<>();
            ArrayList<CuentaBancaria> ccbbTel = new ArrayList<>();
            while(rmp.next()){
                String mp = rmp.getString("modoPago").trim();
                String[] mmpp = mp.split(" ");
                for (int i = 0; i < mmpp.length; i++) {
                    String mp_ = mmpp[i];
                    modosPagos.add(mp.trim());
                }

                CuentaBancaria cbdom = getCuentaBancariaDeObjectGraph(rmp.getInt("domiciliacion_cuenta_bancaria_id"), p);
                CuentaBancaria cbtel = getCuentaBancariaDeObjectGraph(rmp.getInt("proveedor_cuenta_bancaria_id"), p);
                if (cbdom!=null)
                    ccbbDom.add(cbdom);
                if (cbtel!=null)
                    ccbbTel.add(cbtel);

            }
            if (modosPagos.indexOf("PAGO TELEMÁTICO")!=-1){
                prov.setModoDePagoTelematico(true);
                if (ccbbTel.size()>0)
                    prov.setCuentaBancaria(ccbbTel.get(ccbbTel.size()-1));
            }else{
                prov.setModoDePagoTelematico(false);
                prov.setCuentaBancaria(null);
            }
            if (modosPagos.indexOf("PAGO DOMICILIADO")!=-1){
                prov.setModoDePagoDomiciliado(true);
                if (ccbbDom.size()>0)
                    prov.setCuentaBancariaDomiciliado(ccbbDom.get(ccbbDom.size()-1));
            }else{
                prov.setModoDePagoDomiciliado(false);
                prov.setCuentaBancariaDomiciliado(null);
            }
            //fin modos de pago

            //comercial ofertas
            Integer coid = r.getInt("comercial_ofertas_id");
            String sqlco = "SELECT * FROM comerciales_ofertas WHERE id = " + coid;
            ResultSet rco = Rentamaster2DB.getResultSet(sqlco, Rentamaster2DB.getConnection());
            while(rco.next()){
                ComercialOfertas co = new ComercialOfertas();
                entitiesToPersist.add(co);
                prov.setComercialOfertas(co);
                co.setProveedor(prov);
                co.setDetalleCorreosElectronicos(rco.getString("detalleCorreosElectronicos"));
                co.setDetalleNombres(rco.getString("detalleNombres"));
                Integer escaneoAceptacionArchivoAdjunto = rco.getInt("escaneo_aceptacion_archivo_adjunto_id");
                ArchivoAdjunto eaaa = new ArchivosAdjuntos().realizaImportacion(escaneoAceptacionArchivoAdjunto, null, entitiesToPersist);
                co.setEscaneoAceptacion(eaaa);
                co.setExcluirEnvios(rco.getBoolean("excluirEnvios"));
                co.setPuedeDarAltaOBajaAgentes(rco.getBoolean("puedeDarAltaOBajaAgentes"));
                co.setXmlComercialVisitas(rco.getString("xmlComercialVisitas"));

            }
            rco.close();

            //programaciones_concceptos adicionales
            String sql = "select * from programaciones_conceptos_adicionales where proveedor_id = " + prov.getRm2id();
            ResultSet rpca = Rentamaster2DB.getResultSet(sql);
            while(rpca.next()){
                ProgramacionConceptoAdicional pca = new ProgramacionConceptoAdicional();
                entitiesToPersist.add(pca);
                ConceptoAdicional ca = new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(rpca.getInt("concepto_adicional_id"), persistence);
                pca.setConceptoAdicional(ca);
                pca.setProgramacionRecibo(null);
                pca.setProveedor(prov);
            }
        }
        r.close();
        String sqlprop = "SELECT * FROM propietarios WHERE persona_id = " + personaId;
        r = Rentamaster2DB.getResultSet(sqlprop, Rentamaster2DB.getConnection());
        while(r.next()){
            Propietario pr = new Propietario();
            entitiesToPersist.add(pr);
            pr.setRm2id(r.getInt("id"));
            pr.setAbreviacionContratos(r.getString("abreviacionContratos"));
            pr.setPersona(p);
            p.setPropietario(pr);
            pr.setCodigoCliente(r.getString("codigoCliente"));
            pr.setExoneracionIrpf(r.getBoolean("exoneracionIrpf"));
            pr.setGestionCaja(r.getBoolean("gestionCaja"));
            ArchivoAdjunto cabecera = new ArchivosAdjuntos().realizaImportacion(r.getInt("cabecera_documentos_archivo_adjunto_id"), null, entitiesToPersist);
            pr.setCabeceraDocumentosArchivoAdjunto(cabecera);
            CuentaBancaria cb = new CuentasBancarias().realizaImportacion(r.getInt("cuenta_bancaria_id"), persistence);
            entitiesToPersist.add(cb);
            pr.setCuentaBancaria(cb);
            pr.setProspecto(r.getBoolean("prospecto"));
            pr.setPersona(p);
            p.setPropietario(pr);

        }
        r.close();



        return p;

    }


    public Propietario devuelvePropietarioDesdeSofiaId(Integer sofiaPropietarioId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT p FROM test1_Propietario p WHERE p.rm2id = " + sofiaPropietarioId;
        Propietario p = (Propietario) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

    public Proveedor devuelveProveedorDesdeSofiaId(Integer sofiaPropietarioId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT p FROM test1_Proveedor p WHERE p.rm2id = " + sofiaPropietarioId;
        Proveedor p = (Proveedor) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

    public Persona devuelvePersonaDesdeSofiaId(Integer sofiaPersonaId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT p FROM test1_Persona p WHERE p.rm2id = " + sofiaPersonaId;
        Persona p = (Persona) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

    public ComercialOfertas devuelveComercialOfertasDesdeSofiaId(Integer sofiaComercialId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT co FROM test1_ComercialOfertas co WHERE co.rm2id = " + sofiaComercialId;
        ComercialOfertas p = (ComercialOfertas) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

    public String convertModoDePagoId(String in){
        String[] ins = new String[]{"PAGO TALÓN","PAGO DOMICILIADO","PAGO TELEMÁTICO"};
        String[] outs = new String[]{"TALON", "PAGO_DOMICILIADO", "PAGO_TELEMATICO"};
        List insal = Arrays.asList(ins);
        List outsal = Arrays.asList(outs);
        return (String) outsal.get(insal.indexOf(in));
    }

    public CuentaBancaria getCuentaBancariaDeObjectGraph(Integer rm2id, Persona p ){
        for (int i = 0; i < p.getCuentasBancarias().size(); i++) {
            CuentaBancaria cb = p.getCuentasBancarias().get(i);
            if (cb.getRm2id().intValue()==rm2id.intValue()){
                return cb;
            }
        }
        return null;
    }

}
