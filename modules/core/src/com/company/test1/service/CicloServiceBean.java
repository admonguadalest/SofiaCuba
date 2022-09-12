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

    public List<Entrada> getEntradasConOrdenesTrabajoSinAsignacionesTareas() throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select e.id, ot.id, count(at.id) from test1_OrdenTrabajo ot " +
                " left join ot.asignacionesTareas at " +
                " join ot.entrada e join e.ciclo c join c.departamento d " +
                " where c.estadoCiclo = 1 and c.tipoCiclo = 'OPERATIVO' and d.piso <> 'FINCA' " +
                " and (ot.excluirDeMonitorizacionEncargado = false or ot.excluirDeMonitorizacionEncargado is null) group by e.id, ot.id";

        /*hql = "select e.id, ot.id, count(at.id) from test1_OrdenTrabajo ot " +
                " left join ot.asignacionesTareas at " +
                " join ot.entrada e group by e.id, ot.id";*/

        EntityManager em = persistence.getEntityManager();
        List l = em.createQuery(hql)
                .getResultList();
        t.close();
        ArrayList<Entrada> al = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            Object[] oo = (Object[]) l.get(i);
            Number num = (Number) oo[2];
            if (num.intValue()==0){
                UUID uuid = (UUID) oo[0];
                Entrada e = dataManager.load(Entrada.class).view("entrada-to-departamento").id(uuid).one();
                al.add(e);
            }
        }

        return al;
    }

}