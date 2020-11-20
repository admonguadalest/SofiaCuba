package com.company.test1.web.screens.contratoinquilino.conceptorecibo;

import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.ConceptoAdicionalConceptoRecibo;

import javax.inject.Inject;
import java.util.Arrays;

@UiController("test1_ConceptoAdicionalConceptoRecibo.edit")
@UiDescriptor("concepto-adicional-concepto-recibo-edit.xml")
@EditedEntityContainer("conceptoAdicionalConceptoReciboDc")
@LoadDataBeforeShow
public class ConceptoAdicionalConceptoReciboEdit extends StandardEditor<ConceptoAdicionalConceptoRecibo> {
    @Inject
    private LookupField<Double> listaPorcentajes;
    @Inject
    private PickerField<ConceptoAdicional> conceptoAdicionalField;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        listaPorcentajes.setOptionsList(Arrays.asList());
    }

    @Subscribe("conceptoAdicionalField")
    public void onConceptoAdicionalFieldValueChange(HasValue.ValueChangeEvent<ConceptoAdicional> event) {
        ConceptoAdicional ca = event.getValue();
        if (ca!=null){
            if (ca.getAbreviacion().compareTo("IVA")==0){
                listaPorcentajes.setOptionsList(Arrays.asList(0.0, 0.04, 0.10, 0.21));
            }
            if (ca.getAbreviacion().compareTo("IRPF")==0){
                listaPorcentajes.setOptionsList(Arrays.asList(0.0, 0.01, 0.02, 0.15, 0.19));
            }
        }else{
            listaPorcentajes.setOptionsList(Arrays.asList());
        }
    }



}