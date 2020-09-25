package com.company.test1.web.screens.registroindicereferencia;

import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.RegistroIndiceReferencia;

import javax.inject.Inject;

@UiController("test1_RegistroIndiceReferencia.browse")
@UiDescriptor("registro-indice-referencia-browse.xml")
@LookupComponent("registroIndiceReferenciasTable")
@LoadDataBeforeShow
public class RegistroIndiceReferenciaBrowse extends StandardLookup<RegistroIndiceReferencia> {
    @Inject
    private GroupTable<RegistroIndiceReferencia> registroIndiceReferenciasTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private CollectionLoader<RegistroIndiceReferencia> registroIndiceReferenciasDl;
    @Inject
    private CollectionContainer<RegistroIndiceReferencia> registroIndiceReferenciasDc;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {

    }
    
    

    public void onBtnPdfClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Evolucion IPC's", RegistroIndiceReferencia.class, registroIndiceReferenciasTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Registros Indicesz.pdf", ExportFormat.getByExtension("pdf"));
    }
}