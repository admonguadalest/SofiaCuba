package com.company.test1.web.screens.proveedor;

import com.company.test1.entity.Persona;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.extroles.Proveedor;

import javax.inject.Inject;

@UiController("test1_Proveedor.browse")
@UiDescriptor("proveedor-browse.xml")
@LookupComponent("proveedorsTable")
@LoadDataBeforeShow
public class ProveedorBrowse extends StandardLookup<Proveedor> {
    @Inject
    private GroupTable<Proveedor> proveedorsTable;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("proveedorsTable.edit")
    private void onProveedorsTableEdit(Action.ActionPerformedEvent event) {
        Proveedor prov = proveedorsTable.getSingleSelected();
        Persona pers = prov.getPersona();
        ScreenLaunchUtil.launchEditEntityScreen(pers,null, null, screenBuilders, this, OpenMode.NEW_TAB,
                null, (s)->{proveedorsTable.getAction("refresh").actionPerform(null);});
        
    }
    
    
}