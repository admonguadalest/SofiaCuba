package com.company.test1.web.screens.asignaciontarea;

import com.company.test1.entity.ciclos.AsignacionTarea;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.NotaIntervencion;
import com.company.test1.entity.ciclos.gantasignacionestareas.Segment;
import com.company.test1.entity.ciclos.gantasignacionestareas.TaskSpan;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.service.ContratosService;
import com.haulmont.charts.gui.components.charts.GanttChart;
import com.haulmont.charts.web.widgets.amcharts.CubaAmchartsIntegration;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.reports.entity.ParameterType;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportInputParameter;
import com.haulmont.reports.entity.ReportOutputType;
import com.haulmont.reports.gui.ReportGuiManager;
import com.haulmont.reports.gui.actions.TablePrintFormAction;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Color;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_CoordinacionAsignacionesTarea")
@UiDescriptor("coordinacion-asignaciones-tarea.xml")
public class CoordinacionAsignacionesTarea extends Screen {

    Hashtable<String, String> colors = new Hashtable();

    @Inject
    private GanttChart ganttChart;
    @Inject
    private CollectionLoader<AsignacionTarea> asignacionTareasProgramadasDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private DataGrid<AsignacionTarea> asignacionTareasProgramadasTable;
    @Inject
    private CollectionLoader<AsignacionTarea> asignacionTareasPendientesProgramarDl;
    @Inject
    private CollectionContainer<AsignacionTarea> asignacionTareasProgramadasDc;
    @Inject
    private CollectionContainer<AsignacionTarea> asignacionTareasPendientesProgramarDc;
    @Inject
    private DataGrid<AsignacionTarea> asignacionTareasPendientesProgramacionTable;
    @Inject
    private Notifications notifications;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Button printProgramadas;
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
    @Inject
    private ReportGuiManager reportGuiManager;
    @Inject
    private UserSession userSession;
    @Inject
    private CollectionLoader<TaskSpan> taskSpanDl;
    @Inject
    private Metadata metadata;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private CollectionContainer<TaskSpan> taskSpanDc;
    @Inject
    private Button btnRefrescar;
    @Inject
    private TokenList<Proveedor> tknProveedores;
    @Inject
    private CollectionContainer<Proveedor> proveedoresAfectadosDc;
    @Inject
    private CollectionLoader<AsignacionTarea> ganttTareasPendientesDl;
    @Inject
    private CollectionLoader<AsignacionTarea> ganttTareasEnCursoDl;
    @Inject
    private DataGrid<AsignacionTarea> dataGridTareasPendientesIndustrialesFiltro;
    @Inject
    private DataGrid<AsignacionTarea> dataGridTareasEnCursoIndustrialesFiltro;

    private void populateColors(){
        colors.put("red", "#FF0000");
        colors.put("orange",  "#FFDB88");
        colors.put("yellow",  "#FEFE22");
        colors.put("green",  "#66FF66");
        colors.put("cyan",  "#00FFFF");
        colors.put("blue",  "#0000FF");
        colors.put("magenta",  "#FF00FF");
        colors.put("purple",  "#A020F0");
        colors.put("black",  "#000000");
        colors.put("gray",  "#C0C0C0");
        colors.put("pink",  "#FF66CC");
        colors.put("brown",  "#E2725B");
        colors.put("beige",  "#FFEBCD");
        colors.put("olive",  "#9AB973");
        colors.put("violet",  "#9F00FF");
    }

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

    public void reportTareasProgramadas(){
        Report r = dataManager.load(Report.class).query("select r from report$Report r " +
                "where r.name = :rn").parameter("rn", "Listado Tareas")
                .one();
        HashMap pams = new HashMap();
        pams.put("entities", asignacionTareasProgramadasDc.getItems());
        pams.put("TITULO", "TAREAS PROGRAMADAS");
        pams.put("FECHA_REPORT", new Date());
        ReportOutputDocument rod = reportGuiManager.getReportResult(r, pams, null);
        exportDisplay.show(new ByteArrayDataProvider(rod.getContent()), "Tareas Programadas.pdf");
        int y = 2;
    }

