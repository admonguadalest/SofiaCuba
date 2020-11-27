package com.company.test1.jmx;

import com.company.test1.entity.Persona;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.List;

@Component("test1_Ajustes")
public class Ajustes implements AjustesMBean {

    public void realizaAjustePoblarTitularDocumentosProveedor() throws Exception{
        String hql = "SELECT dp.id FROM test1_FacturaProveedor dp";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<UUID> ids = AppBeans.get(Persistence.class).getEntityManager().createQuery(hql).getResultList();
        t.close();
        for (int i = 0; i < ids.size(); i++) {
            UUID id = ids.get(i);
            FacturaProveedor fp = null;
            try {
                t = AppBeans.get(Persistence.class).createTransaction();
                fp = AppBeans.get(Persistence.class).getEntityManager().find(FacturaProveedor.class, id, "facturaProveedor-view");
                Persona titular = fp.getImputacionesDocumentoImputable().get(0).getCiclo().getDepartamento().getPropietarioEfectivo().getPersona();
                fp.setTitular(titular);
                t.close();
                AppBeans.get(DataManager.class).commit(fp);
            }catch(Exception exc){
                t.close();
            }

        }

    }

}