package com.company.test1.web.screens.registrosiva;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.sql.Date;

@UiController("test1_AlteracionRegistrosIva")
@UiDescriptor("alteracion-registros-iva.xml")
public class AlteracionRegistrosIva extends Screen {

    @Inject
    private DateField<Date> dteFechaDesde;
    @Inject
    private DateField<Date> dteFechaHasta;
    @Inject
    private Label<String> lblResultados;
    @Inject
    private TextField<String> txtDireccion;
    @Inject
    private TextField<String> txtInquilino;
    @Inject
    private Table tblRegistros;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        prepareTable();
    }


    void prepareTable(){

    }

    public void onBtnBuscarClick() {
        String sql = "select * from z_helper_proceso_recibos_informeiva WHERE id > 0 ORDER by id desc limit 0,10";
        
        
    }
}