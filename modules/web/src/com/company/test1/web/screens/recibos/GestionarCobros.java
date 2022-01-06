package com.company.test1.web.screens.recibos;

import com.company.test1.service.JasperReportService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Recibo;

import javax.inject.Inject;

@UiController("test1_Recibo.browse")
@UiDescriptor("gestionar-cobros.xml")
@LookupComponent("reciboesTable")
@LoadDataBeforeShow
public class GestionarCobros extends StandardLookup<Recibo> {
    @Inject
    private Table<Recibo> reciboesTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Notifications notifications;

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Recibos", Recibo.class, reciboesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Recibos.pdf");
    }

    public void onBtnPdfClick() {
        Recibo r = reciboesTable.getSingleSelected();
        if (r==null){
            notifications.create().withCaption("Seleccionar un Recibo");
            return;
        }
        try {
            byte[] bb = jasperReportService.getReportRecibo(r);
            exportDisplay.show(new ByteArrayDataProvider(bb), "Recibo " + r.getNumRecibo() + ".pdf");
        }catch(Exception exc){
            notifications.create().withCaption("Error al generar el report:" + exc.getMessage()).show();
            return;
        }

    }
}