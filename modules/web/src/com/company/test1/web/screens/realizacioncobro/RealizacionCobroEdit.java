package com.company.test1.web.screens.realizacioncobro;

import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenescobro.RealizacionCobro;

import javax.inject.Inject;

@UiController("test1_RealizacionCobro.edit")
@UiDescriptor("realizacion-cobro-edit.xml")
@EditedEntityContainer("realizacionCobroDc")
@LoadDataBeforeShow
public class RealizacionCobroEdit extends StandardEditor<RealizacionCobro> {

    @Inject
    private CollectionPropertyContainer<OrdenCobro> ordenesCobroDc;
    @Inject
    private InstanceContainer<RealizacionCobro> realizacionCobroDc;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

        int y = 2;
    }
    
    
    
    public void onBtnDescargarClick() {
        String txt = realizacionCobroDc.getItem().getSepa();
        exportDisplay.show(new ByteArrayDataProvider(txt.getBytes()), realizacionCobroDc.getItem().getIdentificador()+".xml");
    }


}