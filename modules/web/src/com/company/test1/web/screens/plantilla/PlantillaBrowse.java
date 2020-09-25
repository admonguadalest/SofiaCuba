package com.company.test1.web.screens.plantilla;

import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.reportsyplantillas.Plantilla;

import javax.inject.Inject;

@UiController("test1_Plantilla.browse")
@UiDescriptor("plantilla-browse.xml")
@LookupComponent("plantillasTable")
@LoadDataBeforeShow
public class PlantillaBrowse extends StandardLookup<Plantilla> {
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private GroupTable<Plantilla> plantillasTable;

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Plantillas", ModeloContrato.class, plantillasTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Plantillas.pdf", ExportFormat.getByExtension("pdf"));
    }
}