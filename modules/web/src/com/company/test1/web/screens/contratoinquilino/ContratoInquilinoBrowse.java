package com.company.test1.web.screens.contratoinquilino;

import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.notificaciones.NotificacionGenericaManual;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_ContratoInquilino.browse")
@UiDescriptor("contrato-inquilino-browse.xml")
@LookupComponent("contratoInquilinoesTable")
@LoadDataBeforeShow
public class ContratoInquilinoBrowse extends StandardLookup<ContratoInquilino> {
    @Inject
    private GroupTable<ContratoInquilino> contratoInquilinoesTable;
    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private Filter filter;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe("contratoInquilinoesTable.edit")
    private void onContratoInquilinoesTableEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(contratoInquilinoesTable.getSingleSelected(), null, null,
                screenBuilders, this, OpenMode.NEW_TAB, null, null);
    }

    @Subscribe("contratoInquilinoesTable.create")
    private void onContratoInquilinoesTableCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(ContratoInquilino.class, null, null,
                screenBuilders, this, OpenMode.NEW_TAB, null, null);
    }

    public void OnBtnPrintClicked(){
        byte[] bb = DynamicReportHelper.getReportDinamico("Contratos", ContratoInquilino.class, contratoInquilinoesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "contratos.pdf");
    }

    public void preselectOnNewManualNotification(){
        List<ContratoInquilino> cc = new ArrayList(contratoInquilinoesTable.getSelected());
        NotificacionGenericaManual ngm = (NotificacionGenericaManual) screenBuilders.screen(this).withScreenId("test1_NotificacionGenericaManual").withOpenMode(OpenMode.NEW_TAB).build();
        ngm.show();
        ngm.preselectContratosInquilinos(cc);

    }

    

}