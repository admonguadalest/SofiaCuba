package com.company.test1.web.screens.plantilla;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.reportsyplantillas.Plantilla;

@UiController("test1_Plantilla.edit")
@UiDescriptor("plantilla-edit.xml")
@EditedEntityContainer("plantillaDc")
@LoadDataBeforeShow
public class PlantillaEdit extends StandardEditor<Plantilla> {
    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("newEntity")){
                this.setEntityToEdit(new Plantilla());
            }
        }

    }
    
    
    
}