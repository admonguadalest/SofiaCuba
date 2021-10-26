package com.company.test1.web.screens.asignaciontarea;

import com.company.test1.entity.ciclos.AsignacionTarea;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.service.ContratosService;
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
import java.util.*;

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
    @Inject
    private LookupField<String> lkpVaciosOcupados;
    @Inject
    private ContratosService contratosService;

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

        lkpVaciosOcupados.setOptionsList(Arrays.asList(new String[]{"Todos","Vacíos","Ocupados"}));
        lkpVaciosOcupados.setValue("Todos");
    }

    @Subscribe("lkpVaciosOcupados")
    public void onLkpVaciosOcupadosValueChange(HasValue.ValueChangeEvent event) {
        if (event.isUserOriginated())
            refrescar();
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
        if (lkpVaciosOcupados.getValue()!=null) {
            if (lkpVaciosOcupados.getValue().compareTo("Todos") != 0) {
                ArrayList vacios = new ArrayList();
                ArrayList ocupados = new ArrayList();
                for (int i = 0; i < deptos.size(); i++) {
                    Departamento d = (Departamento) deptos.get(i);
                    try {
                        ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento(d, "_minimal");
                        if (ci != null) {
                            ocupados.add(d);
                        } else {
                            vacios.add(d);
                        }
                    } catch (Exception exc) {
                        notifications.create().withCaption("Error al consultar contratos asociados").show();
                        return new ArrayList();
                    }
                }
                if (lkpVaciosOcupados.getValue().compareTo("Vacíos") == 0) {
                    deptos = vacios;
                } else {
                    deptos = ocupados;
                }
            }
        }
        Collections.sort(deptos, new Comparator<Departamento>() {
            public int compare(Departamento d1, Departamento d2){
                return d1.getNombreDescriptivoCompleto().compareTo(d2.getNombreDescriptivoCompleto());
            }
        });
        return deptos;
    }


    private List<AsignacionTarea> filtraAsignacionesTareaSegunControles(List<AsignacionTarea> l){
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
        if (lkpVaciosOcupados.getValue()!=null) {
            if (lkpVaciosOcupados.getValue().compareTo("Todos") != 0) {
                ArrayList aatt_vacios = new ArrayList();
                ArrayList aatt_ocupados = new ArrayList();
                for (int i = 0; i < l.size(); i++) {
                    Departamento d = (Departamento) l.get(i).getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
                    try {
                        ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento(d, "_minimal");
                        if (ci != null) {
                            aatt_ocupados.add(l.get(i));
                        } else {
                            aatt_vacios.add(l.get(i));
                        }
                    } catch (Exception exc) {
                        notifications.create().withCaption("Error al consultar contratos asociados").show();
                        return new ArrayList();
                    }
                }
                if (lkpVaciosOcupados.getValue().compareTo("Vacíos") == 0) {
                    l = aatt_vacios;
                } else {
                    l = aatt_ocupados;
                }
            }
        }
        return l;
    }


    @Install(to = "asignacionTareasProgramadasDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasProgramadasDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is not null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        l = filtraAsignacionesTareaSegunControles(l);
        return l;
    }

    @Install(to = "asignacionTareasPendientesProgramarDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasPendientesProgramarDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        l = filtraAsignacionesTareaSegunControles(l);
        return l;
    }

    @Install(to = "asignacionTareasFinalizadasDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> asignacionTareasFinalizadasDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaFinalizacion is not null and (at.cancelado = false or at.cancelado is null)";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        l = filtraAsignacionesTareaSegunControles(l);
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