    public void reportTareasPendientes(){
        Report r = dataManager.load(Report.class).query("select r from report$Report r " +
                "where r.name = :rn").parameter("rn", "Listado Tareas")
                .one();
        HashMap pams = new HashMap();
        pams.put("entities", asignacionTareasPendientesProgramarDc.getItems());
        pams.put("TITULO", "TAREAS PENDIENTES");
        pams.put("FECHA_REPORT", new Date());
        ReportOutputDocument rod = reportGuiManager.getReportResult(r, pams, null);
        exportDisplay.show(new ByteArrayDataProvider(rod.getContent()), "Tareas Programadas.pdf");
        int y = 2;
    }

    @Subscribe
    public void onInit(InitEvent event) {
        populateColors();
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

        taskSpanDl.load();

        tknProveedores.addValueChangeListener(e->{
            taskSpanDl.load();
            ganttTareasPendientesDl.load();
            ganttTareasEnCursoDl.load();
        });

        btnRefrescar.addClickListener(e->{
            taskSpanDl.load();
        });

        ganttTareasPendientesDl.load();
        ganttTareasEnCursoDl.load();







    }

    @Install(to = "ganttTareasEnCursoDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> ganttTareasEnCursoDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        List<Proveedor> provs = new ArrayList();
        if (tknProveedores.getValue()==null){
            tknProveedores.setValue(new ArrayList());
        }
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is not null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        if (tknProveedores.getValue().size()==0){
            return l;
        }else{
            provs = new ArrayList(tknProveedores.getValue());
            ArrayList<AsignacionTarea> aatt = new ArrayList<AsignacionTarea>();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (provs.indexOf(at.getProveedor())!=-1){
                    aatt.add(at);
                }
            }
            Collections.sort(aatt, new Comparator<AsignacionTarea>() {
                @Override
                public int compare(AsignacionTarea o1, AsignacionTarea o2) {
                    return o1.getProveedor().getPersona().getNombreCompleto().compareTo(o2.getProveedor().getPersona().getNombreCompleto());
                }
            });
            return aatt;
        }

    }


    @Subscribe("lkpVaciosOcupados")
    public void onLkpVaciosOcupadosValueChange(HasValue.ValueChangeEvent event) {
        if (event.isUserOriginated())
            refrescar();
    }

    @Install(to = "ganttTareasPendientesDl", target = Target.DATA_LOADER)
    private List<AsignacionTarea> ganttTareasPendientesDlLoadDelegate(LoadContext<AsignacionTarea> loadContext) {
        List<Proveedor> provs = new ArrayList();
        if (tknProveedores.getValue()==null){
            tknProveedores.setValue(new ArrayList());
        }
        String query = "select at from test1_AsignacionTarea at WHERE at.fechaPrevistoInicio is null and (at.cancelado = false or at.cancelado is null) and at.fechaFinalizacion is null";
        List<AsignacionTarea> l = dataManager.load(AsignacionTarea.class).query(query).view("asignacionTarea-view-ext").list();
        if (tknProveedores.getValue().size()==0){
            return l;
        }else{
            List<Proveedor> proveedores = new ArrayList(tknProveedores.getValue());
            ArrayList<AsignacionTarea> aatt = new ArrayList<AsignacionTarea>();
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                if (proveedores.indexOf(at.getProveedor())!=-1){
                    aatt.add(at);
                }
            }
            Collections.sort(aatt, new Comparator<AsignacionTarea>() {
                @Override
                public int compare(AsignacionTarea o1, AsignacionTarea o2) {
                    return o1.getProveedor().getPersona().getNombreCompleto().compareTo(o2.getProveedor().getPersona().getNombreCompleto());
                }
            });
            return aatt;
        }

    }



    @Install(to = "taskSpanDl", target = Target.DATA_LOADER)
    private List<TaskSpan> taskSpanDlLoadDelegate(LoadContext<TaskSpan> loadContext) {
        List<AsignacionTarea> tareasProgramadas = asignacionTareasProgramadasDc.getItems();
        Hashtable<Departamento, List<AsignacionTarea>> htAsignaciones = new Hashtable<>();
        ArrayList<TaskSpan> al = new ArrayList();


        List<Proveedor> proveedoresFiltro = new ArrayList();
        try{
            proveedoresFiltro = new ArrayList(tknProveedores.getValue());
        }catch(Exception exc){

        }


        for (int i = 0; i < tareasProgramadas.size(); i++) {
            AsignacionTarea at = tareasProgramadas.get(i);
            if (proveedoresFiltro.size()!=0){
                if (proveedoresFiltro.indexOf(at.getProveedor())==-1){
                    continue;
                }
            }
            Departamento d = at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
            if (!htAsignaciones.containsKey(d)){
                htAsignaciones.put(d, new ArrayList<AsignacionTarea>());
            }
            htAsignaciones.get(d).add(at);
        }


        Iterator<Departamento> itdeptos = htAsignaciones.keySet().iterator();
        Hashtable<Departamento, TaskSpan> htTaskSpans = new Hashtable<>();
        while(itdeptos.hasNext()){
            Departamento d = itdeptos.next();
            List<AsignacionTarea> l = htAsignaciones.get(d);
            Collections.sort(l, new Comparator<AsignacionTarea>() {
                @Override
                public int compare(AsignacionTarea o1, AsignacionTarea o2) {
                    return o1.getFechaPrevistoInicio().compareTo(o2.getFechaPrevistoInicio());
                }
            });
            if (!htTaskSpans.containsKey(d)){
                TaskSpan ts = new TaskSpan();
                ts.setCategory(d.getNombreDescriptivoCompleto());
                htTaskSpans.put(d, ts);
                al.add(ts);
            }
            List<Proveedor> proveedores = proveedoresAfectadosDc.getItems();
            List<String> colorsList = new ArrayList(colors.keySet());
            int colorLen = colorsList.size();
            TaskSpan ts = htTaskSpans.get(d);
            for (int i = 0; i < l.size(); i++) {
                AsignacionTarea at = l.get(i);
                Proveedor p = at.getProveedor();
                Segment s = new Segment();
                s.setTask(at.getProveedor().getPersona().getNombreCompleto() + " - " + at.getDescripcion());
                s.setStart(at.getFechaPrevistoInicio());
                //ojo: temporalmente getFechaPrevista se usa como getFechaPrevistaFinalizacion
                if (at.getFechaPrevista()!=null){
                    Date d1 = at.getFechaPrevista();
                    Date d2 = at.getFechaPrevistoInicio();
                    long time = d1.getTime() - d2.getTime();
                    int days = (int)Math.floor(time /(long)(1000*60*60*24));
                    s.setDuration(days);
                }else{
                    s.setDuration(2);
                }

                s.setColor(colors.get(colorsList.get(proveedores.indexOf(p)%colorLen)));
                ts.getSegments().add(s);
            }

        }
        Collections.sort(al, new Comparator<TaskSpan>() {
            @Override
            public int compare(TaskSpan o1, TaskSpan o2) {

                return o1.getCategory().compareTo(o2.getCategory());
            }
        });
        return al;
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

    public void verUltimasNotas(){
        screenBuilders.lookup(NotaIntervencion.class, this).withOpenMode(OpenMode.DIALOG)
                .build().show();
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

    @Subscribe("dataGridTareasPendientesIndustrialesFiltro.edit")
    public void onDataGridTareasPendientesIndustrialesFiltroEdit(Action.ActionPerformedEvent event) {
        AsignacionTarea at = dataGridTareasPendientesIndustrialesFiltro.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            ganttTareasEnCursoDl.load();
            ganttTareasPendientesDl.load();
            taskSpanDl.load();
        });
    }

    @Subscribe("dataGridTareasEnCursoIndustrialesFiltro.edit")
    public void onDataGridTareasEnCursoIndustrialesFiltroEdit(Action.ActionPerformedEvent event) {
        AsignacionTarea at = dataGridTareasEnCursoIndustrialesFiltro.getSingleSelected();
        if (at==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        EditorBuilder eb = screenBuilders.editor(AsignacionTarea.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .editEntity(at).withScreenId("test1_AsignacionTarea.edit-ext");


        Screen s = eb.build().show();
        s.addAfterCloseListener(e->{
            ganttTareasEnCursoDl.load();
            ganttTareasPendientesDl.load();
            taskSpanDl.load();
        });
    }







}