package com.company.test1.web.screens.definicionremesa;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.DefinicionRemesa;

import javax.inject.Inject;

@UiController("test1_DefinicionRemesa.browse")
@UiDescriptor("definicion-remesa-browse.xml")
@LookupComponent("definicionRemesasTable")
@LoadDataBeforeShow
public class DefinicionRemesaBrowse extends StandardLookup<DefinicionRemesa> {

    @Inject
    private GroupTable<DefinicionRemesa> definicionRemesasTable;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;

    @Subscribe("definicionRemesasTable.edit")
    private void onDefinicionRemesasTableEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(definicionRemesasTable.getSingleSelected(), null, null, screenBuilders, this,
                OpenMode.NEW_TAB, null, null);
    }


    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Definiciones de Remesa", DefinicionRemesa.class, definicionRemesasTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Departamentos.pdf", ExportFormat.getByExtension("pdf"));
    }
}