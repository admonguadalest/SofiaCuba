package com.company.test1.web.screens.tipocoeficiente;

import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.coeficientes.TipoCoeficiente;

import javax.inject.Inject;

@UiController("test1_TipoCoeficiente.browse")
@UiDescriptor("tipo-coeficiente-browse.xml")
@LookupComponent("tipoCoeficientesTable")
@LoadDataBeforeShow
public class TipoCoeficienteBrowse extends StandardLookup<TipoCoeficiente> {
    @Inject
    private Table<TipoCoeficiente> tipoCoeficientesTable;
    @Inject
    private ExportDisplay exportDisplay;

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Tipos de Coeficientes para Ubicaciones", TipoCoeficiente.class, tipoCoeficientesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Tipos de Coeficientes Ubicaciones.pdf", ExportFormat.getByExtension("pdf"));
    }
}