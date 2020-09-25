package com.company.test1.listeners;

import com.company.test1.entity.contratosinquilinos.Anexo;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component("test1_AnexoChangedListener")
public class AnexoChangedListener {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    private void beforeCommit(EntityChangedEvent<Anexo, UUID> event) {

    }
}