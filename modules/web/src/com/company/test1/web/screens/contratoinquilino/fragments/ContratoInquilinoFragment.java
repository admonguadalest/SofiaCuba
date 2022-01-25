package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.web.screens.contratoinquilino.ContratoInquilinoEdit;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

@UiController("test1_ContratoInquilinoFragment")
@UiDescriptor("contrato-inquilino-fragment.xml")
public class ContratoInquilinoFragment extends ScreenFragment {

    @Inject
    private TextField<String> mesAnyoAplicacionIPCField;
    @Inject
    private TextField<Integer> periodoActualizacionIPCField;
    @Inject
    private DateField<Date> fechaOcupacionField;


    public TextField<Integer> getPeriodoActualizacionIPCField() {
        return periodoActualizacionIPCField;
    }

    public TextField<String> getMesAnyoAplicacionIPCField() {
        return mesAnyoAplicacionIPCField;
    }

    public DateField<Date> getFechaOcupacionField() {
        return fechaOcupacionField;
    }




}