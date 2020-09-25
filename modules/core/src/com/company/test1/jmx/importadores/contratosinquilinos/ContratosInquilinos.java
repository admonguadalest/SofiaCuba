package com.company.test1.jmx.importadores.contratosinquilinos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.*;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.enums.*;
import com.company.test1.entity.enums.recibos.ConceptoReciboEstadoNotificacion;
import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.incrementos.IncrementoGeneralObras;
import com.company.test1.entity.incrementos.IncrementoIndice;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoAdicionalConceptoRecibo;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ArchivosAdjuntos;
import com.company.test1.jmx.importadores.ColeccionesAdjuntos;
import com.company.test1.jmx.importadores.CuentasBancarias;
import com.company.test1.jmx.importadores.ciclos.CDFs;
import com.company.test1.jmx.importadores.ciclos.Ciclos;
import com.company.test1.jmx.importadores.conceptosadicionales.ConceptosAdicionales;
import com.company.test1.jmx.importadores.definicionesremesas.DefinicionesRemesas;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.company.test1.jmx.importadores.plantillas.Plantillas;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Departamentos;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Ubicaciones;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.ArrayList;


public class ContratosInquilinos {



    ArrayList entitiesToPersist = new ArrayList();

    public void realizaImportaciones(DataManager dataManager, Persistence persistence) throws Exception{

        String sql = "select * from contratos_inquilinos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){

            entitiesToPersist.clear();
            Integer id = r.getInt("id");

            ContratoInquilino ci = realizaImportacionContrato(id, persistence);


            dataManager.commit(new CommitContext(entitiesToPersist));

        }
        r.close();


    }

    public void realizaAjusteConceptoReciboAtrasos(Persistence persistence) throws Exception{
        //en esta parte asignamos los conceptos recibo atrasos en los incrementos
        String sql = "select * from incrementos i inner join conceptos_recibos cr on i.conceptoRecibo_id = cr.id inner join incrementos_indice ii on ii.id = i.id WHERE i.conceptoReciboAtrasos_id is not null";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Integer cridatrasos = r.getInt("conceptoReciboAtrasos_id");
            ConceptoRecibo cr = devuelveConceptoRecibooDesdeSofiaId(cridatrasos, persistence);
            IncrementoIndice ii = devuelveIncrementoIndiceReferenciaDesdeSofiaId(r.getInt("id"), persistence);
            ii.setConceptoReciboAtrasos(cr);
            try {
                Transaction t = persistence.createTransaction();
                persistence.getEntityManager().persist(ii);
                t.commit();
            }catch(Exception ex){
                int y = 2;
            }
        }
        r.close();

        sql = "select i.id as iid, i.* from incrementos i inner join conceptos_recibos cr on i.conceptoRecibo_id = cr.id inner join incrementos_general_obras igo on igo.id = i.id WHERE i.conceptoReciboAtrasos_id is not null";
        r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Integer cridatrasos = r.getInt("conceptoReciboAtrasos_id");
            ConceptoRecibo cr = devuelveConceptoRecibooDesdeSofiaId(cridatrasos, persistence);
            IncrementoGeneralObras igo = devuelveIncrementoGeneralObrasDesdeSofiaId(r.getInt("id"), persistence);
            igo.setConceptoReciboAtrasos(cr);
            try {
                Transaction t = persistence.createTransaction();
                persistence.getEntityManager().merge(igo);
                t.commit();
            }catch(Exception ex){
                int y = 2;
            }
        }
        r.close();
    }

    public ContratoInquilino realizaImportacionContrato(Integer ciid, Persistence persistence) throws Exception{
        ContratoInquilino ci = new ContratoInquilino();

        entitiesToPersist.add(ci);


        String sql = "SELECT * FROM contratos_inquilinos WHERE id = " + ciid;
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            ci.setRm2id(r.getInt("id"));
            ci.setArrendatarioSinRepresentacion(r.getBoolean("arrendatarioSinRepresentacion"));
            ci.setEstadoContrato(EstadoContratoInquilinoEnum.fromId(r.getInt("estadoContrato")));
            ci.setFechaDesocupacion(r.getDate("fechaDesocupacion"));
            ci.setFechaOcupacion(r.getDate("fechaOcupacion"));
            ci.setFechaRealizacion(r.getDate("fechaRealizacion"));
            ci.setFechaVencimientoPrevisto(r.getDate("fechaVencimientoPrevisto"));
            ci.setFormaDeCobro(FormaDeCobroContratoInquilinoEnum.fromId(r.getInt("formaDeCobro")));
            ci.setLugarRealizacion(r.getString("lugarRealizacion"));
            ci.setMesAnyoAplicacionIPC(r.getString("mesAnyoAplicacionIPC"));
            ci.setNumeroContrato(r.getString("numeroContrato"));
            ci.setOmitirIPCNegativo(r.getBoolean("omitirIPCNegativo"));
            ci.setPeriodoActualizacionIPC(r.getInt("periodoActualizacionIPC"));
            ci.setPlazoAnyos(r.getInt("plazoAnyos"));
            ci.setPlazoAnyosProrrogable(r.getInt("plazoAnyosProrrogable"));
            ci.setRentaContractual(r.getDouble("rentaContractual"));
            if (r.getInt("tipoContrato")==1){
                ci.setTipoContrato(TipoContratoInquilinoEnum.ANTIGUA_LAU);
            }else{
                ci.setTipoContrato(TipoContratoInquilinoEnum.NUEVA_LAU);
            }

            ci.setUsoContrato(UsoContratoEnum.fromId(r.getInt("usoContrato")));
            int coleccion_adjuntos_id = r.getInt("coleccion_adjuntos_id");
            ColeccionArchivosAdjuntos ca = new ColeccionesAdjuntos().realizaImportacionColeccion(coleccion_adjuntos_id, null, entitiesToPersist);
            ci.setColeccionArchivosAdjuntos(ca);

            entitiesToPersist.add(ca);

            ci.setDepartamento(new Departamentos().getDepartamentoFromSofiaId(r.getInt("departamento_id"), persistence));
            int escaneoContratoAA = r.getInt("escaneo_contrato_archivo_adjunto_id");
            ArchivoAdjunto aa = new ArchivosAdjuntos().realizaImportacion(escaneoContratoAA, null, entitiesToPersist);
            ci.setEscaneoContrato(aa);



            ImplementacionModelo im = realizaImportacionImplementacionModelo(r.getInt("implementacion_modelo_id"), ci, persistence);
            ci.setImplementacionModelo(im);


            ci.setInquilino(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("inquilino_persona_id"), persistence));
            ci.setRepresentanteArrendador(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("representante_arrendador_persona_id"), persistence));
            ci.setRepresentanteArrendatario(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("representante_arrendataria_persona_id"), persistence));
            ci.setDetalleEntregaLlaves(r.getString("detalleEntregaLlaves"));
            ci.setPagador(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("pagador_persona_id"), persistence));
            ci.setNombreTipoIndiceIncrementos(r.getString("nombreTipoIndiceIncrementos"));
            CarpetaDocumentosFotograficos cdff = new CDFs().devuelveCarpetaDesdeRm2Id(r.getInt("carpeta_documento_fotografico_firma_id"));
            ci.setCarpetaDocumentoFotograficoFirma(cdff);

            ci.setElPagadorEsElTitular(r.getBoolean("elPagadorEsElTitular"));
            ci.setPermitirApuntesProcesosAgregados(r.getBoolean("permitirApuntesProcesosAgregados"));
            ci.setComercialOfertas(new Personas().devuelveComercialOfertasDesdeSofiaId(r.getInt("comercial_ofertas_id"), persistence));
            ci.setExcluirDeMonitorizacionDeRecibosPendientesYDevueltos(r.getBoolean("excluirDeMonitorizacionDeRecibosPendientesYDevueltos"));
            ci.setEsteContratoDevengaCostesPostales(r.getBoolean("esteContratoDevengaCostesPostales"));
            ci.setRecibosIncobrables(r.getBoolean("recibosIncobrables"));
            ci.setFechaMandato(r.getDate("fechaMandato"));
            ci.setReferenciaMandato(r.getString("referenciaMandato"));
            ci.setComunicacionRenuncia(r.getBoolean("comunicacionRenuncia"));
            ci.setFechaPrevistaRenuncia(r.getDate("fechaPrevistaRenuncia"));
            ci.setFechaComunicacion(r.getDate("fechaComunicacion"));
            ArchivoAdjunto renuncia = new ArchivosAdjuntos().realizaImportacion(r.getInt("escaneo_renuncia_archivo_adjunto_id"), null, entitiesToPersist);
            ci.setArchivoAdjuntoRenuncia(renuncia);


            ci.setObservacionesRenuncia(r.getString("observacionesRenuncia"));
            ci.setComentariosAdministrador(r.getString("comentariosAdministrador"));

            //fianza
            String sqlf = "SELECT * from fianzas WHERE contrato_inquilino_id = " + ci.getRm2id();
            ResultSet rsf = Rentamaster2DB.getResultSet(sqlf);
            while(rsf.next()){
                Fianza f = new Fianza();

                entitiesToPersist.add(f);

                f.setEsAvalBancario(rsf.getBoolean("esAvalBancario"));
                f.setFechaAbonoFianza(rsf.getDate("fechaAbonoFianza"));
                f.setFianzaComplementaria(rsf.getDouble("fianzaComplementaria"));
                f.setFianzaLegal(rsf.getDouble("fianzaLegal"));
                f.setIdentificadorAval(rsf.getString("identificadorAval"));
                f.setObservaciones(rsf.getString("observaciones"));
                ArchivoAdjunto escaneoAval = new ArchivosAdjuntos().realizaImportacion(rsf.getInt("escaneo_aval_archivo_adjunto_id"), null, entitiesToPersist);

                if (escaneoAval!=null)
                    entitiesToPersist.add(escaneoAval);


                f.setEscaneoArchivoAdjunto(escaneoAval);

                f.setContratoInquilino(ci);
                ci.setFianza(f);
                f.setEstadoFianza(EstadoFianzaEnum.fromId(rsf.getInt("estadoFianza")));
                f.setTienePolizaAlquiler(rsf.getBoolean("tienePolizaAlquiler"));
                f.setNumeroPoliza(rsf.getString("numeroPoliza"));
                f.setInformacionDeContactoPoliza(rsf.getString("informaciondeContactoPoliza"));
                ArchivoAdjunto escaneoSeguro = new ArchivosAdjuntos().realizaImportacion(rsf.getInt("escaneo_seguro_adjunto_id"), null, entitiesToPersist);
                f.setEscaneoSeguroArchivoAdjunto(escaneoSeguro);



                f.setFechaIngresoFianzaEnCamara(rsf.getDate("fechaIngresoFianzaEnCamara"));
                f.setFechaRescateFianzaDeCamara(rsf.getDate("fechaRescateFianzaDeCamara"));
                ArchivoAdjunto escaneoFianza = new ArchivosAdjuntos().realizaImportacion(rsf.getInt("escaneo_fianza_archivo_adjunto_id"), null, entitiesToPersist);
                f.setEscaneoFianza(escaneoFianza);






                ci.setFianza(f);
            }
            rsf.close();


            //se deja para mas tarde cuando ambas entidades esten cargadas
