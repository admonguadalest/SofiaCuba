package com.company.test1.web.screens.ordentrabajo;

import com.company.test1.entity.ciclos.AsignacionTarea;
import com.company.test1.entity.ciclos.NotaIntervencion;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.asignaciontarea.AsignacionTareaEdit;
import com.company.test1.web.screens.notaintervencion.NotaIntervencionEdit;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.OrdenTrabajo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiController("test1_OrdenTrabajo.edit")
@UiDescriptor("orden-trabajo-edit.xml")
@EditedEntityContainer("ordenTrabajoDc")
@LoadDataBeforeShow
public class OrdenTrabajoEdit extends StandardEditor<OrdenTrabajo> {

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {
        ordenTrabajoDl.load();
    }
    
    
    
    @Install(to = "carpetaDocumentosFotograficosesLc", target = Target.DATA_LOADER)
    private List<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosesLcLoadDelegate(LoadContext<CarpetaDocumentosFotograficos> loadContext) {
//        return Arrays.asList(new CarpetaDocumentosFotograficos[]{ordenTrabajoDc.getItem().getEntrada().getCiclo().getca});
        return new ArrayList();//pendinte
    }



    @Inject
    private InstanceLoader<OrdenTrabajo> ordenTrabajoDl;
    @Inject
    private CollectionContainer<NotaIntervencion> notasIntervencionDc;
    @Inject
    private InstanceContainer<OrdenTrabajo> ordenTrabajoDc;
    @Inject
    private CollectionContainer<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosesDc;
    @Inject
    private CollectionLoader<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosesLc;
    @Inject
    private Table<NotaIntervencion> tableNotasIntervencion;
    @Inject
    private CollectionLoader<NotaIntervencion> notasIntervencionDl;
    @Inject
    private CollectionLoader<AsignacionTarea> asignacionesTareasDl;
    @Inject
    private DataContext dataContext;
    @Inject
    private Table<AsignacionTarea> tableAsignacionesTareas;
    @Inject
    private ScreenBuilders screenBuilders;






    @Subscribe("tableAsignacionesTareas.create")
    private void onTableAsignacionesTareasCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(AsignacionTarea.class, null, tableNotasIntervencion, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    @Subscribe("tableAsignacionesTareas.edit")
    private void onTableAsignacionesTareasEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableAsignacionesTareas.getSingleSelected(), null, tableAsignacionesTareas, screenBuilders, this, OpenMode.DIALOG, dataContext, null);

    }

    @Subscribe("tableNotasIntervencion.create")
    private void onTableNotasIntervencionCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(NotaIntervencion.class, null, tableNotasIntervencion, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    @Subscribe("tableNotasIntervencion.edit")
    private void onTableNotasIntervencionEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableNotasIntervencion.getSingleSelected(), null, tableNotasIntervencion, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

}
    
    

    