package com.company.test1.web.screens.representacionlegal;

import com.company.test1.entity.recibos.RegistroIndiceReferencia;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RepresentacionLegal;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_RepresentacionLegal.browse")
@UiDescriptor("representacion-legal-browse.xml")
@LookupComponent("representacionLegalsTable")
@LoadDataBeforeShow
public class RepresentacionLegalBrowse extends StandardLookup<RepresentacionLegal> {
    @Inject
    private ExportDisplay exportDisplay;

    @Inject
    private CollectionLoader representacionLegalsDl;


    @Inject
    private Table<RepresentacionLegal> representacionLegalsTable;
    @Inject
    private DataManager dataManager;

    public void onPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Consulta de Representaciones Legales", RepresentacionLegal.class, representacionLegalsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Representaciones Legales.pdf", ExportFormat.getByExtension("pdf"));
    }



    


}