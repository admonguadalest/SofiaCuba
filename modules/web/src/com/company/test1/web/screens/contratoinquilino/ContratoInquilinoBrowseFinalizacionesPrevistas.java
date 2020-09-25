package com.company.test1.web.screens.contratoinquilino;

import com.company.test1.service.ContratosService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@UiController("test1_ContratoInquilinoFinalizacionesPrevistas.browse")
@UiDescriptor("contrato-inquilino-browse-finalizacionesprevistas.xml")
@LookupComponent("contratoInquilinoesTable")
@LoadDataBeforeShow
public class ContratoInquilinoBrowseFinalizacionesPrevistas extends StandardLookup<ContratoInquilino> {

    @Inject
    private CollectionLoader<ContratoInquilino> contratoInquilinoesDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private GroupTable<ContratoInquilino> contratoInquilinoesTable;
    @Inject
    private ExportDisplay exportDisplay;

    /*@Install(to = "contratoInquilinoesDl", target = Target.DATA_LOADER)
    private List<ContratoInquilino> contratoInquilinoesDlLoadDelegate(LoadContext<ContratoInquilino> loadContext) {
        Date d = DateUtils.addMonths(new Date(), 3);
        String hql = "select e from test1_ContratoInquilino e where e.fechaVencimientoPrevisto <= :d and e.fechaVencimientoPrevisto >= :today";
        List<ContratoInquilino> l = dataManager.loadValue(hql, ContratoInquilino.class).parameter("d", d).parameter("today", new Date()).list();
        for (int i = 0; i < l.size(); i++) {
            ContratoInquilino ci = l.get(i);
            ci = dataManager.reload(ci, "contratoInquilino-list");
            l.set(i, ci);
        }
        return l;
    }*/


    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Finalizaciones Previstas", ContratoInquilino.class, contratoInquilinoesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Finalizaciones Previstas.pdf");
    }
}