package com.company.test1.listeners;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component("test1_UbicacionEntityListener")
public class UbicacionEntityListener implements BeforeInsertEntityListener<Ubicacion>, BeforeUpdateEntityListener<Ubicacion> {


    @Override
    public void onBeforeInsert(Ubicacion ubicacion, EntityManager entityManager) {
        /**
         * generate new rm2id
         */
        String sql = "SELECT max(RM2ID) as maxrm2id FROM cubatest1.ubicacion;";
        Number n = (Number) entityManager.createNativeQuery(sql).getFirstResult();
        ubicacion.setRm2id(n.intValue() + 1);
    }

    @Override
    public void onBeforeUpdate(Ubicacion ubicacion, EntityManager entityManager) {

    }
}