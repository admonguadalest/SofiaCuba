package com.company.test1.service;

import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.coeficientes.UbicacionCoeficiente;
import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.IncrementoGeneralObrasModoReparticion;
import com.company.test1.entity.enums.TipoContratoInquilinoEnum;
import com.company.test1.entity.enums.recibos.ConceptoReciboEstadoNotificacion;
import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.incrementos.IncrementoGeneralObras;
import com.company.test1.entity.incrementos.IncrementoIndice;
import com.company.test1.entity.recibos.*;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang3.time.DateUtils;
import org.docx4j.wml.Numbering;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(IncrementosService.NAME)
public class IncrementosServiceBean implements IncrementosService {

    @Inject
    NumberUtilsService numberUtilsService;
    @Inject
    Persistence persistence;


    public double devuelveCoeficienteDepartamentoSobreTotalUbicacion(Departamento d, TipoCoeficiente tc)
            throws Exception{

        double totalUbicacion = 0.0;
        Ubicacion u = d.getUbicacion();
        Transaction t = persistence.createTransaction();
        u = persistence.getEntityManager().reload(u, "ubicacion-with-direcciones");
        t.close();
        List<UbicacionCoeficiente> uucc = u.getUbicacionesCoeficientes();
        for (int i = 0; i < uucc.size(); i++) {
            UbicacionCoeficiente uc = uucc.get(i);
            if (uc.getTipoCoeficiente().getId().compareTo(tc.getId())==0){
                totalUbicacion = uc.getTotalTipoCoeficiente();
            }
        }
        t = persistence.createTransaction();
        //calculando el del departamento
        String sql = "SELECT c.valor FROM test1_Coeficiente c "
                + " JOIN c.tipoCoeficiente tc "
                + " JOIN c.departamento d "
                + " WHERE "
                + " tc.id = :tc AND "
                + " d.id = :did";
//        Hashtable ht = new Hashtable();
//        ht.put("tc",tc.getId());
//        ht.put("did", d.getId());

        double totalDepto = 0.0;
        List l = persistence.getEntityManager().createQuery(sql).setParameter("tc", tc.getId()).setParameter("did", d.getId()).getResultList();
        if (l.size()==1){
            Number n = (Number) l.get(0);
            totalDepto = n.doubleValue();
        }else{
            throw new Exception("No se pudo determinar el coeficiente para el departamento");
        }

        double res =  totalDepto / totalUbicacion;
        return res;
    }


