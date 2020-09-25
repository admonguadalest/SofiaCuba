package com.company.test1.web.screens.operarios;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_EdicionOperario")
@UiDescriptor("edicion-operario.xml")
public class EdicionOperario extends Screen {

    @Inject
    private CollectionLoader<User> usersDl;
    @Inject
    private CollectionContainer<User> usersDc;
    @Inject
    private DataManager dataManager;


    @Install(to = "usersDl", target = Target.DATA_LOADER)
    private List<User> usersDlLoadDelegate(LoadContext<User> loadContext) {
        //load only users with profile oeprario associated
        return null;
    }

    @Install(to = "ubicacionsDl", target = Target.DATA_LOADER)
    private List<Ubicacion> ubicacionsDlLoadDelegate(LoadContext<Ubicacion> loadContext) {
        List<Ubicacion>  u = dataManager.loadValue("select u from test1_Ubicacion u where u.esPropiedadVertical is true order by u.nombre", Ubicacion.class).list();
        return u;
    }

    @Install(to = "departamentoesDl", target = Target.DATA_LOADER)
    private List<Departamento> departamentoesDlLoadDelegate(LoadContext<Departamento> loadContext) {
        List<Departamento>  d = dataManager.loadValue("select d from test1_Departamento d JOIN d.ubicacion u where u.esPropiedadVertical is false order by u.nombre, d.piso, d.puerta", Departamento.class).list();
        return d;
    }
    
    
    
    
}