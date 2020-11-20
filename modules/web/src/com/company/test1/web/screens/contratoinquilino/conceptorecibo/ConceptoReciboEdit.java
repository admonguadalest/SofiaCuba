package com.company.test1.web.screens.contratoinquilino.conceptorecibo;

import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.recibos.ConceptoAdicionalConceptoRecibo;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.fragments.ConceptoReciboActivacionFragment;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.fragments.ConceptoReciboEntreFechasFragment;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.fragments.ConceptoReciboNoEmisionesfragment;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.sun.xml.bind.api.impl.NameConverter;

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
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private Table<ConceptoAdicionalConceptoRecibo> tableConceptosAdicionales;

    @Inject
    private InstanceContainer<ConceptoRecibo> conceptoReciboDc;
    @Inject
    private Notifications notifications;

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
        StandardEditor se = (StandardEditor) screenBuilders.editor(ConceptoAdicionalConceptoRecibo.class, this).newEntity().withOpenMode(OpenMode.DIALOG).withParentDataContext(dataContext)
                .withListComponent(tableConceptosAdicionales).build();
        se.addAfterCloseListener(e->{
            StandardCloseAction sca = (StandardCloseAction) e.getCloseAction();
            if (sca.getActionId().compareTo("commit")==0){

                ConceptoAdicionalConceptoRecibo cacr = (ConceptoAdicionalConceptoRecibo) se.getEditedEntity();
                cacr.setConceptoRecibo(conceptoReciboDc.getItem());
            }
        });
        se.show();
    }

    @Subscribe("tableConceptosAdicionales.edit")
    private void onTableConceptosAdicionalesEdit(Action.ActionPerformedEvent event) {
        ConceptoAdicionalConceptoRecibo cacr = tableConceptosAdicionales.getSingleSelected();
        if (cacr == null){
            notifications.create().withDescription("Seleccionar un Concepto Adicional").show();
            return;
        }

        StandardEditor se = (StandardEditor) screenBuilders.editor(ConceptoAdicionalConceptoRecibo.class, this).editEntity(cacr).withOpenMode(OpenMode.DIALOG).withParentDataContext(dataContext)
                .withListComponent(tableConceptosAdicionales).build();

        se.show();
    }
    
    
    
    
    
}