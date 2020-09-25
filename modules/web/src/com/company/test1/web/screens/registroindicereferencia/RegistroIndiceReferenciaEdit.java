package com.company.test1.web.screens.registroindicereferencia;

import com.company.test1.service.RegistroIndiceReferenciaService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.RegistroIndiceReferencia;

import javax.inject.Inject;

@UiController("test1_RegistroIndiceReferencia.edit")
@UiDescriptor("registro-indice-referencia-edit.xml")
@EditedEntityContainer("registroIndiceReferenciaDc")
@LoadDataBeforeShow
public class RegistroIndiceReferenciaEdit extends StandardEditor<RegistroIndiceReferencia> {
    @Inject
    private DataManager dataManager;
    @Inject
    private RegistroIndiceReferenciaService registroIndiceReferenciaService;
    @Inject
    private TextField<Integer> annoField;
    @Inject
    private TextField<String> nombreTipoField;
    @Inject
    private InstanceContainer<RegistroIndiceReferencia> registroIndiceReferenciaDc;
    @Inject
    private TextField<Integer> mesField;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        if (PersistenceHelper.isNew(this.getEditedEntity())){
            //calculo del anno y mes que toca
            Integer[] ii = registroIndiceReferenciaService.devuelveSiguienteParAnnoMesParaRegistro();
            registroIndiceReferenciaDc.getItem().setAnno(ii[0]);
            registroIndiceReferenciaDc.getItem().setMes(ii[1]);
            registroIndiceReferenciaDc.getItem().setNombreTipo("IPC");

        }
        annoField.setEditable(false);
        mesField.setEditable(false);
        nombreTipoField.setEditable(false);

    }
    
    
    
}