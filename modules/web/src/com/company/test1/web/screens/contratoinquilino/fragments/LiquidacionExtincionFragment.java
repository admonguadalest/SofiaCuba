package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.contratosinquilinos.LiquidacionExtincion;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.Date;

@UiController("test1_LiquidacionExtincionFragment")
@UiDescriptor("liquidacion-extincion-fragment.xml")
public class LiquidacionExtincionFragment extends ScreenFragment {

    @Inject
    private CheckBox conformidadAdministrador;
    @Inject
    private InstanceContainer<LiquidacionExtincion> liquidacionExtincionDc;

    @Inject
    private TextField<Double> cantidadesActaLiquidacion;
    @Inject
    private RichTextArea detalle;
    @Inject
    private DateField<Date> fechaLiquidacion;
    @Inject
    private DateField<Date> fechaPagoCantidadesActa;
    @Inject
    private TextField<Double> importeAvalEjecutado;
    @Inject
    private TextField<Double> porLiquidar;
    @Inject
    private TextField<Double> totalFianzaYGarantias;
    @Inject
    private TextField<Double> totalIndemnizaciones;
    @Inject
    private TextField<Double> totalRecibosPendientes;
    @Inject
    private Notifications notifications;

    @Subscribe("totalFianzaYGarantias")
    public void onTotalFianzaYGarantiasValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("totalIndemnizaciones")
    public void onTotalIndemnizacionesValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }

    @Subscribe("totalRecibosPendientes")
    public void onTotalRecibosPendientesValueChange(HasValue.ValueChangeEvent<Double> event) {
        calculaTotalLiquidacion();
    }
    
    
    

    private void calculaTotalLiquidacion(){
        double total = 0.0;
        try {
            total += totalFianzaYGarantias.getValue() - totalRecibosPendientes.getValue() - totalIndemnizaciones.getValue();
            liquidacionExtincionDc.getItem().setPorLiquidar(total);
        }catch(Exception e){

            liquidacionExtincionDc.getItem().setPorLiquidar(0.0);
        }


    }

}