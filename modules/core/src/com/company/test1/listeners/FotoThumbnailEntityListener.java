package com.company.test1.listeners;

import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
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

@Component("test1_FotoThumbnailEntityListener")
public class FotoThumbnailEntityListener  implements BeforeInsertEntityListener<FotoThumbnail>, BeforeUpdateEntityListener<FotoThumbnail> {

    @Inject
    Persistence persistence;


    @Override
    public void onBeforeInsert(FotoThumbnail entity, EntityManager entityManager){
        int y = 2;

        try {
            if (ArchivoAdjuntoEntityListener.activated)
                AppBeans.get(ColeccionArchivosAdjuntosService.class).generaNuevoFotoThumbnailExt(entity);
        } catch (Exception exc){
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void onBeforeUpdate(FotoThumbnail entity, EntityManager entityManager) {
        int y = 2;

    }
}