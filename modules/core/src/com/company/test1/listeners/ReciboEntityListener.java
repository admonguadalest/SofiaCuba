package com.company.test1.listeners;

import com.company.test1.core.HelperRecibosInformeIva;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.AfterDeleteEntityListener;
import com.haulmont.cuba.core.listener.AfterInsertEntityListener;
import com.haulmont.cuba.core.listener.AfterUpdateEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.sql.Connection;
import java.util.UUID;

@Component("test1_ReciboEntityListener")
public class ReciboEntityListener implements AfterUpdateEntityListener<Recibo>, AfterInsertEntityListener<Recibo>, AfterDeleteEntityListener<Recibo> {

    public static boolean activated = true;

    @Override
    public void onAfterDelete(Recibo entity, Connection connection) {
        if (activated)
            AppBeans.get(HelperRecibosInformeIva.class).retrocedeReciboEnTablaZHelper(entity.getId());
    }

    @Override
    public void onAfterInsert(Recibo entity, Connection connection) {
        if (activated)
            AppBeans.get(HelperRecibosInformeIva.class).procesaRecibo(entity);
    }

    @Override
    public void onAfterUpdate(Recibo entity, Connection connection) {
        if (activated)
            AppBeans.get(HelperRecibosInformeIva.class).procesaRecibo(entity);
    }
}