//            ci.setCiclo(new Ciclos().devuelveCicloDesdeSofiaId(r.getInt("ciclo_id"), persistence));

            //programacion recibo
            ProgramacionRecibo pr = new ProgramacionRecibo();

            entitiesToPersist.add(pr);

            String sqlpr = "select * from programaciones_recibos where contrato_inquilino_id = " + ci.getRm2id();
            ResultSet rpr = Rentamaster2DB.getResultSet(sqlpr);
            while(rpr.next()){
                pr.setRm2id(rpr.getInt("id"));
                pr.setPropietarioEsEmisor(rpr.getBoolean("propietarioEsEmisor"));
                pr.setTipoCobro(TipoCobroProgramacionReciboEnum.fromId(rpr.getInt("tipoCobro")));
                pr.setContratoInquilino(ci);
                ci.setProgramacionRecibo(pr);
                pr.setCuentaBancariaInquilino(new CuentasBancarias().devuelveCuentaBancariaDesdeSofiaId(rpr.getInt("cuenta_bancaria_inquilino_id"), persistence));
                pr.setDefinicionRemesa(new DefinicionesRemesas().devuelveDefinicionRemesaDesdeSofiaId(rpr.getInt("definicion_remesa_defecto_id"), persistence));
                pr.setDesactivarProgramacion(rpr.getBoolean("desactivarProgramacion"));
                pr.setCuentaBancariaPagador(new CuentasBancarias().devuelveCuentaBancariaDesdeSofiaId(rpr.getInt("cuenta_bancaria_pagador_id"), persistence));
                pr.setAplicarIPCNegativo(rpr.getBoolean("aplicarIPCNegativo"));
                pr.setProrrateoProximaEmision(rpr.getDouble("prorrateoProximaEmision"));
                //conceptosrecibos
                String sqlcr = "select * from conceptos_recibos where programacion_recibo_id = " + rpr.getInt("id");
                ResultSet rcr = Rentamaster2DB.getResultSet(sqlcr);
                while(rcr.next()){
                    Concepto c = new Conceptos().devuelveConceptoDesdeIdSofia(rcr.getInt("concepto_id"), persistence);
                    ConceptoRecibo cr = new ConceptoRecibo();

                    entitiesToPersist.add(cr);

                    cr.setRm2id(rcr.getInt("id"));
                    cr.setActivadoDesactivado(rcr.getBoolean("activadoDesactivado"));
                    cr.setDescripcionCausa(rcr.getString("descripcionCausa"));
                    cr.setEsModificacionAgregada(rcr.getBoolean("esModificacionAgregada"));
                    cr.setFechaDesde(rcr.getDate("fechaDesde"));
                    cr.setFechaHasta(rcr.getDate("fechaHasta"));
                    cr.setImporte(rcr.getDouble("importe"));
                    cr.setNumEmisiones(rcr.getInt("numEmisiones"));
                    cr.setVigencia(ConceptoReciboVigenciaEnum.fromId(rcr.getInt("vigencia")));
                    cr.setConcepto(c);
                    cr.setProgramacionRecibo(pr);
                    pr.getConceptosRecibo().add(cr);
                    cr.setFechaValor(rcr.getDate("fechaValor"));
                    cr.setEstadoNotificacion(ConceptoReciboEstadoNotificacion.fromId(rcr.getInt("estadoNotificacion")));
                    cr.setActualizableIPC(rcr.getBoolean("actualizableIPC"));
                    cr.setOmitirEnProrrateo(rcr.getBoolean("omitirEnProrrateo"));
                    //conceptos adicionales
                    String sqlcacr = "select * from conceptos_adicionales_conceptos_recibo where concepto_recibo_id = " + rcr.getInt("id");
                    ResultSet rcacr = Rentamaster2DB.getResultSet(sqlcacr);
                    while(rcacr.next()){
                        ConceptoAdicionalConceptoRecibo cacr = new ConceptoAdicionalConceptoRecibo();

                        entitiesToPersist.add(cacr);

                        cacr.setConceptoRecibo(cr);
                        cr.getConceptosAdicionalesConceptoRecibo().add(cacr);
                        ConceptoAdicional cad = new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(rcacr.getInt("concepto_adicional_id"), persistence);
                        cacr.setConceptoAdicional(cad);
                        cacr.setPorcentaje(rcacr.getDouble("porcentaje"));
                    }
                    rcacr.close();

                    //incrementos indices
                    String sqlii = "select i.id as iid, i.*, ii.* from incrementos i inner join conceptos_recibos cr on i.conceptoRecibo_id = cr.id inner join incrementos_indice ii on ii.id = i.id WHERE cr.id = " + cr.getRm2id();
                    ResultSet rsii = Rentamaster2DB.getResultSet(sqlii);
                    while(rsii.next()){
                        com.company.test1.entity.incrementos.IncrementoIndice ii = new com.company.test1.entity.incrementos.IncrementoIndice();

                        entitiesToPersist.add(ii);


                        ii.setMesesAtrasos(rsii.getInt("mesesAtrasos"));
                        ii.setIndiceAnterior(rsii.getDouble("indiceAnterior"));
                        ii.setMesAnnoIndice(rsii.getString("mesAnoIndice"));
                        ii.setNombreTipo(rsii.getString("nombreTipo"));
                        ii.setValorBase(rsii.getDouble("valorBase"));
                        ii.setValorIndice(rsii.getDouble("valorIndice"));
                        ii.setValorIndicePorcentual(rsii.getDouble("valorIndicePorcentual"));
                        ii.setConceptoRecibo(cr);
                        ii.setConceptoReciboAtrasos(null);
                        ii.setFechaIncremento(rsii.getDate("fechaIncremento"));
                        ii.setImporte(rsii.getDouble("importe"));
                        ii.setImporteAtrasos(null);//pendiente atrasos actualizarlos a posteriori cuando todos los conceptos recibos se han creado
                        ii.setMesesRepercusionAtrasos(rsii.getInt("mesesRepercusionesAtrasos"));
                        ii.setRm2id(rsii.getInt("iid"));
                        cr.setIncremento(ii);
                    }
                    rsii.close();
                    //incrementos obras-generales
                    String sqliog = "select igo.id as igoid, i.*, igo.* from incrementos i inner join conceptos_recibos cr on i.conceptoRecibo_id = cr.id inner join incrementos_general_obras igo on igo.id = i.id WHERE cr.id = " + cr.getRm2id();
                    ResultSet rsigo = Rentamaster2DB.getResultSet(sqliog);
                    while(rsigo.next()){
                        IncrementoGeneralObras igo = new IncrementoGeneralObras();

                        entitiesToPersist.add(igo);

                        igo.setFechaIncremento(rsigo.getDate("fechaIncremento"));
                        igo.setImporte(rsigo.getDouble("importe"));
                        igo.setMesesAtrasos(rsigo.getInt("mesesAtrasos"));
                        igo.setImporteAtrasos(null);
                        igo.setConceptoRecibo(cr);
                        igo.setConceptoReciboAtrasos(null);//pendiente atrasos actualizarlos a posteriori cuando todos los conceptos recibos se han creado
                        igo.setMesesRepercusionAtrasos(rsigo.getInt("mesesRepercusionesAtrasos"));
                        try {
                            TipoCoeficiente tc = devuelveTipoCoeficienteDesdeSofiaId(rsigo.getInt("tipoCoeficiente_id"), persistence);
                            igo.setTipoCoeficiente(tc);
                        }catch(Exception exc){
                            //nada continuamos
                        }
                        igo.setValorCoeficiente(rsigo.getDouble("valorCoeficiente"));
                        igo.setImporteGlogalIncremento(rsigo.getDouble("importeGlobalIncremento"));
                        try {
                            igo.setConcepto(new Conceptos().devuelveConceptoDesdeIdSofia(rsigo.getInt("concepto_id"), persistence));
                        }catch(Exception exc){
                            //nada continuamos
                        }
                        igo.setModoReparticion(IncrementoGeneralObrasModoReparticion.fromId(rsigo.getInt("modoReparto")));
                        try {
                            igo.setUbicacion(new Ubicaciones().getUbicacionFromSofiaId(rsigo.getInt("ubicacion_id"), persistence));
                        }catch(Exception exc){
                            //nada continuamos
                        }
                        igo.setRm2id(rsigo.getInt("igoid"));
                        cr.setIncremento(igo);

                    }
                    rsigo.close();



                }
                rcr.close();

                //programacionecs_concceptos adicionales: reviar programacion_rrecibo id = 1799
                String sqlpca = "select * from programaciones_conceptos_adicionales where programacion_recibo_id = " + pr.getRm2id();
                ResultSet rpca = Rentamaster2DB.getResultSet(sqlpca);
                while(rpca.next()){
                    ProgramacionConceptoAdicional pca = new ProgramacionConceptoAdicional();
                    entitiesToPersist.add(pca);
                    ConceptoAdicional capr = new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(rpca.getInt("concepto_adicional_id"), persistence);
                    pca.setConceptoAdicional(capr);
                    pca.setProgramacionRecibo(pr);
                    pca.setProveedor(null);
                }


            }
            rpr.close();

            ci.setProgramacionRecibo(pr);

            //liquidacionsuscripcion
            LiquidacionSuscripcion ls = new LiquidacionSuscripcion();
            ci.setLiquidacionSuscripcion(ls);
            entitiesToPersist.add(ls);

            String sqlls = "select * from liquidaciones_suscripcion where contrato_inquilino_id = " + ci.getRm2id();
            ResultSet rls = Rentamaster2DB.getResultSet(sqlls);
            while(rls.next()){

                ls.setEsRenovacion(rls.getBoolean("esRenovacion"));
                ls.setFianzaContrato(rls.getDouble("fianzaContrato"));
                ls.setGarantiasEnEfectivo(rls.getDouble("garantiasEnEfectivo"));
                ls.setDescRecibosACuenta(rls.getString("descRecibosACuenta"));
                ls.setRecibosACuenta(rls.getDouble("recibosACuenta"));
                ls.setDescDevolucionFianzaContratoRenunciado(rls.getString("descDevolucionFianzaContratoRenunciado"));
                ls.setDevolucionFianzaContratoRenunciado(rls.getDouble("devolucionFianzaContratoRenunciado"));
                ls.setGastosContrato(rls.getDouble("gastosContrato"));
                ls.setTotalLiquidacion(rls.getDouble("totalLiquidacion"));
                ls.setDetalle(rls.getString("detalle"));
                ArchivoAdjunto escaneoIngresoLiquidacion = new ArchivosAdjuntos().realizaImportacion(rls.getInt("escaneo_ingreso_liquidacion_archivo_adjunto_id"), null, entitiesToPersist);



                ls.setEscaneoIngresoLiquidacion(escaneoIngresoLiquidacion);

                ls.setContratoInquilino(ci);

                ArchivoAdjunto escaneoArchivoAdjunto = new ArchivosAdjuntos().realizaImportacion(rls.getInt("escaneo_archivo_adjunto_id"), null, entitiesToPersist);



                ls.setEscaneoLiquidacion(escaneoArchivoAdjunto);

                ls.setOtrosConceptos(rls.getDouble("otrosConceptos"));
                ls.setCantidadesEntregadasEnReserva(rls.getDouble("cantidadesEntregadasEnReserva"));
                ls.setFechaIngresoITP(rls.getDate("fechaIngresoITP"));
                ArchivoAdjunto escaneoResguardoItp = new ArchivosAdjuntos().realizaImportacion(rls.getInt("escaneo_resguardo_itp_archivo_adjunto_id"), null, entitiesToPersist);



                ls.setEscaneoResguardoITP(escaneoResguardoItp);



            }
            rls.close();

            //liquidacion extincion
            LiquidacionExtincion le = new LiquidacionExtincion();
            entitiesToPersist.add(le);
            ci.setLiquidacionExtincion(le);


            String sqlle = "select * from liquidaciones_extincion where contrato_inquilino_id = " + ci.getRm2id();
            ResultSet rle = Rentamaster2DB.getResultSet(sqlle);
            while(rle.next()){

                le.setTotalesGarantias(rle.getDouble("totalesGarantias"));
                le.setTotalesRecibosPendientes(rle.getDouble("totalesRecibosPendientes"));
                le.setTotalesIndemnizaciones(rle.getDouble("totalesIndemnizaciones"));
                le.setPorLiquidar(rle.getDouble("porLiquidar"));
                ArchivoAdjunto escaneoLiquidacion = new ArchivosAdjuntos().realizaImportacion(rle.getInt("escaneo_liquidacion_archivo_adjunto_id"), null, entitiesToPersist);



                le.setEscaneoLiquidacion(escaneoLiquidacion);

                le.setContratoInquilino(ci);

                le.setDetalle(rle.getString("detalle"));
                le.setImporteAvalEjecutado(rle.getDouble("importeAvalEjecutado"));
                le.setCantidadesEntregadasEnReserva(rle.getDouble("cantidadesEntregadasEnReserva"));
                le.setConformidadAdministrador(rle.getBoolean("conformidadAdministrador"));
                CarpetaDocumentosFotograficos cdf = new CDFs().devuelveCarpetaDesdeRm2Id(rle.getInt("retencion_carpeta_documento_fotografico_id"));


                le.setRetencionCarpetaDocumentoFotografico(cdf);

                le.setCantidadesACuentaLiquidacion(rle.getDouble("cantidadesACuentaLiquidacion"));
                le.setFechaCantidadesACuentaLiquidacion(rle.getDate("fechaCantidadesACuentaLiquidacion"));
                le.setFechaLiquidacion(rle.getDate("fechaLiquidacion"));


            }
            rle.close();

            //subrogadores
            String sqls = "select * from subrogadores where contrato_inquilino_id = " + ci.getRm2id();
            ResultSet rss = Rentamaster2DB.getResultSet(sqls);
            while(rss.next()){
                Subrogador s = new Subrogador();

                entitiesToPersist.add(s);

                ci.getSubrogadores().add(s);
                s.setContratoInquilino(ci);
                s.setFechaDesde(rss.getDate("fechaDesde"));
                s.setFechaHasta(rss.getDate("fechaHasta"));
                s.setSubrogador(new Personas().devuelvePersonaDesdeSofiaId(rss.getInt("subrogador_persona_id"), persistence));
            }
            rss.close();

            //cotitulares
            String sqlc = "select * from cotitulares_contratos_inquilinos where contrato_inquilino_id = " + ci.getRm2id();
            ResultSet rct = Rentamaster2DB.getResultSet(sqlc);
            while(rct.next()){
                CotitularContratoInquilino cci = new CotitularContratoInquilino();

                entitiesToPersist.add(cci);

                cci.setContratoInquilino(ci);
                ci.getCotitulares().add(cci);
                cci.setCotitular(new Personas().devuelvePersonaDesdeSofiaId(rct.getInt("cotitular_persona_id"), persistence));
            }
            rct.close();

            //anexos
            String sqla = "select * from anexos where contrato_inquilino_id = " + ci.getRm2id();
            ResultSet ra = Rentamaster2DB.getResultSet(sqla);
            while(ra.next()){
                Anexo a = new Anexo();

                entitiesToPersist.add(a);

                a.setNombreAnexo(ra.getString("nombreAnexo"));
                a.setContratoInquilino(ci);
                ci.getAnexos().add(a);
                a.setPlantilla(new Plantillas().devuelvePlantillaDesdeSofiaId(ra.getInt("plantilla_id"), persistence));
                String sqlpv = "select * from parametros_valores_anexo where anexo_id = " + ra.getInt("id");
                ResultSet rpv = Rentamaster2DB.getResultSet(sqlpv);
                while(rpv.next()){
                    ParametroValorAnexo pva = new ParametroValorAnexo();

                    entitiesToPersist.add(pva);

                    pva.setAnexo(a);
                    a.getParametrosValores().add(pva);
                    pva.setNombreParametro(rpv.getString("nombreParametro"));
                    pva.setValor(rpv.getString("valor"));


                }
                rpv.close();

            }ra.close();



        }
        r.close();

        return ci;
    }

    public ProgramacionRecibo devuelveProgramacionReciboDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select pr FROM test1_ProgramacionRecibo pr WHERE pr.rm2id = " + id;


        ProgramacionRecibo pr = (ProgramacionRecibo) persistence.getEntityManager().createQuery(hql).getFirstResult();
        if (pr==null){
            int y = 2;
        }
        if (id!=0) {
            pr = persistence.getEntityManager().reload(pr, "programacionRecibo-viewCargaRecibos");
        }
        t.close();
        return pr;
    }

    public ContratoInquilino devuelveContratoInquilinoDesdeSofiaId(Integer id, Persistence persistence) throws Exception{

        Transaction t = persistence.createTransaction();
        String hql = "select ci FROM test1_ContratoInquilino ci WHERE ci.rm2id = " + id;
        ContratoInquilino ci = (ContratoInquilino) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return ci;
    }

    public ConceptoRecibo devuelveConceptoRecibooDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select cr FROM test1_ConceptoRecibo cr WHERE cr.rm2id = " + id;
        ConceptoRecibo ci = (ConceptoRecibo) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return ci;
    }

    public TipoCoeficiente devuelveTipoCoeficienteDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select tc FROM test1_TipoCoeficiente tc WHERE tc.rm2id = " + id;
        TipoCoeficiente tc  = (TipoCoeficiente) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return tc;
    }

    public IncrementoIndice devuelveIncrementoIndiceReferenciaDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select ii FROM test1_IncrementoIndice ii WHERE ii.rm2id = " + id;
        IncrementoIndice ii  = (IncrementoIndice) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return ii;
    }

    public IncrementoGeneralObras devuelveIncrementoGeneralObrasDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select igo FROM test1_IncrementoGeneralObras igo WHERE igo.rm2id = " + id;
        IncrementoGeneralObras igo  = (IncrementoGeneralObras) persistence.getEntityManager().createQuery(hql).getFirstResult();
        igo = persistence.getEntityManager().reload(igo, "incrementoGeneralObras-view");
        t.close();
        return igo;
    }


    public ImplementacionModelo realizaImportacionImplementacionModelo(Integer id, ContratoInquilino ci, Persistence persistence) throws Exception{
        String sql = "select * from implementaciones_modelos where id = " + id;
        Modelos modelos = new Modelos();
        ImplementacionModelo im = new ImplementacionModelo();

        entitiesToPersist.add(im);

        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            im.setRmi2(r.getInt("id"));
            ModeloContrato mc = new Modelos().getModeloDesdeSofiaId(r.getInt("modelo_contrato_id"), persistence);
            im.setModeloContrato(mc);
            im.setContratoInquilino(ci);

            String sqloc = "select * from overrides_clausulas where implementacion_modelo_id = " + im.getRmi2();
            ResultSet rsoc = Rentamaster2DB.getResultSet(sqloc);
            while(rsoc.next()){
                OverrideClausula oc = new OverrideClausula();

                entitiesToPersist.add(oc);

                oc.setClausula(modelos.getClausulaDesdeSofiaId(rsoc.getInt("clausula_id"), persistence));
                oc.setImplementacionModelo(im);
                oc.setVersionAplicada(modelos.getVersionClausulaDesdeSofiaId(rsoc.getInt("version_clausula_aplicada_id"), persistence));
                im.getOverrideClausulas().add(oc);
            }
            rsoc.close();

            String sqlsd = "select * from secciones_descartadas where implementacion_modelo_id = " + im.getRmi2();
            ResultSet rssd = Rentamaster2DB.getResultSet(sqlsd);
            while(rssd.next()){
                SeccionDescartada sd = new SeccionDescartada();

                entitiesToPersist.add(sd);

                sd.setImplementacionModelo(im);
                im.getSeccionesDescartadas().add(sd);
                sd.setSeccion(modelos.getSeccionDesdeSofiaId(rssd.getInt("seccion_id"), persistence));
            }
            rssd.close();

            String sqlppvv = "select * from parametros_valores where implementacion_modelo_id = " + im.getRmi2();
            ResultSet rspv = Rentamaster2DB.getResultSet(sqlppvv);
            while(rspv.next()){
                ParametroValor pv = new ParametroValor();

                entitiesToPersist.add(pv);

                pv.setImplementacionModelo(im);
                im.getParametrosValores().add(pv);
                pv.setNombreParametro(rspv.getString("nombreParametro"));
                pv.setValor(rspv.getString("valor"));
            }
            rssd.close();
        }
        r.close();
        return im;
    }



}
