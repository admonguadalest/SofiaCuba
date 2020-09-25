package com.company.test1.web.screens.direccion;

import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.Arrays;

@UiController("test1_DireccionFragment")
@UiDescriptor("direccion-fragment.xml")
public class DireccionFragment extends ScreenFragment {

    @Inject
    private LookupField viaField;

    private void pueblaValoresPickers(){
        String[] vias = new String[]{"Calle","Pza.","Pseo.", "Camino"};
        viaField.setOptionsList(Arrays.asList(vias));
    }

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        pueblaValoresPickers();
    }
    
    

}