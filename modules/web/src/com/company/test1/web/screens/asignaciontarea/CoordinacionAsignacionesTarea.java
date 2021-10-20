package com.company.test1.web.screens.asignaciontarea;

import com.company.test1.entity.ciclos.AsignacionTarea;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private Departamento deptoProgramadas = null;
    private Departamento deptoPendientes = null;
    private Proveedor proveedor = null;


    @Inject
    private CollectionLoader<Departamento> departamentosAfectadosDl;
    @Inject
    private LookupField<Departamento> lkpDepartamentosAfectados;
    @Inject
    private LookupField<Proveedor> lkpProveedoresAfectados;
    @Inject
    private CollectionLoader<Proveedor> proveedoresAfectadosDl;
    @Inject
    private CollectionLoader<AsignacionTarea> asignacionTareasFinalizadasDl;
    @Inject
    private DataGrid<AsignacionTarea> asignacionTareasFinalizadasTable;

    @Subscribe("lkpDepartamentosAfectados")
    public void onLkpDepartamentosAfectadosValueChange(HasValue.ValueChangeEvent<Departamento> event) {
        Departamento d = lkpDepartamentosAfectados.getValue();
        if (d != null){
            this.deptoProgramadas = d;
            this.deptoPendientes = d;


        }else{
            this.deptoProgramadas = null;
            this.deptoPendientes = null;

        }
        asignacionTareasPendientesProgramarDl.load();
        asignacionTareasProgramadasDl.load();
        asignacionTareasFinalizadasDl.load();
    }

    @Subscribe("lkpProveedoresAfectados")
    public void onLkpProveedoresAfectadosValueChange(HasValue.ValueChangeEvent<Proveedor> event) {
        this.proveedor = lkpProveedoresAfectados.getValue();

        asignacionTareasPendientesProgramarDl.load();
        asignacionTareasProgramadasDl.load();
        asignacionTareasFinalizadasDl.load();
    }





    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        asignacionTareasProgramadasDl.load();
        asignacionTareasPendientesProgramarDl.load();
        asignacionTareasFinalizadasDl.load();

        departamentosAfectadosDl.load();
        proveedoresAfectadosDl.load();
    }

    public void refrescar(){
        this.deptoPendientes = null;
        this.deptoProgramadas = null;
        this.proveedor = null;
        lkpProveedoresAfectados.setValue(null);
        asignacionTareasProgramadasTable.setSelected(new ArrayList());
        asignacionTareasPendientesProgramacionTable.setSelected(new ArrayList());
        asignacionTareasPendientesProgramarDl.load();
        asignacionTareasProgramadasDl.load();
        asignacionTareasFinalizadasDl.load();
        departamentosAfectadosDl.load();
        proveedoresAfectadosDl.load();
        lkpDepartamentosAfectados.setValue(null);
    }

//    public void filtrarTareasSeleccionProgramadas(){
//        AsignacionTarea at = asignacionTareasProgramadasTable.getSingleSelected();
//        asignacionTareasPendientesProgramacionTable.setSelected(new ArrayList());
//        if (at==null){
//            notifications.create().withCaption("Seleccionar un registro").show();
//            return;
//        }
//        this.deptoProgramadas = at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
//        this.deptoPendientes = this.deptoProgramadas;
//        asignacionTareasProgramadasDl.load();
//        asignacionTareasPendientesProgramarDl.load();
//    }

    public void verCicloSeleccionProgramadas(){
        AsignacionTarea at = asignacionTareasProgramadasTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        Ciclo c = at.getOrdenTrabajo().getEntrada().getCiclo();
        Screen s = screenBuilders.editor(Ciclo.class, this)
                .withLaunchMode(OpenMode.NEW_TAB)
                .editEntity(c).build();
        s.show();
    }

    public void verCicloSeleccionFinalizadas(){
        AsignacionTarea at = asignacionTareasFinalizadasTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        Ciclo c = at.getOrdenTrabajo().getEntrada().getCiclo();
        Screen s = screenBuilders.editor(Ciclo.class, this)
                .withLaunchMode(OpenMode.NEW_TAB)
                .editEntity(c).build();
        s.show();
    }

    public void verCicloSeleccionPendientes(){
        AsignacionTarea at = asignacionTareasPendientesProgramacionTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        Ciclo c = at.getOrdenTrabajo().getEntrada().getCiclo();
        Screen s = screenBuilders.editor(Ciclo.class, this)
                .withLaunchMode(OpenMode.NEW_TAB)
                .editEntity(c).build();
        s.show();
    }

