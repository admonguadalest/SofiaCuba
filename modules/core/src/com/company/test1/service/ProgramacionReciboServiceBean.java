package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.recibos.*;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.thoughtworks.xstream.security.NoPermission;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service(ProgramacionReciboService.NAME)
public class ProgramacionReciboServiceBean implements ProgramacionReciboService {

    @Inject
    private Persistence persistence;

    public int getNumRecibosEnQueConceptoReciboHaSidoAplicado(ConceptoRecibo cr) throws Exception{
        String hql = "SELECT icr FROM test1_ImplementacionConcepto icr JOIN icr.conceptoRecibo cr WHERE " +
                "cr.id = :crid";
        Transaction t = persistence.createTransaction();
        List<ImplementacionConcepto> l = persistence.getEntityManager().createQuery(hql)
                .setParameter("crid", cr.getId()).getResultList();
        t.close();
        return l.size();
    }

    public List<ConceptoRecibo> getConceptosReciboAgregado(ProgramacionRecibo pr){

        List<ConceptoRecibo> todosLosConceptoRecibo = pr.getConceptosRecibo();
        List<ConceptoRecibo> crAgregados =  new ArrayList<ConceptoRecibo>();

        boolean yaEstabaIncluido;

        for (int i = 0; i < todosLosConceptoRecibo.size(); i++) {
            ConceptoRecibo conceptoRecibo = todosLosConceptoRecibo.get(i);
            yaEstabaIncluido=false;


            for (int j = 0; j < crAgregados.size(); j++) {
                ConceptoRecibo cr = crAgregados.get(j);
                if (conceptoRecibo.getConcepto().getId().equals(cr.getConcepto().getId())){
                    yaEstabaIncluido=true;
                    cr.setImporte(cr.getImporte()+conceptoRecibo.getImporte());
                    break;
                }
            }

            if (yaEstabaIncluido==false){
                ConceptoRecibo cr = new ConceptoRecibo();
                cr.setConcepto(conceptoRecibo.getConcepto());
                cr.setImporte(conceptoRecibo.getImporte());

                cr.setVigencia(conceptoRecibo.getVigencia());
                cr.setActivadoDesactivado(conceptoRecibo.getActivadoDesactivado());
                crAgregados.add(cr);
            }
        }

        Collections.sort(crAgregados,ConceptoRecibo.ordenacionConceptoReciboComparator);
        return crAgregados;
    }

    public List<NonPersistentConceptoRecibo> resumeConceptosRecibo(ProgramacionRecibo pr)
            throws Exception{
        ArrayList<NonPersistentConceptoRecibo> al = new ArrayList<NonPersistentConceptoRecibo>();
        TreeMap<Concepto,Double> tmConceptoImportes = new TreeMap(Concepto.ordenacionComparator);
        TreeMap<Concepto,List<ConceptoRecibo>> tmConceptoConceptosRecibo = new TreeMap(Concepto.ordenacionComparator);

        for (int i = 0; i < pr.getConceptosRecibo().size(); i++) {
            ConceptoRecibo cr = pr.getConceptosRecibo().get(i);
            Concepto concepto = cr.getConcepto();
            Double d = tmConceptoImportes.get(concepto);
            if (d==null){
                d = new Double(0.0);

            }
            Date proxFechaEmision = calculaProximaFechaEmisionRecibo(pr.getContratoInquilino());
            if (esConceptoReciboVigenteEnFecha(cr, proxFechaEmision)){
                if (concepto.getAdicionSustraccion()){
                    d+= cr.getImporte();
                }else{
                    d-= cr.getImporte();
                }

            }
            tmConceptoImportes.put(concepto, d);
            List<ConceptoRecibo> l = tmConceptoConceptosRecibo.get(concepto);
            if (l == null){
                l = new ArrayList<ConceptoRecibo>();
                tmConceptoConceptosRecibo.put(concepto, l);
            }
            l.add(cr);

        }

        Iterator<Concepto> ki = tmConceptoImportes.keySet().iterator();
        while(ki.hasNext()){
            Concepto c = ki.next();
            Double d = tmConceptoImportes.get(c);
            NonPersistentConceptoRecibo npcr = new NonPersistentConceptoRecibo();
            npcr.setConcepto(c);
            npcr.setImporte(d);
            List<ConceptoRecibo> ccrr = tmConceptoConceptosRecibo.get(c);
            npcr.setConceptosRecibo(ccrr);
            al.add(npcr);
        }
        return al;
    }

    public Date calculaProximaFechaEmisionRecibo(ContratoInquilino ci){
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        int m = c.get(Calendar.MONTH);
        while(m == c.get(Calendar.MONTH)){
            d = DateUtils.addDays(d, 1);
            c.setTime(d);
        }
        return d;
    }

    public boolean esConceptoReciboVigenteEnFecha(ConceptoRecibo cr, Date fecha){
        if (cr.getVigencia()== ConceptoReciboVigenciaEnum.PERMANENTE){
            return true;
        }else if(cr.getVigencia()==ConceptoReciboVigenciaEnum.ENTRE_FECHAS){
            Date fechaDesde = cr.getFechaDesde();
            Date fechaHasta = cr.getFechaHasta();
            return (fechaDesde.getTime() < fecha.getTime()) && (fechaHasta.getTime() > fecha.getTime());
        }else if(cr.getVigencia()==ConceptoReciboVigenciaEnum.ACTIVACION){
            return cr.getActivadoDesactivado();
        }else if(cr.getVigencia()==ConceptoReciboVigenciaEnum.NUMERO_EMISIONES){
            //pendiente revisar
            int numemisiones = cr.getNumEmisiones();
            String sql = "SELECT count(ic.id) FROM test1_ImplementacionConcepto ic JOIN ic.conceptoRecibo cr WHERE "
                    + "cr.id = :crid";
            Transaction t = persistence.createTransaction();
            Number n = (Number) persistence.getEntityManager().createQuery(sql).setParameter("crid", cr.getId()).getFirstResult();
//            Number n = (Number) sl.executeNamedDynamicQuerySingleResult(sql, new Hashtable());
            return n.intValue() < numemisiones;
        }
        return false;
    }



}