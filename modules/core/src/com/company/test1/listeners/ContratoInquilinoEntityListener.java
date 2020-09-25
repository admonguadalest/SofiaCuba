package com.company.test1.listeners;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component("test1_ContratoInquilinoEntityListener")
public class ContratoInquilinoEntityListener implements BeforeInsertEntityListener<ContratoInquilino>, BeforeUpdateEntityListener<ContratoInquilino> {


    @Override
    public void onBeforeInsert(ContratoInquilino entity, EntityManager entityManager) {
        int y = 2;

    }

    @Override
    public void onBeforeUpdate(ContratoInquilino entity, EntityManager entityManager) {
        int y = 2;

    }
}