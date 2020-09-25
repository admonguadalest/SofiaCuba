package com.company.test1.web.screens.direccion;

import com.company.test1.entity.enums.NombreTipoDireccion;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.Direccion;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("test1_Direccion.edit")
@UiDescriptor("direccion-edit.xml")
@EditedEntityContainer("direccionDc")
@LoadDataBeforeShow
public class DireccionEdit extends StandardEditor<Direccion> {
    @Inject
    private LookupField<String> paisField;
    @Inject
    private LookupField<String> nombreField;
    @Inject
    private TextField<String> nombrePersonalizadoField;
    @Inject
    private InstanceContainer<Direccion> direccionDc;

    List<String> nombres = Arrays.asList(new String[]{NombreTipoDireccion.DOMICILIO_CONTRACTUAL.getId(), NombreTipoDireccion.DOMICILIO_INQUILINO.getId(),
            NombreTipoDireccion.DOMICILIO_SOCIAL.getId(), NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId(), "Personalizada"});
    @Inject
    private DataContext dataContext;

    @Subscribe
    private void onInit(InitEvent event) {
        List<String> l = Arrays.asList("ESPAÑA");
        paisField.setOptionsList(l);

        
        nombreField.setOptionsList(nombres);





        
        
    }

    @Subscribe("nombreField")
    public void onNombreFieldValueChange1(HasValue.ValueChangeEvent event) {
        if (nombreField.getValue().compareTo("Personalizada")==0){
            if (nombres.indexOf(direccionDc.getItem().getNombre())!=-1) {
                nombrePersonalizadoField.setValue("");
            }else{
                nombrePersonalizadoField.setValue(direccionDc.getItem().getNombre());
            }
        }else{
            direccionDc.getItem().setNombre(nombreField.getValue());
            nombrePersonalizadoField.setVisible(false);
        }
    }

    
    
    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (nombres.indexOf(direccionDc.getItem().getNombre())!=-1){
            nombreField.setValue(direccionDc.getItem().getNombre());
        }else{
            nombreField.setValue("Personalizada");
            nombrePersonalizadoField.setVisible(true);
            nombrePersonalizadoField.setValue(direccionDc.getItem().getNombre());
        }
    }
    
    

    @Subscribe("nombrePersonalizadoField")
    public void onNombrePersonalizadoFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        
        direccionDc.getItem().setNombre(event.getValue());
    }
    

    @Subscribe("nombreField")
    public void onNombreFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (event.getValue().compareTo("Personalizada")==0){
            nombrePersonalizadoField.setValue(direccionDc.getItem().getNombre());
            nombrePersonalizadoField.setVisible(true);
        }
    }
    
    
    


}