    public ConceptoRecibo[] creaConceptosReciboParaIncrementosGeneralesYObras(
            Concepto concepto,
            ContratoInquilino contratoInquilino,
            List<Departamento> deptos,
            TipoCoeficiente tipoCoeficiente,
            Date fechaAplicacionIncremento,
            Double importeAumento,
            TipoContratoInquilinoEnum disciminadorLau,
            Boolean aplicacion12PrcContratosIndefinidos,
            Boolean actualizableIPC,
            IncrementoGeneralObrasModoReparticion modoReparto,
            Concepto conceptoAtrasos,
            Integer nMesesAtrasos,
            String descripcionIncremento,
            Object[] infoVigencia
    )
            throws Exception
    {

        ConceptoRecibo[] ccrr;
        if ((nMesesAtrasos!=null)&&(nMesesAtrasos > 0) && (conceptoAtrasos!=null)){
            ccrr = new ConceptoRecibo[2];
        }else{
            ccrr = new ConceptoRecibo[1];
        }

        Transaction t = persistence.createTransaction();
        contratoInquilino = persistence.getEntityManager().reload(contratoInquilino, "contratoInquilino-view");
        t.close();

        Departamento d = contratoInquilino.getDepartamento();

        double coeficiente = devuelveCoeficienteDepartamentoSobreTotalUbicacion(d, tipoCoeficiente);
        //si coeficiente es 0.0 devolvemos null
        if (coeficiente==0.0) return null;

        double factor = 0.0;

        if (modoReparto == IncrementoGeneralObrasModoReparticion.MODO_REPARTO_TOTAL_REPARTIDO_COEFS_TODOS_DEPTOS){
            factor = coeficiente / 12;
        }else if(modoReparto == IncrementoGeneralObrasModoReparticion.MODO_REPARTO_TOTAL_PONDERADO_DEPTOS_SELECCIONADOS){
            double[] coefs = new double[deptos.size()];
            double sumcoefs = 0.0;
            for (int i = 0; i < deptos.size(); i++) {
                Departamento departamento = deptos.get(i);
                double dc = devuelveCoeficienteDepartamentoSobreTotalUbicacion(departamento, tipoCoeficiente);
                coefs[i] = dc;
                sumcoefs += dc;
            }
            factor = (coeficiente / sumcoefs)/12;
        }else if(modoReparto == IncrementoGeneralObrasModoReparticion.MODO_REPARTO_TODO_EN_CADA_DEPARTAMENTO){
            factor = (double)1/(double)12;
        }

        /*Principal*/
        ConceptoRecibo cr = new ConceptoRecibo();

        cr.setActualizableIPC(actualizableIPC);
        cr.setConcepto(concepto);
        cr.setDescripcionCausa(descripcionIncremento);
        cr.setEsModificacionAgregada(false);
        cr.setEstadoNotificacion(ConceptoReciboEstadoNotificacion.NO_NOTIFICADO);
        cr.setFechaValor(fechaAplicacionIncremento);
        //si es contratoInquilino indefinido y se esta realizando la aplicacion del 12% entonces el importe se debe ajustar
        //aplicando el 12%
        boolean esContratoIndefinido = (contratoInquilino.getTipoContrato()== TipoContratoInquilinoEnum.ANTIGUA_LAU);
        if (esContratoIndefinido){
            if (aplicacion12PrcContratosIndefinidos){
                cr.setImporte(importeAumento * factor * 0.12);
            }else{
                cr.setImporte(importeAumento * factor);
            }
        }else{
            cr.setImporte(importeAumento * factor);
        }
        cr.setImporte(numberUtilsService.roundToNDecimals(cr.getImporte(), 2.0));

        cr.setProgramacionRecibo(contratoInquilino.getProgramacionRecibo());

        ConceptoReciboVigenciaEnum vigencia = (ConceptoReciboVigenciaEnum) infoVigencia[0];

        cr.setVigencia(vigencia);

        if (vigencia==ConceptoReciboVigenciaEnum.PERMANENTE) {

        }
        if (vigencia==ConceptoReciboVigenciaEnum.ACTIVACION) {
            cr.setActivadoDesactivado(true);
        }
        if (vigencia==ConceptoReciboVigenciaEnum.ENTRE_FECHAS) {
            cr.setFechaDesde((Date) infoVigencia[2]);
            cr.setFechaHasta((Date) infoVigencia[3]);
        }
        if (vigencia==ConceptoReciboVigenciaEnum.NUMERO_EMISIONES) {
            cr.setNumEmisiones((Integer)infoVigencia[4]);
        }
        //adjuntando ConceptoADicionalConceptoRecibo
        realizaAdjuntacionConceptosAdicionalesConceptoReciboEnConceptoReciboSegunConceptoRecibo(cr, "RENTA");
        ccrr[0] = cr;

        IncrementoGeneralObras igo = new IncrementoGeneralObras();
        // cr.setIncremento(igo);
        igo.setConceptoRecibo(cr);
        cr.setIncremento(igo);

        if ((nMesesAtrasos!=null)&&(nMesesAtrasos > 0)){
            ConceptoRecibo cr2 = new ConceptoRecibo();

            cr2.setActualizableIPC(actualizableIPC);
            cr2.setConcepto(conceptoAtrasos);
            cr2.setDescripcionCausa("ATRASOS");
            cr2.setEsModificacionAgregada(false);
            cr2.setEstadoNotificacion(ConceptoReciboEstadoNotificacion.NO_NOTIFICADO);

            cr2.setFechaValor(fechaAplicacionIncremento);

            cr2.setImporte(cr.getImporte() * nMesesAtrasos);
            //cr2.setImporte((importeAumento * factor)*nMesesAtrasos);

            cr2.setImporte(numberUtilsService.roundToNDecimals(cr2.getImporte(), 2.0));

            cr2.setProgramacionRecibo(contratoInquilino.getProgramacionRecibo());

            cr2.setVigencia(ConceptoReciboVigenciaEnum.NUMERO_EMISIONES);
            cr2.setNumEmisiones(1);
            //adjuntando ConceptoADicionalConceptoRecibo
            realizaAdjuntacionConceptosAdicionalesConceptoReciboEnConceptoReciboSegunConceptoRecibo(cr2, "RENTA");
            ccrr[1] = cr2;

            igo.setConceptoReciboAtrasos(cr2);
            cr2.setIncremento(igo);
            igo.setImporteAtrasos(cr2.getImporte());
        }

        igo.setFechaIncremento(fechaAplicacionIncremento);
        igo.setImporte(cr.getImporte());
        igo.setImporteGlogalIncremento(numberUtilsService.roundToNDecimals(importeAumento, 2.0));
        igo.setMesesAtrasos(nMesesAtrasos);
        igo.setTipoCoeficiente(tipoCoeficiente);
        igo.setValorCoeficiente(numberUtilsService.roundToNDecimals(factor * 12, 3.0));
        igo.setModoReparticion(modoReparto);

        return ccrr;
    }

