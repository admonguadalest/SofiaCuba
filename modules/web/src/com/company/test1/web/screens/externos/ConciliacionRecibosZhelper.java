package com.company.test1.web.screens.externos;

import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import javax.persistence.Persistence;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



@UiController("test1_ConciliacionRecibosZhelper")
@UiDescriptor("conciliacion-recibos-zhelper.xml")
public class ConciliacionRecibosZhelper extends Screen {

    @Inject
    private Table tableRecibos;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataManager dataManager;
    @Inject
    private RecibosService recibosService;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionLoader<Recibo> recibosDl;
    @Inject
    private DateField<Date> fechaHastaField;
    @Inject
    private DateField<Date> fechaDesdeField;
    @Inject
    private CollectionContainer<Recibo> recibos;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

    }

    public void buscar(){
        if ((fechaDesdeField.getValue()==null) || (fechaHastaField.getValue()==null)){
            notifications.create().withCaption("Rellenar Datos").withDescription("Inserir fecha desde y fecha hasta").show();
            return;
        }
        try{
            List<Recibo> rr = recibosService.devuelveRecibosPendientesDeConciliacionZHelper(fechaDesdeField.getValue(), fechaHastaField.getValue());
            for (int i = 0; i < rr.size(); i++) {
                rr.set(i, dataManager.reload(rr.get(i), "recibo-browse-view"));
            }
            recibos.setItems(rr);
        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription(exc.getMessage()).show();
        }
    }

    public void onBtnConciliarSeleccionados(){
        ArrayList<Recibo> al = new ArrayList(tableRecibos.getSelected());
        try {

            for (int i = 0; i < al.size(); i++) {
                Recibo r = al.get(i);
                r = dataManager.reload(r, "recibo-detalle-view");
                recibosService.registraReciboEnTablaZHelper(r);
            }
        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription(exc.getMessage()).show();
            return;

        }
        buscar();
        notifications.create().withCaption("Conciliación Registrada").withDescription("Los recibos seleccionados fueron registrados corréctamente en el registro de conciliación").show();
    }







}