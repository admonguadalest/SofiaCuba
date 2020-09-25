package com.company.test1.web.screens.externos;

import com.company.test1.entity.extroles.Propietario;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.SerializacionService;
import com.company.test1.web.GuiUtils;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.sql.Date;

@UiController("test1_Mod347")
@UiDescriptor("mod-347.xml")
public class Mod347 extends Screen {

    @Inject
    private CollectionContainer<Propietario> propietariosDc;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private LookupField<Propietario> lkpPropietarios;
    @Inject
    private DateField<Date> dteFechaHasta;
    @Inject
    private DateField<Date> dteFechaDesde;
    @Inject
    private Notifications notifications;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private SerializacionService serializacionService;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        propietariosDl.load();
    }
    


    public void onBtnDescargarImportableClick() {
        if (GuiUtils.isAnyValueNull(lkpPropietarios, dteFechaDesde, dteFechaHasta)){
            notifications.create().withCaption("Especificar Propietario, Fecha Desde y Fecha Hasta").show();
            return;
        }
        try{
            String s = serializacionService.reportMod347(lkpPropietarios.getValue(), dteFechaDesde.getValue(), dteFechaHasta.getValue());
            exportDisplay.show(new ByteArrayDataProvider(s.getBytes()), "Mod347.txt");
        }catch(Exception exc){
            notifications.create().withCaption("No se pudo generar descargable - " + exc.getMessage());
        }
    }

    public void onBtnPrevisualizarClick() {
        if (GuiUtils.isAnyValueNull(lkpPropietarios, dteFechaDesde, dteFechaHasta)){
            notifications.create().withCaption("Especificar Propietario, Fecha Desde y Fecha Hasta").show();
            return;
        }
        try {
            byte[] bb = jasperReportService.reportMod347(lkpPropietarios.getValue(), dteFechaDesde.getValue(), dteFechaHasta.getValue());
            exportDisplay.show(new ByteArrayDataProvider(bb), "Resumen 347.pdf");
        }catch(Exception exc){
            notifications.create().withCaption("Error al generar previsualizacion");
        }

    }
}