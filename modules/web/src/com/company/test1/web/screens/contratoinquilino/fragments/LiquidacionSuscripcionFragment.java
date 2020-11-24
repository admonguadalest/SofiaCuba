package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.contratosinquilinos.LiquidacionSuscripcion;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController("test1_LiquidacionSuscripcionFragment")
@UiDescriptor("liquidacion-suscripcion-fragment.xml")
public class LiquidacionSuscripcionFragment extends ScreenFragment {

    @Inject
    private TextField<Double> totalLiquidacionField;
    @Inject
    private TextField<Double> recibosACuentaField;
    @Inject
    private TextField<Double> otrosConceptosField;
    @Inject
    private TextField<Double> gastosContratoField;
    @Inject
    private TextField<Double> garantiasEnEfectivoField;
    @Inject
    private TextField<Double> fianzaContratoField;
    @Inject
    private InstanceContainer<LiquidacionSuscripcion> liquidacionSuscripcionDc;
    @Inject
    private TextField<Double> cantidadesEntregadasEnReservaField;
    @Inject
    private TextArea<String> detalleField;

    @Subscribe("cantidadesEntregadasEnReservaField")
    private void onCantidadesEntregadasEnReservaFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("fianzaContratoField")
    private void onFianzaContratoFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("garantiasEnEfectivoField")
    private void onGarantiasEnEfectivoFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("gastosContratoField")
    private void onGastosContratoFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("otrosConceptosField")
    private void onOtrosConceptosFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("recibosACuentaField")
    private void onRecibosACuentaFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    private void calculaTotalLiquidacion(){
        double total = 0.0;
        try {
            total += fianzaContratoField.getValue() + garantiasEnEfectivoField.getValue() + cantidadesEntregadasEnReservaField.getValue() +
                    gastosContratoField.getValue() + recibosACuentaField.getValue() + otrosConceptosField.getValue();
            totalLiquidacionField.setValue(total);
        }catch(Exception e){
            liquidacionSuscripcionDc.getItem().setTotalLiquidacion(0.0);
        }


    }
}