    private Double devuelvePorcentajeMasFrecuenteAplicadoEnConceptoAdicionalParaConceptoRecibo(ConceptoAdicional ca)
            throws Exception{
        try {
            Transaction t = persistence.createTransaction();
            String sql = "select concepto_adicional_id, porcentaje, count(concepto_recibo_id)  as c from conceptos_adicionales_conceptos_recibo "
                    + " group by concepto_adicional_id, porcentaje order by c desc";
            List<Object[]> l = persistence.getEntityManager().createQuery(sql).getResultList();
            t.close();
            if (l.size()>0){
                Object[] oarr = l.get(0);
                return (Double) oarr[1];
            }else{
                return null;
            }
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void realizaAdjuntacionConceptosAdicionalesConceptoReciboEnConceptoReciboSegunConceptoRecibo(ConceptoRecibo cr, String nombreConceptoReciboBase)
            throws Exception
    {
        List<ConceptoRecibo> ccrrbase = getConceptoReciboDesdeNombre(nombreConceptoReciboBase, cr.getProgramacionRecibo());

        if (ccrrbase.isEmpty()){
            //si llego aqui, quiere decir qeu no tengo base para saber los porcentajes por defecto de cada uno de los conceptos adicionales aociados...
            //por tanto me he de basar en el programacionrecibo
            ProgramacionRecibo pr = cr.getProgramacionRecibo();
            List<ProgramacionConceptoAdicional> lpca = pr.getProgramacionConceptosAdicionales();
            for (int i = 0; i < lpca.size(); i++) {
                ProgramacionConceptoAdicional pca = lpca.get(i);
                ConceptoAdicional ca = pca.getConceptoAdicional();
                ConceptoAdicionalConceptoRecibo cacr = new ConceptoAdicionalConceptoRecibo();
                cacr.setConceptoAdicional(ca);
                cacr.setConceptoRecibo(cr);
                //calculo del valor mas probable
                Double porcentaje = 0.0;
                porcentaje = devuelvePorcentajeMasFrecuenteAplicadoEnConceptoAdicionalParaConceptoRecibo(ca);
                if (porcentaje == null){
                    porcentaje = 0.0;
                }
                cacr.setPorcentaje(porcentaje);
                cr.getConceptosAdicionalesConceptoRecibo().add(cacr);
            }
            //creo la lista de conceptos adicionale establecida en el programacion recibo y trato de asignarle valores de preseleccion
            //en funcion del valor mas frecuente para este conceptorecibo

        }else{

            ConceptoRecibo crbase = ccrrbase.get(0);
            List<ConceptoAdicionalConceptoRecibo> lcacr = crbase.getConceptosAdicionalesConceptoRecibo();
            for (int i = 0; i < lcacr.size(); i++) {
                ConceptoAdicionalConceptoRecibo cacr = lcacr.get(i);
                ConceptoAdicionalConceptoRecibo cacr_ = new ConceptoAdicionalConceptoRecibo();
                cacr_.setConceptoAdicional(cacr.getConceptoAdicional());
                cacr_.setConceptoRecibo(cr);
                cacr_.setPorcentaje(cacr.getPorcentaje());
                cr.getConceptosAdicionalesConceptoRecibo().add(cacr_);
            }
        }
    }

    private List<ConceptoRecibo> getConceptoReciboDesdeNombre(String nombre, ProgramacionRecibo pr){
        List<ConceptoRecibo> res = new ArrayList<ConceptoRecibo>();
        List<ConceptoRecibo> ccrr = pr.getConceptosRecibo();
        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo cr = ccrr.get(i);
            if (cr.getConcepto().getTituloConcepto().compareTo(nombre)==0){
                res.add(cr);
            }
        }
        return res;
    }


    public ConceptoRecibo[] creaConceptosReciboParaIncrementosIndiceReferencia(
            Concepto concepto,
            ContratoInquilino contratoInquilino,
            Integer mes,
            Integer anno,
            Date fechaAplicacionIncremento,
            Concepto conceptoAtrasos,
            Integer nMesesAtrasos

    )
            throws Exception
    {

        Persistence persistence = AppBeans.get(Persistence.class);
        Transaction t = persistence.createTransaction();
        contratoInquilino = AppBeans.get(Persistence.class).getEntityManager().reload(contratoInquilino, "contratoInquilino-view");
        t.close();

        ConceptoRecibo[] ccrr;

        if (nMesesAtrasos > 0){
            ccrr = new ConceptoRecibo[2];
        }else{
            ccrr = new ConceptoRecibo[1];
        }

        /*Principal*/
        ConceptoRecibo cr = new ConceptoRecibo();
        cr.setActivadoDesactivado(true);
        cr.setActualizableIPC(true);
        cr.setConcepto(concepto);
        cr.setDescripcionCausa("");
        cr.setEsModificacionAgregada(false);
        cr.setEstadoNotificacion(ConceptoReciboEstadoNotificacion.NO_NOTIFICADO);

        cr.setFechaValor(fechaAplicacionIncremento);

        double baseAumento = 0.0;
        double indiceAnterior = 0.0;
        double indiceActual = 0.0;
        double incrementoPorcentual;
        double importeAumento;

        //calculo incremento
        String nombreTipoIndice = contratoInquilino.getNombreTipoIndiceIncrementos();

        try{

            RegistroIndiceReferencia rir = getRegistroForValues(mes -1, anno, nombreTipoIndice);
            RegistroIndiceReferencia rir_ant = getRegistroForValuesIndiceAnterior(mes-1, anno, contratoInquilino.getPeriodoActualizacionIPC(), nombreTipoIndice);
            indiceAnterior = rir_ant.getValor();
            indiceActual = rir.getValor();
        }catch(Exception exc){
            throw new Exception(exc.getMessage());
        }
        incrementoPorcentual = ((indiceActual - indiceAnterior)/indiceAnterior);
        incrementoPorcentual = numberUtilsService.roundToNDecimals(incrementoPorcentual*100, 3.0);
        //base aumento
        List<ConceptoRecibo> ccrr_actualizables = getConceptosReciboActualizablesIPC(contratoInquilino.getProgramacionRecibo());
        for (int i = 0; i < ccrr_actualizables.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr_actualizables.get(i);
            if (conceptoRecibo.getConcepto().getAdicionSustraccion()){
                baseAumento += conceptoRecibo.getImporte();
            }else{
                baseAumento -= conceptoRecibo.getImporte();
            }
        }

        baseAumento = numberUtilsService.roundToNDecimals(baseAumento, 2.0);

        importeAumento = baseAumento * (incrementoPorcentual/100);
        importeAumento = numberUtilsService.roundToNDecimals(importeAumento, 2.0);

        if (!contratoInquilino.getProgramacionRecibo().getAplicarIPCNegativo() && (importeAumento < 0.00)){
            importeAumento = 0.0;
            cr.setDescripcionCausa("Omisión IPC negativo");
        }

        cr.setImporte(importeAumento);
        cr.setProgramacionRecibo(contratoInquilino.getProgramacionRecibo());
//        cr.setRealizadoPor(usuarioActivo); PENDIENTE
        cr.setVigencia(ConceptoReciboVigenciaEnum.PERMANENTE);
        //adjuntando ConceptoADicionalConceptoRecibo
        realizaAdjuntacionConceptosAdicionalesConceptoReciboEnConceptoReciboSegunConceptoRecibo(cr, "RENTA");
        ccrr[0] = cr;

        IncrementoIndice iir = new IncrementoIndice();
        //  cr.setIncremento(iir);
        iir.setConceptoRecibo(cr);
        iir.setFechaIncremento(fechaAplicacionIncremento);
        iir.setImporte(importeAumento);
        iir.setIndiceAnterior(indiceAnterior);
        iir.setMesAnnoIndice(contratoInquilino.getMesAnyoAplicacionIPC());
        iir.setNombreTipo(nombreTipoIndice);
        iir.setValorBase(baseAumento);
        iir.setValorIndice(indiceActual);
        iir.setValorIndicePorcentual(incrementoPorcentual/100);

        cr.setIncremento(iir);

        if (nMesesAtrasos > 0){
            ConceptoRecibo cr2 = new ConceptoRecibo();

            cr2.setActivadoDesactivado(true);
            cr2.setActualizableIPC(false);

            cr2.setConcepto(conceptoAtrasos);
            cr2.setDescripcionCausa("ATRASOS");
            cr2.setEsModificacionAgregada(false);
            cr2.setEstadoNotificacion(ConceptoReciboEstadoNotificacion.NO_NOTIFICADO);
//            cr2.setFechaCreacion(new Date());
            cr2.setFechaValor(fechaAplicacionIncremento);
            cr2.setImporte(numberUtilsService.roundToNDecimals((importeAumento )*nMesesAtrasos, 2.0));
            cr2.setNumEmisiones(1);
            cr2.setProgramacionRecibo(contratoInquilino.getProgramacionRecibo());
//            cr2.setRealizadoPor(usuarioActivo);//PENDIENTE
            cr2.setVigencia(ConceptoReciboVigenciaEnum.NUMERO_EMISIONES);
            //adjuntando ConceptoADicionalConceptoRecibo
            realizaAdjuntacionConceptosAdicionalesConceptoReciboEnConceptoReciboSegunConceptoRecibo(cr2, "RENTA");
            ccrr[1] = cr2;

            iir.setConceptoReciboAtrasos(cr2);
            iir.setImporteAtrasos(cr2.getImporte());
            iir.setMesesAtrasos(nMesesAtrasos);
            iir.setMesesAtrasos(nMesesAtrasos);
            cr2.setIncremento(iir);
        }else{
            iir.setConceptoReciboAtrasos(null);
            iir.setImporteAtrasos(0.0);
            iir.setMesesAtrasos(0);
            iir.setMesesAtrasos(0);
        }

        return ccrr;
    }

    public  RegistroIndiceReferencia getRegistroForValues(Integer mes, Integer anno, String nombreTipo) throws Exception {
        Transaction t = persistence.createTransaction();
        String sql = "SELECT r FROM test1_RegistroIndiceReferencia r WHERE r.mes = :m AND r.anno = :a AND r.nombreTipo LIKE :nt";
        Hashtable ht = new Hashtable();
        ht.put("a",anno);
        ht.put("m", mes);
        ht.put("nt", nombreTipo);
        RegistroIndiceReferencia r = (RegistroIndiceReferencia) persistence.getEntityManager().createQuery(sql).setParameter("a", anno).setParameter("m", mes).setParameter("nt", nombreTipo).getFirstResult();
        t.close();
        return r;
    }

    private RegistroIndiceReferencia getRegistroForValuesIndiceAnterior(Integer mes, Integer anno, Integer mesesEntreRevision, String nombreTipo)
            throws Exception
    {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH,mes);
            cal.set(Calendar.YEAR,anno);
            Date dactual = cal.getTime();
            Date danterior = DateUtils.addMonths(dactual, -mesesEntreRevision);

            cal.setTime(danterior);
            int monthAnterior = cal.get(Calendar.MONTH);
            int yearAnterior = cal.get(Calendar.YEAR);

            RegistroIndiceReferencia rir = getRegistroForValues(monthAnterior, yearAnterior, nombreTipo);
            return rir;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public List<ConceptoRecibo> getConceptosReciboActualizablesIPC(ProgramacionRecibo pr ){
        List<ConceptoRecibo> ccrr = pr.getConceptosRecibo();
        List<ConceptoRecibo> cc = new ArrayList();
        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr.get(i);
            if (conceptoRecibo.getActualizableIPC()){
                cc.add(conceptoRecibo);
            }
        }
        return cc;
    }

    public String calculaProximoValorMesAnyoAplicacionIPC(ContratoInquilino c) throws Exception{
        DateFormat df = new SimpleDateFormat("MMyyyy");
        Date d = null;
        try{
            d = df.parse(c.getMesAnyoAplicacionIPC());
        }catch(Exception exc){
            throw new Exception("Valor de Fecha de Aplicación de IPC inválida para contrato");
        }
        Integer mesesIntervalo = c.getPeriodoActualizacionIPC();
        d = DateUtils.addMonths(d, mesesIntervalo);
        String mapr = df.format(d);
        return mapr;
    }



}