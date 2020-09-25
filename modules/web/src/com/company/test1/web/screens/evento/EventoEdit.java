package com.company.test1.web.screens.evento;

import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.Evento;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Date;

@UiController("test1_Evento.edit")
@UiDescriptor("evento-edit.xml")
@EditedEntityContainer("eventoDc")
@LoadDataBeforeShow
public class EventoEdit extends StandardEditor<Evento> {

    @Inject
    private InstanceLoader<Evento> eventoDl;

    @Inject
    private InstanceContainer<Evento> eventoDc;

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {

        eventoDl.load();

        eventoDc.getItem().setFecha(new Date());

        
    }
    
    
}