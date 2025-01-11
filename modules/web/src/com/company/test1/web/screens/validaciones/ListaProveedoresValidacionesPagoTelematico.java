package com.company.test1.web.screens.validaciones;

import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.service.ValidacionesService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_ListaProveedoresValidacionesPagoTelematico")
@UiDescriptor("lista-proveedores-validaciones-pago-telematico.xml")
public class ListaProveedoresValidacionesPagoTelematico extends Screen {
    @Inject
    private DateField<Date> fechaDesde;
    @Inject
    private DataManager dataManager;
    @Inject
    private ValidacionesService validacionesService;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionContainer<Proveedor> proveedoresDc;
    @Inject
    private CollectionLoader<Proveedor> proveedoresDl;
    @Inject
    private Table<Proveedor> tblProveedores;
    @Inject
    private ScreenBuilders screenBuilders;

    @Install(to = "proveedoresDl", target = Target.DATA_LOADER)
    private List<Proveedor> proveedoresDlLoadDelegate(LoadContext<Proveedor> loadContext) {
        try {
            java.util.Date fechaDese = fechaDesde.getValue();
            List<Proveedor> provs = validacionesService.devuelveIdsProveedoresConValidacionesPagoPendientes(fechaDesde.getValue());
            return provs;
        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage()).show();
        }
        return new ArrayList();
    }

    public void buscarProveedores(){
        proveedoresDl.load();
    }

    public void verValidaciones(){
        Proveedor p = tblProveedores.getSingleSelected();
        if (p==null){
            notifications.create().withCaption("Seleccionar un Proveedor").show();
            return;
        }
        Screen s = screenBuilders.screen(this).withScreenId("test1_GestionarValidaciones")
                .withOpenMode(OpenMode.NEW_TAB)
                .build().show();
        GestionarValidaciones gvs = (GestionarValidaciones) s;
        gvs.buscaValidacionesPendientesParaDatos(DocumentoImputableTipoEnum.FACTURA,
                fechaDesde.getValue(), p);
    }



}