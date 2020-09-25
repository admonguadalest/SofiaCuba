package com.company.test1.web.screens.proveedor;

import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.conceptosadicionales.ProgramacionConceptoAdicionalEdit;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("test1_ProveedorFragment")
@UiDescriptor("proveedor-fragment.xml")
public class ProveedorFragment extends ScreenFragment {

    @Inject
    private Table<ProgramacionConceptoAdicional> tableProgramacionesCCAA;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private InstanceContainer<Proveedor> proveedorDc;

    @Subscribe("tableProgramacionesCCAA.create")
    private void onTableProgramacionesCCAACreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(ProgramacionConceptoAdicional.class, null, tableProgramacionesCCAA, screenBuilders, this, OpenMode.DIALOG, dataContext,
                null, null);
    }

    @Subscribe("tableProgramacionesCCAA.edit")
    private void onTableProgramacionesCCAAEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableProgramacionesCCAA.getSingleSelected(), null, tableProgramacionesCCAA, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }
}