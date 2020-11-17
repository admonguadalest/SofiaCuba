package com.company.test1.core;

import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.listener.AfterCompleteTransactionListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component(HelperRecibosInformeIva.NAME)
public class HelperRecibosInformeIva implements AfterCompleteTransactionListener {
    public static final String NAME = "test1_HelperRecibosInformeIva";

    @Inject
    Persistence persistence;
    @Inject
    DataManager dataManager;

    @Inject
    Resources resources;

    @Override
    public void afterComplete(boolean committed, Collection<Entity> detachedEntities) {
        ArrayList al = new ArrayList(detachedEntities);
        for (int i = 0; i < al.size(); i++) {
            Object o =  al.get(i);
            if (o instanceof Recibo){

//                procesaRecibo(((Recibo)o));
            }

        }
    }

    public void _procesaRecibo(UUID id){

        String rid = id.toString().replace("-", "");

        String sql = resources.getResourceAsString("classpath:com/company/test1/core/sql_background_task_z_helper.sql");
        sql = sql.replace("[no_recibo]", rid);
        Transaction t = persistence.createTransaction();
        String iddelete = id.toString().replace("-", "");
        String deleteSql = "delete from z_helper_proceso_recibos_informeiva WHERE recibo_id = '" + iddelete + "'";
        int res = persistence.getEntityManager().createNativeQuery(deleteSql).executeUpdate();
        t.commit();
        t = persistence.createTransaction();
        res = persistence.getEntityManager().createNativeQuery(sql).executeUpdate();
        t.commit();
    }

    public void procesaRecibo(Recibo recibo, Persistence persistence){
        retrocedeReciboEnTablaZHelper(recibo.getId(), persistence);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Transaction t = persistence.createTransaction();

        Recibo r = recibo;
//        r = dataManager.reload(r, "recibo-view");
        double porcentajeIva = 0.0;
        double importeIva = 0.0;
        double porcentajeIrpf = 0.0;
        double importeIrpf = 0.0;



        for (int i = 0; i < recibo.getImplementacionesConceptos().size(); i++) {
            ImplementacionConcepto ic = recibo.getImplementacionesConceptos().get(i);
            List<RegistroAplicacionConceptoAdicional> racas = ic.getRegistroAplicacionesConceptosAdicionales();
            for (int j = 0; j < racas.size(); j++) {
                RegistroAplicacionConceptoAdicional raca = racas.get(j);
                if (raca.getConceptoAdicional().getAbreviacion().compareTo("IVA")==0){
                    porcentajeIva = raca.getPorcentaje().doubleValue();
                    if (ic.getConcepto().getAdicionSustraccion()){
                        importeIva += raca.getImporteAplicado();
                    }else{
                        importeIva -= raca.getImporteAplicado();
                    }

                }
                if (raca.getConceptoAdicional().getAbreviacion().compareTo("IRPF")==0){
                    porcentajeIrpf = raca.getPorcentaje().doubleValue();
                    importeIrpf += raca.getImporteAplicado();
                    if (ic.getConcepto().getAdicionSustraccion()){
                        //se alteran los signos porque irpf es tipo sustraccion
                        importeIrpf -= raca.getImporteAplicado();
                    }else{
                        importeIrpf += raca.getImporteAplicado();
                    }
                }
            }

        }

        String nombreUbicacion = r.getUtilitarioContratoInquilino().getDepartamento().getUbicacion().getNombre();
        nombreUbicacion = nombreUbicacion.replace("'","`");
        String nombreInquilino = r.getUtilitarioInquilino().getNombreCompleto();
        nombreInquilino = nombreInquilino.replace("'","`");

        String sql = "insert into z_helper_proceso_recibos_informeiva\n" +
                "(ubicacion_rm2id," +
                "ubicacion_id, " +
                "departamento_rm2id, " +
                "departamento_id, " +
                "recibo_id, " +
                "inquilino_id, " +
                "fechaEmision, " +
                "numRecibo, " +
                "nifDniInquilino," +
                "nombreCompleto,\n" +
                "nombre_ubicacion," +
                "piso, " +
                "puerta, " +
                "totalRecibo, " +
                "totalReciboPostCCAA, " +
                "importe, " +
                "importePostCCAA, " +
                "base, " +
                "porcentajeIVA,\n" +
                "importeAplicadoIVA, " +
                "porcentajeIRPF, " +
                "importeAplicadoIRPF) " +
                "values " +
                "(" + r.getUtilitarioContratoInquilino().getDepartamento().getUbicacion().getRm2id().toString() + "," +
                "'" + r.getUtilitarioContratoInquilino().getDepartamento().getUbicacion().getId().toString().replace("-","") +"', " +
                r.getUtilitarioContratoInquilino().getDepartamento().getRm2id().toString() + "," +
                "'" + r.getUtilitarioContratoInquilino().getDepartamento().getId().toString().replace("-","") +"', " +
                "'" + r.getId().toString().replace("-","") +"', " +
                "'" + r.getUtilitarioInquilino().getId().toString().replace("-","") +"', " +
                "'" + sdf.format(r.getFechaEmision()) +"', " +
                "'" + r.getNumRecibo() +"', " +
                "'" + r.getUtilitarioInquilino().getNifDni() +"', " +
                "'" + nombreInquilino +"', " +
                "'" + nombreUbicacion +"', " +
                "'" + r.getUtilitarioContratoInquilino().getDepartamento().getPiso() +"', " +
                "'" + r.getUtilitarioContratoInquilino().getDepartamento().getPuerta() +"', " +
                r.getTotalRecibo() + ", " +
                r.getTotalReciboPostCCAA() + ", " +
                r.getTotalRecibo() + ", " +
                r.getTotalReciboPostCCAA() + ", " +
                r.getTotalRecibo() + ", " +
                porcentajeIva + ", " +
                importeIva + ", " +
                porcentajeIrpf + ", " +
                importeIrpf + ") ";
        int res = persistence.getEntityManager().createNativeQuery(sql).executeUpdate();
        t.commit();
        t.close();
    }

    public void retrocedeReciboEnTablaZHelper(UUID id, Persistence persistence){
        Transaction t = persistence.createTransaction();
        String iddelete = id.toString().replace("-", "");
        String deleteSql = "delete from z_helper_proceso_recibos_informeiva WHERE recibo_id = '" + iddelete + "'";
        int res = persistence.getEntityManager().createNativeQuery(deleteSql).executeUpdate();
        t.commit();
    }

}