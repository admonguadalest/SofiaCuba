package com.company.test1.web.screens.estudioinversion;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.EstudioInversion;

import javax.inject.Inject;

@UiController("test1_Inversion.edit")
@UiDescriptor("estudio-inversion-edit.xml")
@EditedEntityContainer("estudioInversionDc")
@LoadDataBeforeShow
public class EstudioInversionEdit extends StandardEditor<EstudioInversion> {
    @Inject
    private DataContext dataContext;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {

    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        EstudioInversion ei = this.getEditedEntity();
        if (ei.getColeccionArchivosAdjuntos()==null){
            ColeccionArchivosAdjuntos caa = dataContext.create(ColeccionArchivosAdjuntos.class);
            caa.setNombre("Adjuntos");
            ei.setColeccionArchivosAdjuntos(caa);
        }
    }

    public void verEnlace(){
        this.getWindow().showWebPage(this.getEditedEntity().getLinkMaps(), ParamsMap.of("target", "_blank") );
    }



}