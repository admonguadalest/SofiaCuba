package com.company.test1.web.screens.modelocontrato;

import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.haulmont.cuba.gui.screen.LookupComponent;

import javax.inject.Inject;
import java.util.Collection;
import java.util.function.Consumer;

@UiController("test1_ModeloContrato.browse")
@UiDescriptor("modelo-contrato-browse.xml")
@LookupComponent("modeloContratoesTable")
@LoadDataBeforeShow
public class ModeloContratoBrowse extends StandardLookup<ModeloContrato> {
    @Inject
    private GroupTable<ModeloContrato> modeloContratoesTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionLoader<ModeloContrato> modeloContratoesDl;

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Modelos de Contrato", ModeloContrato.class, modeloContratoesTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Modelos de Contrato.pdf", ExportFormat.getByExtension("pdf"));
    }

    public RichTextArea getDescripcionColumn(ModeloContrato mc){
        RichTextArea rta = uiComponents.create(RichTextArea.NAME);
        rta.setStyleName("RichtTextAreaAsTableCell");
        rta.setValue(mc.getDescripcion());
        rta.setEditable(false);
        rta.setWidth("100%");

        return rta;
    }

    public void onBtnDuplicarClick() {
        ModeloContrato mc = modeloContratoesTable.getSingleSelected();
        if (mc==null){
            notifications.create().withCaption("Seleccionar un modelo de contrato a duplicar");
        }
        mc = dataManager.reload(mc, "modeloContrato-CompleteForClonation");
        ModeloContrato mc2 = (ModeloContrato) mc.clone();
        dataManager.commit(mc2);
        modeloContratoesDl.load();


    }
}