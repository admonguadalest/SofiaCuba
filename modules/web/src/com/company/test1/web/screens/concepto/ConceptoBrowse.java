package com.company.test1.web.screens.concepto;

import com.company.test1.entity.recibos.Concepto;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.core.global.DataManager;
import java.util.ArrayList;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@UiController("test1_Concepto.browse")
@UiDescriptor("concepto-browse.xml")
@LookupComponent("conceptoesTable")
@LoadDataBeforeShow
public class ConceptoBrowse extends StandardLookup<Concepto> {
    @Inject
    private GroupTable<Concepto> conceptoesTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;
    @Inject DataManager dataManager;
    @Inject
    DataLoader conceptoesDl;

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Conceptos", Concepto.class, conceptoesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Conceptos.pdf", ExportFormat.getByExtension("pdf"));

    }

    public void onBtnSubirClick() {
        Set<Concepto> s = conceptoesTable.getSelected();
        if (s.size()==0){
            notifications.create().withCaption("Seleccionar un concepto").show();
            return;
        }
        Concepto c = (Concepto) new ArrayList(s).get(0);
        Integer i = c.getOrdenacion();
        if (i==1){
            notifications.create().withCaption("El concepto seleccionado ya es el mas alto de la lista").show();
            return;
        }else{
            Concepto c2 = null;
            try {
                c2 = dataManager.loadValue("select c from test1_Concepto c where c.ordenacion = " + (i-1), Concepto.class).one();
            }catch(Exception exc){

            }
            if (c2 == null){
                notifications.create().withCaption("No se hallo concepto con ordenacion superior. Si es preciso corregir manualmente.").show();
                return;
            }
            Integer nord = c2.getOrdenacion();
            c2.setOrdenacion(i);
            c.setOrdenacion(nord);
            dataManager.commit(c, c2);

        }
        conceptoesDl.load();

    }

    public void onBtnBajarClick() {
        Set<Concepto> s = conceptoesTable.getSelected();
        if (s.size()==0){
            notifications.create().withCaption("Seleccionar un concepto").show();
            return;
        }
        Concepto c = (Concepto) new ArrayList(s).get(0);
        Integer i = c.getOrdenacion();
        Concepto c2 = null;
        try {
            c2 = dataManager.loadValue("select c from test1_Concepto c where c.ordenacion = " + (i + 1), Concepto.class).one();
        }catch(Exception exc){
            int y = 2;
        }
        if (c2 == null){
            notifications.create().withCaption("No se hallo concepto con ordenacion inferior. Si es preciso corregir manualmente.").show();
            return;
        }
        Integer nord = c2.getOrdenacion();
        c2.setOrdenacion(i);
        c.setOrdenacion(nord);
        dataManager.commit(c, c2);


        conceptoesDl.load();

    }

    public void onBtnRehacerOrdenacionClick() {
        List<Concepto> cc = dataManager.loadValue("select c from test1_Concepto c order by c.ordenacion", Concepto.class).list();
        for (int i = 0; i < cc.size(); i++) {
            cc.get(i).setOrdenacion(i+1);
            dataManager.commit(cc.get(i));
        }
        conceptoesDl.load();

    }
}