package com.company.test1.web.screens.ordencobro;

import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenescobro.OrdenCobro;

import javax.inject.Inject;

@UiController("test1_OrdenCobro.edit")
@UiDescriptor("orden-cobro-edit.xml")
@EditedEntityContainer("ordenCobroDc")
@LoadDataBeforeShow
public class OrdenCobroEdit extends StandardEditor<OrdenCobro> {

    @Inject
    private InstanceContainer<OrdenCobro> ordenCobroDc;
    @Inject
    private ScreenBuilders screenBuilders;

    public void VerRecibo(){
        Recibo r = ordenCobroDc.getItem().getRecibo();
        Screen s = screenBuilders.editor(Recibo.class, this).editEntity(r).withOpenMode(OpenMode.NEW_TAB).build();
        s.show();
    }
}