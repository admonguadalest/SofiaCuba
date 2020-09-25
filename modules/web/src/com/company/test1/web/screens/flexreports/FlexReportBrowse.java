package com.company.test1.web.screens.flexreports;

import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.service.JasperReportService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.reportsyplantillas.FlexReport;

import javax.inject.Inject;

@UiController("test1_FlexReport.browse")
@UiDescriptor("flex-report-browse.xml")
@LookupComponent("flexReportsTable")
@LoadDataBeforeShow
public class FlexReportBrowse extends StandardLookup<FlexReport> {
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Table<FlexReport> flexReportsTable;

    public void onCrearReportClick() {
        try {
            byte[] bb = jasperReportService.createReport();
            exportDisplay.show(new ByteArrayDataProvider(bb), "test.pdf", ExportFormat.getByExtension("pdf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("FlexReports", FlexReport.class, flexReportsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "FlexReports.pdf", ExportFormat.getByExtension("pdf"));
    }

}