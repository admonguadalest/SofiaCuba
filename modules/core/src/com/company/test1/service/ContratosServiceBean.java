package com.company.test1.service;

import com.company.test1.StringUtils;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service(ContratosService.NAME)
public class ContratosServiceBean implements ContratosService {

    @Inject
    Persistence persistence;
    @Inject
    DataManager dataManager;

    public ContratoInquilino devuelveContratoVigenteParaDepartamento(Departamento d) throws Exception{
        Transaction t = persistence.createTransaction();
        ContratoInquilino ci = (ContratoInquilino) persistence.getEntityManager().
                createQuery("SELECT c FROM test1_ContratoInquilino c JOIN c.departamento d WHERE d.id = :did AND c.estadoContrato = :ec")
                .setParameter("did", d.getId()).setParameter("ec", EstadoContratoInquilinoEnum.VIGENTE).getFirstResult();
        if (ci!=null){
            ci = persistence.getEntityManager().reload(ci, "contratoInquilino-notificaciones-aumentos-view");
        }

        t.close();
        return ci;
    }

    public ContratoInquilino devuelveContratoVigenteParaDepartamento(Departamento d, String viewName) throws Exception{
        Transaction t = persistence.createTransaction();
        ContratoInquilino ci = (ContratoInquilino) persistence.getEntityManager().
                createQuery("SELECT c FROM test1_ContratoInquilino c JOIN c.departamento d WHERE d.id = :did AND c.estadoContrato = :ec")
                .setView(ContratoInquilino.class, viewName).setParameter("did", d.getId()).setParameter("ec", EstadoContratoInquilinoEnum.VIGENTE).getFirstResult();


        t.close();
        return ci;
    }

    @Override
    public List<ContratoInquilino> devuelveContratosParaPropietarios(List<DefinicionRemesa> ddrr, List<Propietario> props) throws Exception {
        List al = null;
        Transaction t = persistence.createTransaction();
        String hql = "select distinct ci from test1_ContratoInquilino ci JOIN ci.departamento d JOIN d.ubicacion u LEFT JOIN d.propietario p1 LEFT JOIN u.propietario p2 " +
                "JOIN ci.programacionRecibo pr JOIN pr.definicionRemesa dr " +
                "WHERE ci.estadoContrato = 3 and (";
        for (int i = 0; i < props.size(); i++) {
            Propietario p = props.get(i);
            hql +=  " (p1 = :p"+i+" OR p2 = :p"+i+")";
        }
        hql += " ) AND (";
        for (int i = 0; i < ddrr.size(); i++) {
            DefinicionRemesa dr = ddrr.get(i);
            if (i == 0){
                hql +=  " (dr = :dr"+i+")";
            }else{
                hql +=   " OR (dr = :dr"+i+")";
            }

        }
        hql += ") ";

        Query q = persistence.getEntityManager().createQuery(hql);
        for (int i = 0; i < props.size(); i++) {
            Propietario p = props.get(i);
            q.setParameter("p"+i, p);
        }

        for (int i = 0; i < ddrr.size(); i++) {
            DefinicionRemesa dr = ddrr.get(i);
            q.setParameter("dr"+i, dr);
        }
        al = q.getResultList();

        for (int i = 0; i < al.size(); i++) {
            ContratoInquilino ci = (ContratoInquilino) al.get(i);
            System.out.println(ci.getDepartamento().getNombreDescriptivoCompleto());
        }

        t.close();
        return al;

    }

