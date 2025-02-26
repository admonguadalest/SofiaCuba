package com.company.test1.web.screens.ordenpagocontratoinquilino;

import com.company.test1.entity.ordenespago.RealizacionPago;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.OrdenPagoContratoInquilino;

import javax.inject.Inject;
import java.util.Arrays;

@UiController("test1_OrdenPagoContratoInquilino.edit")
@UiDescriptor("orden-pago-contrato-inquilino-edit.xml")
@EditedEntityContainer("ordenPagoContratoInquilinoDc")
@LoadDataBeforeShow
public class OrdenPagoContratoInquilinoEdit extends StandardEditor<OrdenPagoContratoInquilino> {
    @Inject
    private InstanceContainer<OrdenPagoContratoInquilino> ordenPagoContratoInquilinoDc;

    @Subscribe("importeField")
    public void onImporteFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        ordenPagoContratoInquilinoDc.getItem().setImporteEfectivo(ordenPagoContratoInquilinoDc.getItem().getImporte());
    }
    @Inject
    private LookupField lkpTipo;
    @Inject
    private TextArea<String> descripcionField;
    @Inject
    private PickerField<RealizacionPago> realizacionPagoField;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        String[] ss = new String[]{"RETORNO FIANZA", "LIQ. SUMINISTROS", "OTROS"};
        lkpTipo.setOptionsList(Arrays.asList(ss));
        realizacionPagoField.setEditable(false);
        try{
            if (descripcionField.getValue()!=null) {
                lkpTipo.setValue(descripcionField.getValue());
            }else{
                lkpTipo.setValue("RETORNO FIANZA");
            }
        }catch(Exception exc){

        }
    }

    @Subscribe("lkpTipo")
    public void onLkpTipoValueChange(HasValue.ValueChangeEvent event) {
        if (event.getValue().toString().compareTo("OTROS")==0){
            descripcionField.setValue("");
            descripcionField.setEditable(true);
        }else{
            descripcionField.setValue(lkpTipo.getValue().toString());
            descripcionField.setEditable(false);
        }
    }
    
    
}