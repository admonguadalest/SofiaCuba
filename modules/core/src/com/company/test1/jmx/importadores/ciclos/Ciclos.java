package com.company.test1.jmx.importadores.ciclos;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.ciclos.*;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.entity.enums.EstadoCicloEnum;
import com.company.test1.entity.enums.OrdenTrabajoTipoPrivacidadEnum;
import com.company.test1.entity.enums.TipoCiclo;
import com.company.test1.entity.enums.ValidacionEstado;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ColeccionesAdjuntos;
import com.company.test1.jmx.importadores.contratosinquilinos.ContratosInquilinos;
import com.company.test1.jmx.importadores.documentosimputables.FacturasProveedores;
import com.company.test1.jmx.importadores.documentosimputables.Presupuestos;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Departamentos;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ciclos {

    ArrayList entitiesToPersist = new ArrayList();


    public void realizaImportaciones(DataManager dataManager, Persistence persistence) throws Exception{
        String sql = "select * from ciclos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            entitiesToPersist.clear();
            Integer id = r.getInt("id");
            Ciclo c = realizaImportacion(id, persistence, entitiesToPersist);
            dataManager.commit(new CommitContext(entitiesToPersist));
        }
        r.close();
    }

    public Ciclo realizaImportacion(Integer cicloId, Persistence persistence, List entitiesToPersist) throws Exception{
        Ciclo c = new Ciclo();

        entitiesToPersist.add(c);

        String sql = "SELECT * FROM ciclos WHERE id = " + cicloId;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){
            c.setCodigoCiclo(r.getString("codigoCiclo"));
            c.setDescripcion(r.getString("descripcion"));
            c.setEsCicloCorriente(r.getBoolean("esCicloCorriente"));
            c.setEstadoCiclo(EstadoCicloEnum.fromId(r.getInt("estadoCiclo")));
            c.setFechaCiclo(r.getDate("fechaCiclo"));
            c.setObservaciones(r.getString("observaciones"));
            c.setTituloCiclo(r.getString("tituloCiclo"));
            Integer caid = r.getInt("coleccion_adjuntos_id");
            ColeccionArchivosAdjuntos caa = new ColeccionesAdjuntos().realizaImportacionColeccion(caid, null, entitiesToPersist);
            c.setColeccionAdjuntos(caa);
            Integer departamentoId = r.getInt("departamento_id");
            Departamento d = new Departamentos().getDepartamentoFromSofiaId(departamentoId, persistence);
            c.setDepartamento(d);
            c.setTipoCiclo(TipoCiclo.fromId(r.getString("tipoCiclo")));
            c.setExcluirDeMonitorizacionParaBusquedaOrdenesTrabajo(r.getBoolean("excluirDeMonitorizacionParaBusquedaOrdenesTrabajo"));
            //contrato inquilino
//            Integer ciid = r.getInt("contrato_inquilino_id");
//            ContratoInquilino ci = new ContratosInquilinos().realizaImportacionContrato(ciid, persistence);
//            c.setContratoInquilino(ci);
//            ci.setCiclo(c);
            //sacamos esta parte de aqui. Importare los contratos a parte y luego actualizare la asociacion
            //eventos
            String sqlevs = "SELECT * FROM eventos WHERE ciclo_id = " + cicloId;
            ResultSet revs = Rentamaster2DB.getResultSet(sqlevs, Rentamaster2DB.getConnection());
            while(revs.next()){
                Evento evento = new Evento();
                entitiesToPersist.add(evento);
                evento.setCiclo(c);
                c.getEventos().add(evento);
                evento.setNombre(revs.getString("nombre"));
                evento.setArchivado(revs.getBoolean("archivado"));
                evento.setFecha(revs.getDate("fecha"));
                evento.setRm2id(revs.getInt("id"));
            }
            revs.close();
            //importamos antes los documentos fotograficos para hacer disponibles las carpetas a las entradas
            //documentos fotograficos
            String sqldf = "SELECT * FROM carpetas_documentos_fotograficos WHERE ciclo_id = " + cicloId;
            ResultSet rcdf = Rentamaster2DB.getResultSet(sqldf, Rentamaster2DB.getConnection());
            while(rcdf.next()){
                CarpetaDocumentosFotograficos cdf = new CarpetaDocumentosFotograficos();
                entitiesToPersist.add(cdf);
                cdf.setRm2id(rcdf.getInt("id"));
                cdf.setCiclo(c);
                c.getCarpetasDocumentosFotograficos().add(cdf);
                cdf.setNombreCarpeta(rcdf.getString("nombreCarpeta"));
                cdf.setLiquidacionExtincion(null);
                cdf.setAccesibleParaComerciales(rcdf.getBoolean("accesibleParaComerciales"));
                cdf.setNumeroDeFotografias(rcdf.getInt("numeroDeFotografias"));
                cdf.setDescripcion(rcdf.getString("descripcion"));
                cdf.setEvento(devuelveEventoAsociado(c, rcdf.getInt("evento_id")));
                cdf.setAportadasPor(rcdf.getString("aportadasPor"));
                String sqlf = "SELECT * FROM fotos_documentos_fotograficos WHERE carpeta_documento_fotografico_id = " + cdf.getRm2id();
                ResultSet rfdf = Rentamaster2DB.getResultSet(sqlf, Rentamaster2DB.getConnection());
                while(rfdf.next()){
                    FotoDocumentoFotografico fdf = new FotoDocumentoFotografico();
                    entitiesToPersist.add(fdf);
                    fdf.setCarpeta(cdf);
                    cdf.getFotos().add(fdf);
                    fdf.setDescripcion(rfdf.getString("descripcion"));
                    fdf.setExtension(rfdf.getString("extension"));
                    fdf.setMimeType(rfdf.getString("mimeType"));
                    fdf.setNombreArchivo(rfdf.getString("nombreArchivo"));
                    fdf.setNombreArchivoOriginal(rfdf.getString("nombreArchivoOriginal"));
                    fdf.setTamano(rfdf.getInt("tamano"));
                    fdf.setExtId(rfdf.getInt("extId"));
                    fdf.setRepresentacionSerial(rfdf.getBytes("representacionSerial"));
                    Integer fotoDocumentoFotograficoId = rfdf.getInt("id");
                    String sqlfth = "SELECT * from fotos_thumbnail WHERE foto_documento_fotografico_id = " + fotoDocumentoFotograficoId + "";
                    ResultSet rfth = Rentamaster2DB.getResultSet(sqlfth);
                    while(rfth.next()){
                        FotoThumbnail fth = new FotoThumbnail();
                        entitiesToPersist.add(fth);
                        fdf.setFotoThumbnail(fth);
                        fth.setFotoDocumentoFotografico(fdf);
                        fth.setTamano(rfth.getInt("tamano"));
                        fth.setCarpetaDocumentoFotografico(cdf);
                        cdf.getFotosThumbnail().add(fth);
                        fth.setRepresentacionSerial(rfth.getBytes("representacionSerial"));
                        fth.setExtId(rfth.getInt("extId"));

                    }


                }
                rfdf.close();

            }
            rcdf.close();
            //entradas
            String sqle = "SELECT * FROM entradas WHERE ciclo_id = " + cicloId;
            ResultSet re = Rentamaster2DB.getResultSet(sqle, Rentamaster2DB.getConnection());
            while(re.next()){
                Entrada e = new Entrada();
                entitiesToPersist.add(e);
                e.setContenidoEntrada(re.getString("contenidoEntrada"));
                e.setFechaEntrada(re.getDate("fechaEntrada"));
                e.setCiclo(c);
                e.setRm2id(re.getInt("id"));
                c.getEntradas().add(e);
                e.setEnteroRecordatorio(re.getInt("enteroRecordatorio"));
                Integer evid = re.getInt("evento_id");
                Evento ev = devuelveEventoAsociado(c, evid);
                e.setEvento(ev);
                //ordenes de trabajo
                String sqlot = "SELECT * FROM ordenes_trabajo WHERE entrada_id = " + e.getRm2id().intValue();
                ResultSet rot = Rentamaster2DB.getResultSet(sqlot, Rentamaster2DB.getConnection());
                while(rot.next()){
                    OrdenTrabajo ot = new OrdenTrabajo();
                    entitiesToPersist.add(ot);
                    ot.setEntrada(e);
                    e.setOrdenTrabajo(ot);
                    ot.setRm2id(rot.getInt("id"));

                    CarpetaDocumentosFotograficos cdf = devuelveCDFDesdeSofiaId(rot.getInt("carpeta_documento_fotografico_id"), persistence);
                    ot.setCarpetaDocumentosFotograficos(cdf);
                    ot.setFechaSolicitud(rot.getDate("fechaSolicitud"));
                    ot.setFechaPrevistaIntervencion(rot.getDate("fechaPrevistaIntervencion"));
                    ot.setCosteOrientativo(rot.getString("costeOrientativo"));
                    ot.setPropuestoPor(rot.getString("propuestoPor"));
                    ot.setObservacion(rot.getString("observacion"));
                    ot.setDescripcion(rot.getString("descripcion"));
                    ot.setObservacionIntervencion(rot.getString("observacionIntervencion"));
                    ot.setDuracionPrevistaIntervencion(rot.getString("duracionPrevistaIntervencion"));
                    ot.setFranjaHorariaIntervencion(rot.getString("franjaHorariaIntervencion"));
                    ot.setFechaFinalizacion(rot.getDate("fechaFinalizacion"));
                    ot.setFechaFinalizacionEstimada(rot.getDate("fechaFinalizacionEstimada"));
                    ot.setExcluirDeMonitorizacionEncargado(rot.getBoolean("excluirDeMonitorizacionEncargado"));
                    ot.setTipoPrivacidad(OrdenTrabajoTipoPrivacidadEnum.fromId(rot.getInt("tipoPrivacidad")));
//                  EL CAMPO YA NO EXISTE EN LA BBDD
//                  ot.setReparacionRehabilitacion(rot.getBoolean("reparacionRehabilitacion"));
                    //notas de intervencion
                    String sqlnnii = "SELECT * FROM notas_intervenciones WHERE  orden_trabajo_id = " + ot.getRm2id().toString();
                    ResultSet rni = Rentamaster2DB.getResultSet(sqlnnii, Rentamaster2DB.getConnection());
                    while(rni.next()){
                        NotaIntervencion ni = new NotaIntervencion();
                        entitiesToPersist.add(ni);
                        ni.setFechaPrevistaIntervencion(rni.getDate("fechaPrevistaIntervencion"));
                        ni.setContenido(rni.getString("contenido"));
                        ni.setFranjaHoraria(rni.getString("franjaHoraria"));
                        if (rni.getDate("fechaIntervencion")==null){
                            ni.setFechaIntervencion(rni.getDate("fechaCreacion"));
                        }else{
                            ni.setFechaIntervencion(rni.getDate("fechaIntervencion"));
                        }

                        ni.setGradoExitoIntervencion(rni.getInt("gradoExitoIntervencion"));
                        ni.setOrdenTrabajo(ot);
                        ot.getNotasIntervenciones().add(ni);
                        ni.setEnvioNotificacionInquilino(rni.getBoolean("envioNotificacionInquilino"));
                        ni.setEnvioNotificacionPropietario(rni.getBoolean("envioNotificacionPopietario"));
                        ni.setAmpliacionNotificacionInquilino(rni.getString("ampliacionNotificacionInquilino"));
                        ni.setAmpliacionNotificacionPropietario(rni.getString("ampliacionNotificacionPropietario"));
                        ni.setEnviadoAInquilino(rni.getBoolean("enviadoAInquilino"));
                        ni.setEnviadoAPropietario(rni.getBoolean("enviadoAPopietario"));
                        ni.setCoordinarIntervencion(rni.getBoolean("coordinarIntervencion"));
                    }
                    rni.close();
                    //asignaciones tareas
                    String sqlaatt = "SELECT * FROM asignaciones_tareas WHERE  orden_trabajo_id = " + ot.getRm2id().toString();
                    ResultSet rat = Rentamaster2DB.getResultSet(sqlaatt, Rentamaster2DB.getConnection());
                    while(rat.next()){
                        AsignacionTarea at = new AsignacionTarea();
                        entitiesToPersist.add(at);
                        at.setProveedor(new Personas().devuelveProveedorDesdeSofiaId(rat.getInt("proveedor_id"), persistence));
                        at.setOrdenTrabajo(ot);
                        ot.getAsignacionesTareas().add(at);
                        at.setFechaPrevista(rat.getDate("fechaPrevista"));
                        at.setFechaFinalizacion(rat.getDate("fechaFinalizacion"));
                        at.setDescripcion(rat.getString("descripcion"));
                        at.setCancelado(rat.getBoolean("cancelado"));
                        at.setFechaPrevistoInicio(rat.getDate("fechaPrevistoInicio"));
                        at.setAcordadoConIndustrial(rat.getBoolean("acordadoConIndustrial"));
                        at.setComentariosIndustrial(rat.getString("comentariosIndustrial"));

                    }
                    rat.close();


                }
                rot.close();

            }
            re.close();
            //imputaciones
            String sqlidi = "SELECT * FROM imputaciones_documento_imputable WHERE ciclo_id = " + cicloId;
            ResultSet ridi = Rentamaster2DB.getResultSet(sqlidi, Rentamaster2DB.getConnection());
            while(ridi.next()){
                ImputacionDocumentoImputable idi = new ImputacionDocumentoImputable();
                entitiesToPersist.add(idi);
                idi.setRm2id(ridi.getInt("id"));
                idi.setCiclo(c);
                c.getImputacionesDocumentoImputable().add(idi);
                idi.setDescripcionImputacion(ridi.getString("descripcionImputacion"));
                idi.setImporteImputacion(ridi.getDouble("importeImputacion"));
                idi.setImputacionIndefinidos(ridi.getBoolean("imputacionIndefinidos"));
                idi.setPorcentajeImputacion(ridi.getDouble("porcentajeImputacion"));
                idi.setInformacionAdicional(ridi.getString("informacionAdicional"));
                idi.setEvento(devuelveEventoAsociado(c, ridi.getInt("evento_id")));
                //cargando el documento
                Integer did = ridi.getInt("documento_imputable_id");
                if (new FacturasProveedores().esIdCorrespondienteAFacturaProveedor(did, persistence)){
                    FacturaProveedor fp = new FacturasProveedores().realizaImportacion(did, persistence, entitiesToPersist);
                    idi.setDocumentoImputable(fp);
                }
                if (new Presupuestos().esIdCorrespondienteAPresupuesto(did, persistence)){
                    Presupuesto p = new Presupuestos().realizaImportacion(did, persistence, entitiesToPersist);
                    idi.setDocumentoImputable(p);
                }

                //importando validaciones imputaciones documento imputable
                sql = "select * from validaciones_imputacion_documento_imputable vidi inner join validaciones v on vidi.id = v.id where vidi.imputacion_documento_imputable_id = " + ridi.getInt("id");
                ResultSet rv = Rentamaster2DB.getResultSet(sql);
                while(rv.next()){
                    ValidacionImputacionDocumentoImputable vidi = new ValidacionImputacionDocumentoImputable();
                    entitiesToPersist.add(vidi);
                    vidi.setEstadoValidacion(ValidacionEstado.fromId(rv.getInt("estado")));
                    vidi.setImputacionDocumentoImputable(idi);
                    idi.setValidacionImputacion(vidi);
                    vidi.setPropietario(new Personas().devuelvePropietarioDesdeSofiaId(rv.getInt("propietario_id"), persistence));
                    vidi.setFechaAprobacionRechazo(rv.getDate("fechaAprovacionCancelacion"));
                    vidi.setRm2id(rv.getInt("id"));


                }

            }
            ridi.close();

            //archivos adjuntos
            //ya esta hecha antes

            //pendiente items facturables.
        }
        r.close();
        return c;
    }

    private Evento devuelveEventoAsociado(Ciclo c, Integer sofiaEventoId){
        for (int i = 0; i < c.getEventos().size(); i++) {
            Evento ev =  c.getEventos().get(i);
            if (ev.getRm2id().intValue()==sofiaEventoId.intValue()){
                return ev;
            }

        }
        return null;
    }

    public Ciclo devuelveCicloDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT c FROM test1_Ciclo c WHERE c.rm2id = " + id;
        Ciclo c = (Ciclo) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return c;
    }

    public CarpetaDocumentosFotograficos devuelveCDFDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT c FROM test1_CarpetaDocumentosFotograficos c WHERE c.rm2id = " + id;
        CarpetaDocumentosFotograficos c = (CarpetaDocumentosFotograficos) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return c;
    }

    public ImputacionDocumentoImputable devuelveImputacionDocumentoImputableDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT idi FROM test1_ImputacionDocumentoImputable idi WHERE idi.rm2id = " + id;
        ImputacionDocumentoImputable idi = (ImputacionDocumentoImputable) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return idi;
    }

}
