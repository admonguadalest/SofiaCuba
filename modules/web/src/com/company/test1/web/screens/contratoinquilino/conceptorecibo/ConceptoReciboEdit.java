package com.company.test1.web.screens.contratoinquilino.conceptorecibo;

import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.fragments.ConceptoReciboActivacionFragment;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.fragments.ConceptoReciboEntreFechasFragment;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.fragments.ConceptoReciboNoEmisionesfragment;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.ConceptoRecibo;

import javax.inject.Inject;

@UiController("test1_ConceptoRecibo.edit")
@UiDescriptor("concepto-recibo-edit.xml")
@EditedEntityContainer("conceptoReciboDc")
@LoadDataBeforeShow
public class ConceptoReciboEdit extends StandardEditor<ConceptoRecibo> {


    @Inject
    private LookupField<ConceptoReciboVigenciaEnum> vigenciaField;
    @Inject
    private FlowBoxLayout flwVigencia;
    @Inject
    private Fragments fragments;

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {

        
    }

    @Subscribe("vigenciaField")
    private void onVigenciaFieldValueChange(HasValue.ValueChangeEvent<ConceptoReciboVigenciaEnum> event) {
        flwVigencia.removeAll();
        ConceptoReciboVigenciaEnum crve = event.getValue();
        if (crve == ConceptoReciboVigenciaEnum.PERMANENTE){
            //nada
        }else if(crve == ConceptoReciboVigenciaEnum.ACTIVACION){
            ScreenFragment f = fragments.create(this, ConceptoReciboActivacionFragment.class);
            flwVigencia.add(f.getFragment());
        }else if(crve == ConceptoReciboVigenciaEnum.ENTRE_FECHAS){
            ScreenFragment f = fragments.create(this, ConceptoReciboEntreFechasFragment.class);
            flwVigencia.add(f.getFragment());
        }else if(crve == ConceptoReciboVigenciaEnum.NUMERO_EMISIONES){
            ScreenFragment f = fragments.create(this, ConceptoReciboNoEmisionesfragment.class);
            flwVigencia.add(f.getFragment());
        }


    }
    
    

    @Subscribe("tableConceptosAdicionales.create")
    private void onTableConceptosAdicionalesCreate(Action.ActionPerformedEvent event) {
        
    }

    @Subscribe("tableConceptosAdicionales.edit")
    private void onTableConceptosAdicionalesEdit(Action.ActionPerformedEvent event) {

    }
    
    
    
    
    
}