package com.company.test1.web.screens.propietario;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.extroles.Propietario;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_PropietarioFragment")
@UiDescriptor("propietario-fragment.xml")
public class PropietarioFragment extends ScreenFragment {
    @Inject
    private InstanceContainer<Propietario> propietarioDc;









}