package com.company.test1.service;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Entrada;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.EstadoCicloEnum;
import com.company.test1.entity.enums.TipoCiclo;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(CicloService.NAME)
public class CicloServiceBean implements CicloService {

    @Inject
    Persistence persistence;
    @Inject
    private DataManager dataManager;

    public String calculaNuevoCodigoCiclo(Departamento d) throws Exception{
        String ci;


        Ubicacion u = d.getUbicacion();
        String cc = u.getAbreviacionUbicacion() + d.getAbreviacionPisoPuerta();
        //extraigo su codigo y en funcion de la fecha calculo uno nuevo
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        cc += "/" + sdf.format(new Date());
        return cc;
    }

    public String calculaNuevoTituloCiclo(Departamento d) throws Exception{
        String ci;


        Ubicacion u = d.getUbicacion();
        String cc = u.getAbreviacionUbicacion() + d.getAbreviacionPisoPuerta();
        //extraigo su codigo y en funcion de la fecha calculo uno nuevo
        SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
        cc += "_" + sdf.format(new Date());
        return cc;
    }

    public Ciclo devuelveCicloActivoOperativoDeDepartamento(Departamento d ) throws Exception{
        Transaction t = persistence.createTransaction();
        List<Ciclo> l = persistence.getEntityManager().createQuery("select c from test1_Ciclo c join c.departamento d where c.estadoCiclo = :estado " +
                " and c.tipoCiclo = :tipo and d.id = :did").setParameter("estado", EstadoCicloEnum.ACTIVO)
                .setParameter("tipo", TipoCiclo.OPERATIVO).setParameter("did", d.getId()).getResultList();
        if (l.size()==1){
            return l.get(0);
        }else{
            throw new Exception("Se halló mas de un ciclo activo, o ninguno para el departamento : " + d.getNombreDescriptivoCompleto());
        }
    }

    public List<Entrada> getEntradasConOrdenesTrabajoSinAsignacionesTareas(Boolean ocupadosVacios) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select e.id, ot.id, d.id, count(at.id) from test1_OrdenTrabajo ot " +
                " left join ot.asignacionesTareas at " +
                " join ot.entrada e join e.ciclo c join c.departamento d " +
                "" +
                " where c.estadoCiclo = 1 and c.tipoCiclo = 'OPERATIVO' and d.piso <> 'FINCA' " +
                " and (ot.excluirDeMonitorizacionEncargado = false or ot.excluirDeMonitorizacionEncargado is null) group by e.id, ot.id";

        /*hql = "select e.id, ot.id, count(at.id) from test1_OrdenTrabajo ot " +
                " left join ot.asignacionesTareas at " +
                " join ot.entrada e group by e.id, ot.id";*/

        EntityManager em = persistence.getEntityManager();
        List l = em.createQuery(hql)
                .getResultList();
        t.close();
        ArrayList<UUID> entradasIds = new ArrayList();
        ArrayList<UUID> departamentosIds = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            Object[] oo = (Object[]) l.get(i);
            Number num = (Number) oo[3];
            if (num.intValue()==0){
                UUID eid = (UUID) oo[0];
                UUID did = (UUID) oo[2];
                entradasIds.add(eid);
                departamentosIds.add(did);
            }
        }

        hql = "select ci.id, d.id from test1_ContratoInquilino ci join ci.departamento d " +
                "where d.id in :didlist and ci.estadoContrato = 3";

        t = persistence.createTransaction();
        em = persistence.getEntityManager();
        List res = em.createQuery(hql).setParameter("didlist", departamentosIds).getResultList();
        t.close();

        //creando la lista de departamentos de la lista que tiene contrato vigente y otra que no
        //tiene contrato vigente
        ArrayList didsContratoVigente = new ArrayList();
        ArrayList didsSinContratoVigente = new ArrayList();
        for (int i = 0; i < res.size(); i++) {
            UUID did = (UUID) ((Object[])res.get(i))[1];
            if (didsContratoVigente.indexOf(did)==-1) didsContratoVigente.add(did);
        }
        for (int i = 0; i < departamentosIds.size(); i++) {
            UUID did = (UUID) departamentosIds.get(i);
            if (didsContratoVigente.indexOf(did)==-1){
                if (didsSinContratoVigente.indexOf(did)==-1){
                    didsSinContratoVigente.add(did);
                }
            }
        }

        if (ocupadosVacios==null){
            //devuelvo todas las entradas
            t = persistence.createTransaction();
            em = persistence.getEntityManager();
            List<Entrada> entradas = em.createQuery("select e from test1_Entrada e where " +
                    "e.id in :entradalist")
                    .setView(Entrada.class, "entrada-to-departamento")
                    .setParameter("entradalist", entradasIds)
                    .getResultList();
            t.close();
            return entradas;

        }else if(ocupadosVacios==true){
            //devuelvo solo aquellas entradas cuyo departamento tiene un contrato vigente
            t = persistence.createTransaction();
            em = persistence.getEntityManager();
            List<Entrada> entradas = em.createQuery("select e from test1_Entrada e join e.ciclo c " +
                    "join c.departamento d where " +
                    "e.id in :entradalist and d.id in :departamentolist")
                    .setView(Entrada.class, "entrada-to-departamento")
                    .setParameter("entradalist", entradasIds)
                    .setParameter("departamentolist", didsContratoVigente)
                    .getResultList();
            t.close();

            return entradas;

        }
        else{
            //devuelvosolo aquellas entradas cuyo departamento no tiene asignado un contrato vigente
            //devuelvo solo aquellas entradas cuyo departamento tiene un contrato vigente
            t = persistence.createTransaction();
            em = persistence.getEntityManager();
            List<Entrada> entradas = em.createQuery("select e from test1_Entrada e join e.ciclo c " +
                    "join c.departamento d where " +
                    "e.id in :entradalist and d.id in :departamentolist")
                    .setView(Entrada.class, "entrada-to-departamento")
                    .setParameter("entradalist", entradasIds)
                    .setParameter("departamentolist", didsSinContratoVigente)
                    .getResultList();
            t.close();

            return entradas;
        }
    }

}