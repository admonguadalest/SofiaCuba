package com.company.test1.web.screens.ordentrabajo;

import com.company.test1.entity.ciclos.AsignacionTarea;
import com.company.test1.entity.ciclos.NotaIntervencion;
import com.company.test1.entity.ciclos.OrdenTrabajo;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_OrdenTrabajoFragment")
@UiDescriptor("orden-trabajo-fragment.xml")
public class OrdenTrabajoFragment extends ScreenFragment {

    @Inject
    private DataManager dataManager;
    @Inject
    private Table<AsignacionTarea> tableAsignacionesTareas;
    @Inject
    private CollectionPropertyContainer<AsignacionTarea> asignacionesTareasDc;
    @Inject
    private Table<NotaIntervencion> tableNotasIntervencion;
    @Inject
    private CollectionPropertyContainer<NotaIntervencion> notasIntervencionDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private InstanceContainer<OrdenTrabajo> ordenTrabajoDc;



    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        OrdenTrabajo ot = ordenTrabajoDc.getItem();
        int y = 2;
    }

    

    @Install(to = "carpetaDocumentosFotograficosesLc", target = Target.DATA_LOADER)
    private List<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosesLcLoadDelegate(LoadContext<CarpetaDocumentosFotograficos> loadContext) {
//        return Arrays.asList(new CarpetaDocumentosFotograficos[]{ordenTrabajoDc.getItem().getEntrada().getCiclo().getca});
        return new ArrayList();//pendinte
    }

    @Install(to = "administradorsLc", target = Target.DATA_LOADER)
    private List<User> administradorsLcLoadDelegate(LoadContext<User> loadContext) {

        Object o = dataManager.loadValues("SELECT u FROM User u");
        return new ArrayList<User>();
    }

    @Subscribe("tableAsignacionesTareas.create")
    private void onTableAsignacionesTareasCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(AsignacionTarea.class, null, tableAsignacionesTareas, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(AsignacionTarea.class, this).newEntity().
                withLaunchMode(OpenMode.DIALOG)
                .withListComponent(tableAsignacionesTareas)
                .withParentDataContext(dataContext)
                .build();
        s.show();
    }

    @Subscribe("tableNotasIntervencion.create")
    private void onTableNotasIntervencionCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(NotaIntervencion.class, null, tableNotasIntervencion, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
//        ScreenLaunchUtil.launchNewEntityStreen(NotaIntervencion.class, null, tableNotasIntervencion, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(NotaIntervencion.class, this).newEntity().
                withLaunchMode(OpenMode.DIALOG)
                .withListComponent(tableNotasIntervencion)
                .withParentDataContext(dataContext)
                .build();
        s.show();
    }

    @Subscribe("tableAsignacionesTareas.edit")
    private void onTableAsignacionesTareasEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableAsignacionesTareas.getSingleSelected(), null, tableAsignacionesTareas, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    @Subscribe("tableNotasIntervencion.edit")
    private void onTableNotasIntervencionEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableNotasIntervencion.getSingleSelected(), null, tableNotasIntervencion, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }
    
    
    
    
}