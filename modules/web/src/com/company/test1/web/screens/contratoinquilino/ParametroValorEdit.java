package com.company.test1.web.screens.contratoinquilino;

import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.ParametroValor;

import javax.inject.Inject;

@UiController("test1_ParametroValor.edit")
@UiDescriptor("parametro-valor-edit.xml")
@EditedEntityContainer("parametroValorDc")
@LoadDataBeforeShow
public class ParametroValorEdit extends StandardEditor<ParametroValor> {
    @Inject
    private TextArea<String> valorField;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

        valorField.focus();
    }



}