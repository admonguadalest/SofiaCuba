package com.company.test1.web.screens.notificaciones;

import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.service.NotificacionService;
import com.company.test1.service.PdfService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.notificaciones.Notificacion;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_Notificacion.browse")
@UiDescriptor("notificacion-browse.xml")
@LookupComponent("notificacionsTable")
@LoadDataBeforeShow
public class NotificacionBrowse extends StandardLookup<Notificacion> {

    @Inject
    private Table<NotificacionContratoInquilino> notificacionsTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private PdfService pdfService;
    @Inject
    private NotificacionService notificacionService;

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Notificaciones", NotificacionContratoInquilino.class, notificacionsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Notificaciones.pdf");
    }

    public void onBtnPdfClick() {


        byte[] bb = notificacionService.getVersionPdfConcatenada(new ArrayList(notificacionsTable.getSelected()));
        exportDisplay.show(new ByteArrayDataProvider(bb), "Notificaciones.pdf");
    }
}