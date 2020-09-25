package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.contratosinquilinos.Subrogador;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("test1_SubrogadoresFragment")
@UiDescriptor("subrogadores-fragment.xml")
public class SubrogadoresFragment extends ScreenFragment {
    @Inject
    private Table<Subrogador> tableSubrogadores;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("tableSubrogadores.create")
    private void onTableSubrogadoresCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(Subrogador.class, null, tableSubrogadores, screenBuilders, this, OpenMode.DIALOG,
                dataContext, null);
    }

    @Subscribe("tableSubrogadores.edit")
    private void onTableSubrogadoresEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableSubrogadores.getSingleSelected(), null, tableSubrogadores, screenBuilders, this, OpenMode.DIALOG
        ,dataContext, null);
    }

    
    
}