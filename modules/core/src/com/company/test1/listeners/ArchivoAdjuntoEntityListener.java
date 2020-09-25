package com.company.test1.listeners;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.service.ColeccionArchivosAdjuntosServiceBean;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.util.UUID;

@Component("test1_ArchivoAdjuntoEntityListener")
public class ArchivoAdjuntoEntityListener implements BeforeInsertEntityListener<ArchivoAdjunto>, BeforeUpdateEntityListener<ArchivoAdjunto> {

    @Inject
    Persistence persistence;

    public static boolean activated = true;


    @Override
    public void onBeforeInsert(ArchivoAdjunto entity, EntityManager entityManager){
        int y = 2;

        try {
            if (activated)
                AppBeans.get(ColeccionArchivosAdjuntosService.class).generaNuevoArchivoAdjuntoExt(entity);
        } catch (Exception exc){
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void onBeforeUpdate(ArchivoAdjunto entity, EntityManager entityManager) {
        int y = 2;

    }
}