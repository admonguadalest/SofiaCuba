package com.company.test1.web.screens.ubicacion;

import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.Ubicacion;

import javax.inject.Inject;

@UiController("test1_Ubicacion.browse")
@UiDescriptor("ubicacion-browse.xml")
@LookupComponent("ubicacionsTable")
@LoadDataBeforeShow
public class UbicacionBrowse extends StandardLookup<Ubicacion> {

    @Inject
    private Filter filter;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {

        int y = 2;
    }
    
    
    
}