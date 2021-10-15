package com.company.test1.web.screens.asignaciontarea;

import com.company.test1.entity.ciclos.AsignacionTarea;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_CoordinacionAsignacionesTarea")
@UiDescriptor("coordinacion-asignaciones-tarea.xml")
public class CoordinacionAsignacionesTarea extends Screen {

    @Inject
    private CollectionLoader<AsignacionTarea> asignacionTareasProgramadasDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private DataGrid<AsignacionTarea> asignacionTareasProgramadasTable;
    @Inject
    private CollectionLoader<AsignacionTarea> asignacionTareasPendientesProgramarDl;

    @Inject
    private DataGrid<AsignacionTarea> asignacionTareasPendientesProgramacionTable;
    @Inject
    private Notifications notifications;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        asignacionTareasProgramadasDl.load();
        asignacionTareasPendientesProgramarDl.load();
    }



    @Install(to = "asignacionTareasProgramadasDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasProgramadasDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is not null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        return l;
    }

    @Install(to = "asignacionTareasPendientesProgramarDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasPendientesProgramarDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        return l;
    }

    public void verTareaProgramada(){
        AsignacionTarea at = asignacionTareasProgramadasTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro a ver").show();
        }

        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            asignacionTareasPendientesProgramarDl.load();
            asignacionTareasProgramadasDl.load();
            asignacionTareasProgramadasTable.sort("fechaPrevistoInicio", DataGrid.SortDirection.DESCENDING);
        });
    }

    public void verTareaPendiente(){
        AsignacionTarea at = asignacionTareasPendientesProgramacionTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro a ver").show();
        }
        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            asignacionTareasPendientesProgramarDl.load();
            asignacionTareasProgramadasDl.load();
            asignacionTareasProgramadasTable.sort("fechaPrevistoInicio", DataGrid.SortDirection.DESCENDING);
        });
    }



}