//    public void filtrarTareasSeleccionPendientes(){
//        AsignacionTarea at = asignacionTareasPendientesProgramacionTable.getSingleSelected();
//        asignacionTareasProgramadasTable.setSelected(new ArrayList());
//        if (at==null){
//            notifications.create().withCaption("Seleccionar un registro").show();
//            return;
//        }
//        this.deptoProgramadas = at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
//        this.deptoPendientes = this.deptoProgramadas;
//        asignacionTareasProgramadasDl.load();
//        asignacionTareasPendientesProgramarDl.load();
//    }

    @Install(to = "proveedoresAfectadosDl", target = Target.DATA_LOADER)
    private List<Proveedor> proveedoresAfectadosDlLoadDelegate(LoadContext<Proveedor> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is not null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l2 = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();

        l.addAll(l2);
        ArrayList proveedores = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            Proveedor p = l.get(i).getProveedor();
            if (proveedores.indexOf(p)==-1){
                proveedores.add(p);
            }
        }
        Collections.sort(proveedores, new Comparator<Proveedor>() {
            public int compare(Proveedor p1, Proveedor p2){
                return p1.getPersona().getNombreCompleto().compareTo(p2.getPersona().getNombreCompleto());
            }
        });
        return proveedores;
    }



    @Install(to = "departamentosAfectadosDl", target = Target.DATA_LOADER)
    private List<Departamento> departamentosAfectadosDlLoadDelegate(LoadContext<Departamento> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is not null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l2 = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();

        l.addAll(l2);
        ArrayList deptos = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            Departamento d = l.get(i).getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
            if (deptos.indexOf(d)==-1){
                deptos.add(d);
            }
        }
        Collections.sort(deptos, new Comparator<Departamento>() {
            public int compare(Departamento d1, Departamento d2){
                return d1.getNombreDescriptivoCompleto().compareTo(d2.getNombreDescriptivoCompleto());
            }
        });
        return deptos;
    }





    @Install(to = "asignacionTareasProgramadasDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasProgramadasDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is not null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        if (this.deptoProgramadas!=null){
            ArrayList al = new ArrayList();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento().getUuid().compareTo(this.deptoProgramadas.getUuid())==0){
                    al.add(at);
                }
            }
            l = al;
        }
        if (this.proveedor!=null){
            ArrayList al = new ArrayList();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (at.getProveedor().getUuid().compareTo(this.proveedor.getUuid())==0){
                    al.add(at);
                }

            }
            l = al;
        }
        return l;
    }

    @Install(to = "asignacionTareasPendientesProgramarDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasPendientesProgramarDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        if (this.deptoProgramadas!=null){
            ArrayList al = new ArrayList();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento().getUuid().compareTo(this.deptoProgramadas.getUuid())==0){
                    al.add(at);
                }
            }
            l = al;
        }
        if (this.proveedor!=null){
            ArrayList al = new ArrayList();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (at.getProveedor().getUuid().compareTo(this.proveedor.getUuid())==0){
                    al.add(at);
                }

            }
            l = al;
        }
        return l;
    }

    @Install(to = "asignacionTareasFinalizadasDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasFinalizadasDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaFinalizacion is not null and (at.cancelado = false or at.cancelado is null)";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        if (this.deptoProgramadas!=null){
            ArrayList al = new ArrayList();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento().getUuid().compareTo(this.deptoProgramadas.getUuid())==0){
                    al.add(at);
                }
            }
            l = al;
        }
        if (this.proveedor!=null){
            ArrayList al = new ArrayList();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (at.getProveedor().getUuid().compareTo(this.proveedor.getUuid())==0){
                    al.add(at);
                }

            }
            l = al;
        }
        return l;
    }

    public void verTareaProgramada(){
        AsignacionTarea at = asignacionTareasProgramadasTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro a ver").show();
            return;
        }

        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            asignacionTareasPendientesProgramarDl.load();
            asignacionTareasProgramadasDl.load();
            asignacionTareasFinalizadasDl.load();
            asignacionTareasProgramadasTable.sort("fechaPrevistoInicio", DataGrid.SortDirection.DESCENDING);
        });
    }

    public void verTareaPendiente(){
        AsignacionTarea at = asignacionTareasPendientesProgramacionTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro a ver").show();
            return;
        }


        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            asignacionTareasPendientesProgramarDl.load();
            asignacionTareasProgramadasDl.load();
            asignacionTareasFinalizadasDl.load();
            asignacionTareasProgramadasTable.sort("fechaPrevistoInicio", DataGrid.SortDirection.DESCENDING);
        });
    }

    public void verTareaFinalizada(){
        AsignacionTarea at = asignacionTareasFinalizadasTable.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro a ver").show();
            return;
        }


        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            asignacionTareasPendientesProgramarDl.load();
            asignacionTareasProgramadasDl.load();
            asignacionTareasFinalizadasDl.load();
            asignacionTareasProgramadasTable.sort("fechaPrevistoInicio", DataGrid.SortDirection.DESCENDING);
        });
    }



}