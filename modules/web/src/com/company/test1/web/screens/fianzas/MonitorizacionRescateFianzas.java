package com.company.test1.web.screens.fianzas;

import com.company.test1.entity.contratosinquilinos.Fianza;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.List;

@UiController("test1_MonitorizacionRescateFianzas")
@UiDescriptor("monitorizacion-rescate-fianzas.xml")
public class MonitorizacionRescateFianzas extends Screen {
    @Inject
    private DataManager dataManager;
    @Inject
    private DateField<Date> fechaDesdeField;
    @Inject
    private CollectionLoader<Fianza> fianzasDl;
    @Inject
    private DateField<Date> fechaHastaField;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataGrid<Fianza> dataGridFianzas;
    @Inject
    private Notifications notifications;

    @Subscribe("btnBuscar")
    public void onBtnBuscarClick(Button.ClickEvent event) {
        fianzasDl.load();
    }



    @Install(to = "fianzasDl", target = Target.DATA_LOADER)
    private List<Fianza> fianzasDlLoadDelegate(LoadContext<Fianza> loadContext) {
        String eql = "select f from test1_Fianza f join f.contratoInquilino ci where ci.estadoContrato in (5,6,7) and " +
                "ci.fechaDesocupacion >= :fd and ci.fechaDesocupacion <= :fh ";
        List<Fianza> ff = dataManager.load(Fianza.class).query(eql).view("fianza-monitorizacion")
                .parameter("fd", fechaDesdeField.getValue())
                .parameter("fh", fechaHastaField.getValue())
                .list();
        return ff;
    }

    @Subscribe("dataGridFianzas.editFianza")
    public void onDataGridFianzasEditFianza(Action.ActionPerformedEvent event) {
        Fianza f = dataGridFianzas.getSingleSelected();
        if (f == null){
            notifications.create().withCaption("Seleccione un registro").show();
            return;
        }
        screenBuilders.editor(Fianza.class, this).editEntity(f).withOpenMode(OpenMode.DIALOG).build().show();
    }





}