    public Hashtable<ContratoInquilino,List<ConceptoRecibo>> getConceptosRecibosGeneradosEntreFechasParaConceptos(List<ContratoInquilino> contratos, Date fechaDesde, Date fechaHasta, List<Concepto> conceptos){

        Hashtable h = new Hashtable<ContratoInquilino,List<ConceptoRecibo>>();

        fechaDesde = DateUtils.truncate(fechaDesde, Calendar.DAY_OF_MONTH);
        fechaHasta = DateUtils.truncate(fechaHasta, Calendar.DAY_OF_MONTH);
        fechaHasta = DateUtils.addDays(fechaHasta, 1);
        fechaHasta = DateUtils.addSeconds(fechaHasta, -1);

        for (int i = 0; i < contratos.size(); i++) {
            ContratoInquilino c = contratos.get(i);
            ProgramacionRecibo pr = c.getProgramacionRecibo();
            List<ConceptoRecibo> ccrr = pr.getConceptosRecibo();
            for (int j = 0; j < ccrr.size(); j++) {
                ConceptoRecibo conceptoRecibo = ccrr.get(j);
                Concepto conc = conceptoRecibo.getConcepto();
                if (conceptos.indexOf(conc)!=-1){
                    Date fechaValor = conceptoRecibo.getFechaValor();
                    if ((fechaValor.getTime() >= fechaDesde.getTime()) && (fechaValor.getTime() <= fechaHasta.getTime())){
                        List<ConceptoRecibo> ccrr_ = (List<ConceptoRecibo>) h.get(c);
                        if (ccrr_==null){
                            ArrayList<ConceptoRecibo> al_ccrr = new ArrayList<ConceptoRecibo>();
                            h.put(c, al_ccrr);
                            ccrr_ = al_ccrr;
                        }
                        ccrr_.add(conceptoRecibo);

                    }
                }
            }
        }

        return h;

    }

    public String getNuevoNumeroParaContrato(ContratoInquilino ci) throws Exception{
        String s = "";
        Departamento d = ci.getDepartamento();
        d = dataManager.reload(d, "departamento-view");
        if (ci.getDepartamento().getViviendaLocal()){
            Propietario propietario = d.getPropietarioEfectivo();
            propietario = dataManager.reload(propietario, "_base");
            s = propietario.getAbreviacionContratos() + "V" + d.getUbicacion().getAbreviacionUbicacion() + d.getAbreviacionPisoPuerta();
        }else if(!ci.getDepartamento().getViviendaLocal()){
            Propietario propietario = d.getPropietarioEfectivo();
            propietario = dataManager.reload(propietario, "_base");
            s = propietario.getAbreviacionContratos() + "L" + d.getUbicacion().getAbreviacionUbicacion() + d.getAbreviacionPisoPuerta();
        }

        Transaction t = persistence.createTransaction();
        //cuento el numero de contratos renunciados + 1
        String sql = "SELECT c FROM test1_ContratoInquilino c JOIN c.departamento d WHERE c.estadoContrato = :ec AND d.id = :did";

        List<ContratoInquilino> l = persistence.getEntityManager().createQuery(sql, ContratoInquilino.class).setParameter("ec",EstadoContratoInquilinoEnum.RENUNCIADO)
                .setParameter("did", ci.getDepartamento().getId()).getResultList();


        Number n = l.size();
        if (n.intValue()==0){
            List<ContratoInquilino> l_ = persistence.getEntityManager().createQuery(sql, ContratoInquilino.class).setParameter("ec",EstadoContratoInquilinoEnum.VIGENTE)
                    .setParameter("did", ci.getDepartamento().getId()).getResultList();
            if (l_.size()==1){
                n = 1;
            }
        }
        int i = n.intValue() + 1;
        String serie = new Integer(i).toString();
        serie = StringUtils.repeat("0", 5 - serie.length()) + serie;
        return s + serie;
    }

    public ContratoInquilino getContratoVigente(Departamento d) throws Exception{
        String sql = "select c from test1_ContratoInquilino c JOIN c.departamento d WHERE d.id = :did and c.estadoContrato = :estado";
        Transaction t = persistence.createTransaction();
        ContratoInquilino ci = (ContratoInquilino) persistence.getEntityManager().createQuery(sql).setParameter("did", d.getId()).setParameter("estado", EstadoContratoInquilinoEnum.VIGENTE).getFirstResult();
        t.close();
        return ci;
    }


}