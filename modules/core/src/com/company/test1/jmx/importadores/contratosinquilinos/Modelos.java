package com.company.test1.jmx.importadores.contratosinquilinos;

import com.company.test1.entity.enums.ModeloContratoInquilinoPropietario;
import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.modeloscontratosinquilinos.Seccion;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Modelos {

    public void realizaImportacionModelos(DataManager dataManager, Persistence persistence) throws Exception{
        ArrayList topersist = new ArrayList();
        String sql = "select * from modelos_contratos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            ModeloContrato mc = new ModeloContrato();
            topersist.add(mc);
            mc.setRm2id(r.getInt("id"));
            mc.setDescripcion(r.getString("descripcion"));
            mc.setNombreModelo(r.getString("nombreModelo"));
            mc.setInquilinoPropietario(ModeloContratoInquilinoPropietario.fromId(r.getString("inquilinoPropietario")));
            String sqls = "select * from secciones where modelo_contrato_id = " + r.getInt("id");
            ResultSet rs = Rentamaster2DB.getResultSet(sqls);
            while(rs.next()){
                Seccion s = new Seccion();
                topersist.add(s);
                s.setModeloContrato(mc);
                mc.getSecciones().add(s);
                s.setDescripcion(rs.getString("descripcion"));
                s.setNombre(rs.getString("nombreSeccion"));
                s.setObligatoria(rs.getBoolean("obligatoria"));
                s.setOrdenacion(rs.getInt("ordenacion"));
                String sqlc = "select * from clausulas where seccion_id = " + rs.getInt("id");
                ResultSet rc = Rentamaster2DB.getResultSet(sqlc);
                while(rc.next()){
                    Clausula c = new Clausula();
                    topersist.add(c);
                    c.setRmi2(rc.getInt("id"));
                    s.getClausulas().add(c);
                    c.setSeccion(s);
                    c.setDescripcion(rc.getString("descripcion"));
                    c.setNombreClausula(rc.getString("nombreClausula"));
                    c.setOrdenacion(rc.getInt("ordenacion"));
                    String sqlv = "select * from versiones_clausulas where clausula_id = " + rc.getInt("id");
                    ResultSet rvc = Rentamaster2DB.getResultSet(sqlv);
                    while(rvc.next()){
                        VersionClausula vc = new VersionClausula();
                        topersist.add(vc);
                        vc.setRmi2(rvc.getInt("id"));
                        vc.setClausula(c);
                        c.getVersiones().add(vc);
                        vc.setDescripcionParametros(rvc.getString("descripcionParametros"));
                        vc.setEsPredeterminada(rvc.getBoolean("esPredeterminada"));
                        vc.setExpresionesValoresDefecto(rvc.getString("expresionesValoresDefecto"));
                        vc.setNombresParametros(rvc.getString("nombresParametros"));
                        vc.setTextoVersion(rvc.getString("textoVersion"));
                    }
                    rvc.close();


                }
                rc.close();

            }
            rs.close();

            dataManager.commit(new CommitContext(topersist));
            topersist.clear();

        }
        r.close();


    }

    public ModeloContrato getModeloDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select mc from test1_ModeloContrato mc where mc.rm2id = " + id;
        ModeloContrato mc = (ModeloContrato) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return mc;

    }

    public Seccion getSeccionDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select mc from test1_Seccion mc where mc.rm2id = " + id;
        Seccion mc = (Seccion) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return mc;

    }

    public Clausula getClausulaDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select mc from test1_Clausula mc where mc.rmi2 = " + id;
        Clausula mc = (Clausula) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return mc;

    }

    public VersionClausula getVersionClausulaDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select mc from test1_VersionClausula mc where mc.rmi2 = " + id;
        VersionClausula mc = (VersionClausula) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return mc;

    }

}
