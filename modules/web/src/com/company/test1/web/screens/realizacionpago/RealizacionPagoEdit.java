package com.company.test1.web.screens.realizacionpago;

import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.RealizacionPago;

import javax.inject.Inject;

@UiController("test1_RealizacionPago.edit")
@UiDescriptor("realizacion-pago-edit.xml")
@EditedEntityContainer("realizacionPagoDc")
@LoadDataBeforeShow
public class RealizacionPagoEdit extends StandardEditor<RealizacionPago> {
    @Inject
    private InstanceContainer<RealizacionPago> realizacionPagoDc;
    @Inject
    private ExportDisplay exportDisplay;

    public void onBtnDescargarClick() {
        byte[] bb = realizacionPagoDc.getItem().getSepa().getBytes();
        exportDisplay.show(new ByteArrayDataProvider(bb), realizacionPagoDc.getItem().getIdentificador() + ".xml");
    }
}