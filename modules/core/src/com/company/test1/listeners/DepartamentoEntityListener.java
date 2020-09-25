package com.company.test1.listeners;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.util.UUID;

@Component("test1_DepartamentoEntityListener")
public class DepartamentoEntityListener implements BeforeInsertEntityListener<Departamento>, BeforeUpdateEntityListener<Departamento> {

    @Inject
    Persistence persistence;
    @Override
    public void onBeforeInsert(Departamento departamento, EntityManager entityManager) {
        /**
         * generate new rm2id
         */
        String sql = "SELECT max(RM2ID) as maxrm2id FROM cubatest1.departamento;";
        Number n = (Number) entityManager.createNativeQuery(sql).getFirstResult();
        departamento.setRm2id(n.intValue() + 1);

    }

    @Override
    public void onBeforeUpdate(Departamento departamento, EntityManager entityManager) {

    }
}