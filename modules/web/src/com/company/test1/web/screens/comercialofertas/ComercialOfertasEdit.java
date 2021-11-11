package com.company.test1.web.screens.comercialofertas;

import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.extroles.ComercialOfertas;

import javax.inject.Inject;

@UiController("test1_ComercialOfertas.edit")
@UiDescriptor("comercial-ofertas-edit.xml")
@EditedEntityContainer("comercialOfertasDc")
@LoadDataBeforeShow
public class ComercialOfertasEdit extends StandardEditor<ComercialOfertas> {

    @Inject
    private PickerField<Proveedor> proveedorField;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<ComercialOfertas> comercialOfertasDc;
    @Inject
    private DataContext dataContext;


    




}