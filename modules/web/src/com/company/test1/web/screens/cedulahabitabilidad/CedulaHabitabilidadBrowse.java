package com.company.test1.web.screens.cedulahabitabilidad;

import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.CedulaHabitabilidad;

import javax.inject.Inject;

@UiController("test1_CedulaHabitabilidad.browse")
@UiDescriptor("cedula-habitabilidad-browse.xml")
@LookupComponent("cedulaHabitabilidadsTable")
@LoadDataBeforeShow
public class CedulaHabitabilidadBrowse extends StandardLookup<CedulaHabitabilidad> {

    @Inject
    private GroupTable<CedulaHabitabilidad> cedulaHabitabilidadsTable;
    @Inject
    private ExportDisplay exportDisplay;

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("CCHH", CedulaHabitabilidad.class, cedulaHabitabilidadsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Conceptos.pdf", ExportFormat.getByExtension("pdf"));

    }

}