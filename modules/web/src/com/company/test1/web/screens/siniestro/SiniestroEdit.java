package com.company.test1.web.screens.siniestro;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.PolizaDeSeguros;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.InstancePropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.Siniestro;

import javax.inject.Inject;

@UiController("test1_Siniestro.edit")
@UiDescriptor("siniestro-edit.xml")
@EditedEntityContainer("siniestroDc")
@LoadDataBeforeShow
public class SiniestroEdit extends StandardEditor<Siniestro> {

    @Inject
    private InstancePropertyContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;
    @Inject
    private PickerField<PolizaDeSeguros> polizaDeSegurosField;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

        if (coleccionArchivosAdjuntosDc.getItem().getNombre().length()==0){
            coleccionArchivosAdjuntosDc.getItem().setNombre("Archivos Adjuntos Siniestro");
        }

    }

    public void verPoliza(){
        PolizaDeSeguros ps = polizaDeSegurosField.getValue();
        screenBuilders.editor(PolizaDeSeguros.class, this).withOpenMode(OpenMode.NEW_TAB).editEntity(ps).build().show();
    }






}