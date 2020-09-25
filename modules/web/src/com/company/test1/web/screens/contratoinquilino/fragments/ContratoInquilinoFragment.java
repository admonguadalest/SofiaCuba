package com.company.test1.web.screens.contratoinquilino.fragments;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import java.util.Date;

@UiController("test1_ContratoInquilinoFragment")
@UiDescriptor("contrato-inquilino-fragment.xml")
public class ContratoInquilinoFragment extends ScreenFragment {
    @Subscribe("fechaOcupacionField")
    public void onFechaOcupacionFieldValueChange(HasValue.ValueChangeEvent<Date> event) {



        
    }
}