package com.company.test1.web.screens.ciclo;

import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.Ciclo;

import javax.inject.Inject;

@UiController("test1_Ciclo.browse")
@UiDescriptor("ciclo-browse.xml")
@LookupComponent("cicloesTable")
@LoadDataBeforeShow
public class CicloBrowse extends StandardLookup<Ciclo> {

    @Inject
    private GroupTable<Ciclo> cicloesTable;
    @Inject
    private ScreenBuilders screenBuilders;

//    @Subscribe("cicloesTable.edit")
//    private void onCicloesTableEdit(Action.ActionPerformedEvent event) {
//
//        ScreenLaunchUtil.launchEditEntityScreen(cicloesTable.getSingleSelected(), null, null,
//                screenBuilders, this, OpenMode.NEW_TAB, null, null);
//
//    }
    
    
}