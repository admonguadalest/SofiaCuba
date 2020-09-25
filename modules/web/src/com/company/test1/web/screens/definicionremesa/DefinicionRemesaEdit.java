package com.company.test1.web.screens.definicionremesa;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.extroles.Propietario;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.DefinicionRemesa;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_DefinicionRemesa.edit")
@UiDescriptor("definicion-remesa-edit.xml")
@EditedEntityContainer("definicionRemesaDc")
@LoadDataBeforeShow
public class DefinicionRemesaEdit extends StandardEditor<DefinicionRemesa> {
    @Inject
    private CollectionLoader<CuentaBancaria> cuentasBancariasDl;
    @Inject
    private PickerField<Propietario> propietarioField;

    @Subscribe("propietarioField")
    private void onPropietarioFieldValueChange(HasValue.ValueChangeEvent<Propietario> event) {
        cuentasBancariasDl.load();        
    }

    @Install(to = "cuentasBancariasDl", target = Target.DATA_LOADER)
    private List<CuentaBancaria> cuentasBancariasDlLoadDelegate(LoadContext<CuentaBancaria> loadContext) {
        if (propietarioField.getValue()!=null) {
            return propietarioField.getValue().getPersona().getCuentasBancarias();
        }
        return new ArrayList<CuentaBancaria>();
    }
    
    

    
}