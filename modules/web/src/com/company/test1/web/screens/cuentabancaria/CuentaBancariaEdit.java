package com.company.test1.web.screens.cuentabancaria;

import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.UiComponentsGenerator;
import com.haulmont.cuba.gui.model.impl.DataContextImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.CuentaBancaria;
import com.haulmont.cuba.web.gui.components.WebTextField;
import com.haulmont.cuba.web.widgets.CubaTextField;

import javax.inject.Inject;

@UiController("test1_CuentaBancaria.edit")
@UiDescriptor("cuenta-bancaria-edit.xml")
@EditedEntityContainer("cuentaBancariaDc")
@LoadDataBeforeShow
public class CuentaBancariaEdit extends StandardEditor<CuentaBancaria> {

    @Inject
    private TextField<String> codigoBICField;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private UiComponentsGenerator uiComponentsGenerator;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        WebTextField<String> wtf = (WebTextField<String>)codigoBICField;
        CubaTextField ctf = (CubaTextField) wtf.getComposition();

        int y = 2;
    }
    
    
}