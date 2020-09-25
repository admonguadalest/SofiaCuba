package com.company.test1.web.screens.concepto;

import com.company.test1.service.RecibosService;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Concepto;

import javax.inject.Inject;

@UiController("test1_Concepto.edit")
@UiDescriptor("concepto-edit.xml")
@EditedEntityContainer("conceptoDc")
@LoadDataBeforeShow
public class ConceptoEdit extends StandardEditor<Concepto> {
    @Inject
    private RecibosService recibosService;
    @Inject
    private InstanceContainer<Concepto> conceptoDc;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        Concepto c = conceptoDc.getItem();
        if (c.getOrdenacion()==null){
            Integer i = recibosService.getValorOrdenacionParaNuevoConcepto();
            c.setOrdenacion(i);
        }
        
    }
    
    
    
}