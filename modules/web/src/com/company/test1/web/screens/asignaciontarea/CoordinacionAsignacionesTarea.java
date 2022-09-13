package com.company.test1.web.screens.asignaciontarea;

import com.company.test1.entity.ciclos.*;
import com.company.test1.entity.ciclos.gantasignacionestareas.Segment;
import com.company.test1.entity.ciclos.gantasignacionestareas.TaskSpan;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.service.CicloService;
import com.company.test1.service.ContratosService;
import com.haulmont.charts.gui.components.charts.GanttChart;
import com.haulmont.charts.web.widgets.amcharts.CubaAmchartsIntegration;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
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
import java.text.SimpleDateFormat;
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
    @Inject
    private LookupField<String> lkpVaciosOcupadosVR;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private VBoxLayout vbDynEntradas;
    @Inject
    private VBoxLayout vbDynTareasGestionPresupuestaria;
    @Inject
    private CicloService cicloService;

    private boolean habilitarDesmonitorizacionRapidaDeEntradas = true;

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

        lkpVaciosOcupadosVR.setOptionsList(Arrays.asList(new String[]{"Todos","Vacíos","Ocupados"}));
        lkpVaciosOcupadosVR.setValue("Vacíos");

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


        cargarEntradasSinAsignacionesTareas();
        cargarAsignacionesTareasGestionPresupuestaria();
        cargarAsignacionesTareasPendientesDePlanificacion();



    }

    @Subscribe("lkpVaciosOcupadosVR")
    public void onLkpVaciosOcupadosVRValueChange(HasValue.ValueChangeEvent event) {
        if (event.isUserOriginated())
            onBtnRefrescarClick(null);
    }



    @Subscribe("btnRefrescarVistaRapida")
    public void onBtnRefrescarClick(Button.ClickEvent event) {
        cargarEntradasSinAsignacionesTareas();
        cargarAsignacionesTareasGestionPresupuestaria();
        cargarAsignacionesTareasPendientesDePlanificacion();
    }



    private void cargarEntradasSinAsignacionesTareas()  {
        //cargando datos de la vista rapida
        try {
            vbDynEntradas.removeAll();
            boolean seHallaronRegistros = false;
            List<Entrada> ee = getEntradasConOrdenesTrabajoSinAsignacionesTareas();
            GridLayout gl = null;
            gl = uiComponents.create(GridLayout.NAME);

            if (this.habilitarDesmonitorizacionRapidaDeEntradas){
                gl.setColumns(4);
            }else{
                gl.setColumns(3);
            }
            gl.setWidth("100%");
            gl.setRows(100);
            if (ee.size()>0){

                int rowCounter = -1;

                for (int i = 0; i < ee.size(); i++) {

                    Entrada e = ee.get(i);
                    final Entrada e_ = e;
                    /*if (e.getCiclo().getDepartamento().getPiso().indexOf("FINCA")!=-1){
                        continue;
                    }*/
                    Departamento d = e.getCiclo().getDepartamento();
                    ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento(d, "_minimal");
                    boolean anadir = false;
                    String vo = lkpVaciosOcupadosVR.getValue();
                    if (vo.compareTo("Todos")==0){
                        anadir = true;
                    }else if(vo.compareTo("Vacíos")==0){
                        anadir = (ci==null);
                    }else{
                        anadir = (ci!=null);
                    }
                    if (anadir){
                        seHallaronRegistros = true;
                        rowCounter++;



                        vbDynEntradas.add(gl);
                        Button b = uiComponents.create(Button.NAME);
                        b.addClickListener(ev->{
                            Screen s = screenBuilders.editor(Ciclo.class, this).editEntity(e_.getCiclo())
                                    .withOpenMode(OpenMode.NEW_TAB)
                                    .build();
                            s.addAfterCloseListener(ev2->{
                                onBtnRefrescarClick(null);
                            });
                            s.show();
                        });
                        b.setStyleName("link");
                        b.setCaption(e.getCiclo().getDepartamento().getNombreDescriptivoCompleto() );
                        gl.add(b, 0,rowCounter);

                        Label lev = uiComponents.create(Label.NAME);
                        try{
                            lev.setValue(e.getEvento().getNombre());
                        }catch(Exception exc){
                            lev.setValue("(!Entrada sin evento asociado)");
                        }
                        gl.add(lev, 1,rowCounter);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Label lev2 = uiComponents.create(Label.NAME);
                        try{
                            lev2.setValue(sdf.format(e.getFechaEntrada()));
                        }catch(Exception exc){
                            lev2.setValue("!Sin valor de fecha");
                        }
                        gl.add(lev2, 2,rowCounter);

                        if (habilitarDesmonitorizacionRapidaDeEntradas){
                            CheckBox chb = uiComponents.create(CheckBox.NAME);
                            chb.setCaption("Desmonitorizar Entrada");
                            chb.addValueChangeListener(ev3->{
                                e_.getOrdenTrabajo().setExcluirDeMonitorizacionEncargado(true);
                                dataManager.commit(e_.getOrdenTrabajo());
                                notifications.create().withCaption("Orden Trabajo actualizada!")
                                        .show();
                            });
                            gl.add(chb, 3,rowCounter);
                        }

                    }


                    int y = 2;
                }
            }

            if (!seHallaronRegistros){
                Label l = uiComponents.create(Label.NAME);
                l.setValue("(0 registros hallados)");
                vbDynEntradas.add(l);
            }else{
                vbDynEntradas.add(gl);
            }


        } catch (Exception e) {
            e.printStackTrace();
            String m = "";
            if ((e.getMessage()==null)){
                m = e.getClass().getSimpleName();
            }else if(e.getMessage().trim().length()==0){
                m = e.getClass().getSimpleName();
            }
            else{
                m = e.getMessage();
            }
            notifications.create().withDescription(m).show();
        }

    }

    private void cargarAsignacionesTareasGestionPresupuestaria(){
        try{
            vbDynTareasGestionPresupuestaria.removeAll();
            List<AsignacionTarea> aatt = getAsignacionesTareasGestionPresupuestaria();
            GridLayout gl = null;
            gl = uiComponents.create(GridLayout.NAME);

            gl.setColumns(3);
            gl.setWidth("100%");
            gl.setRows(100);
            boolean seHallaronRegistros = false;
            if (aatt.size()>0){


                int rowCounter = -1;
                for (int i = 0; i < aatt.size(); i++) {

                    AsignacionTarea at = aatt.get(i);
                    at = dataManager.reload(at, "asignacionTarea-view-ext");
                    final AsignacionTarea at_ = at;

                    Departamento d = at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
                    ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento(d);
                    boolean anadir = false;
                    String vo = lkpVaciosOcupadosVR.getValue();
                    if (vo.compareTo("Todos")==0){
                        anadir = true;
                    }else if(vo.compareTo("Vacíos")==0){
                        anadir = (ci==null);
                    }else{
                        anadir = (ci!=null);
                    }
                    if (anadir) {
                        rowCounter++;
                        seHallaronRegistros = true;

                        Button b = uiComponents.create(Button.NAME);
                        b.addClickListener(ev -> {
                            Screen s = screenBuilders.editor(AsignacionTarea.class, this).editEntity(at_)
                                    .withScreenId("test1_AsignacionTarea.edit-ext")
                                    .withOpenMode(OpenMode.DIALOG).build();
                            s.addAfterCloseListener(ev2->{
                                onBtnRefrescarClick(null);
                            });
                            s.show();
                        });
                        b.setStyleName("link");
                        b.setCaption(at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento().getNombreDescriptivoCompleto());
                        gl.add(b, 0, rowCounter);

                        Label lev = uiComponents.create(Label.NAME);
                        lev.setValue(at.getProveedor().getPersona().getNombreCompleto());
                        gl.add(lev, 1, rowCounter);

                        Label lev2 = uiComponents.create(Label.NAME);
                        lev2.setValue(at.getGestionPresupuestaria().name());
                        gl.add(lev2, 2, rowCounter);

                    }
                }

            }

            if (!seHallaronRegistros){
                Label l = uiComponents.create(Label.NAME);
                l.setValue("(0 registros hallados)");
                vbDynTareasGestionPresupuestaria.add(l);
            }else{
                vbDynTareasGestionPresupuestaria.add(gl);
            }



        }catch(Exception exc){
            String m = "";
            if (exc.getMessage().trim().length()==0){
                m = exc.getClass().getSimpleName();
            }else{
                m = exc.getMessage();
            }
            notifications.create().withDescription(m).show();

        }
    }

    @Inject
    private VBoxLayout vbDynTareasProgramables;

    private void cargarAsignacionesTareasPendientesDePlanificacion(){
        try{
            vbDynTareasProgramables.removeAll();
            boolean seHallaronRegistros = false;
            List<AsignacionTarea> aatt = getAsignacionesTareasPendientesDePlanificacion();
            GridLayout gl = null;
            gl = uiComponents.create(GridLayout.NAME);

            gl.setColumns(3);
            gl.setWidth("100%");
            gl.setRows(100);
            if (aatt.size()>0){


                int rowCounter = -1;
                for (int i = 0; i < aatt.size(); i++) {

                    AsignacionTarea at = aatt.get(i);
                    at = dataManager.reload(at, "asignacionTarea-view-ext");
                    final AsignacionTarea at_ = at;

                    Departamento d = at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento();
                    ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento(d);
                    boolean anadir = false;
                    String vo = lkpVaciosOcupadosVR.getValue();
                    if (vo.compareTo("Todos")==0){
                        anadir = true;
                    }else if(vo.compareTo("Vacíos")==0){
                        anadir = (ci==null);
                    }else{
                        anadir = (ci!=null);
                    }
                    if (anadir) {
                        rowCounter++;
                        seHallaronRegistros = true;


                        Button b = uiComponents.create(Button.NAME);
                        b.addClickListener(ev -> {
                            Screen s = screenBuilders.editor(AsignacionTarea.class, this).editEntity(at_)
                                    .withScreenId("test1_AsignacionTarea.edit-ext")
                                    .withOpenMode(OpenMode.DIALOG).build();
                            s.addAfterCloseListener(ev2->{
                                onBtnRefrescarClick(null);
                            });
                            s.show();
                        });
                        b.setStyleName("link");
                        b.setCaption(at.getOrdenTrabajo().getEntrada().getCiclo().getDepartamento().getNombreDescriptivoCompleto());
                        gl.add(b, 0, rowCounter);

                        Label lev = uiComponents.create(Label.NAME);
                        lev.setValue(at.getProveedor().getPersona().getNombreCompleto());
                        gl.add(lev, 1, rowCounter);

                        Label lev2 = uiComponents.create(Label.NAME);
                        lev2.setValue(at.getGestionPresupuestaria().name());
                        gl.add(lev2, 2, rowCounter);

                    }
                }

            }

            if (!seHallaronRegistros){
                Label l = uiComponents.create(Label.NAME);
                l.setValue("(0 registros hallados)");
                vbDynTareasProgramables.add(l);
            }else{
                vbDynTareasProgramables.add(gl);
            }



        }catch(Exception exc){

            String m = "";
            if (exc.getMessage().trim().length()==0){
                m = exc.getClass().getSimpleName();
            }else{
                m = exc.getMessage();
            }
            notifications.create().withDescription(m).show();
        }
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


        Screen s = eb.build();
        s.addAfterCloseListener(e->{
            ganttTareasEnCursoDl.load();
            ganttTareasPendientesDl.load();
            taskSpanDl.load();
        });
        s.show();
    }
    
    
    private List<Entrada> getEntradasConOrdenesTrabajoSinAsignacionesTareas() throws Exception{
        String ocupadosVaciosStr = lkpVaciosOcupadosVR.getValue();
        Boolean ocupadosVacios = null;
        if (ocupadosVaciosStr.compareTo("Todos")==0){
            ocupadosVacios = null;
        }else if(ocupadosVaciosStr.compareTo("Vacíos")==0){
            ocupadosVacios = false;
        }else{
            ocupadosVacios = true;
        }
        List<Entrada> ee = cicloService.getEntradasConOrdenesTrabajoSinAsignacionesTareas(ocupadosVacios);


        return ee;
    } 


    public List<AsignacionTarea> getAsignacionesTareasGestionPresupuestaria() throws Exception{
        String hql = "SELECT at from test1_AsignacionTarea at join at.ordenTrabajo ot join ot.entrada e " +
                "join e.ciclo c join c.departamento d where at.fechaFinalizacion is null and " +
                "at.gestionPresupuestaria is not null and " +
                "at.gestionPresupuestaria <> :ejsinppto and " +
                "at.gestionPresupuestaria <> :pptoaprob and " +
                "c.estadoCiclo = 1 and c.tipoCiclo = 'OPERATIVO' and d.piso <> 'FINCA' and (ot.excluirDeMonitorizacionEncargado = false or ot.excluirDeMonitorizacionEncargado is null)";
        List<AsignacionTarea> aatt = dataManager.load(AsignacionTarea.class).query(hql)
                .parameter("ejsinppto", GestionPresupuestariaEnum.SIN_PRESUPUESTO)
                .parameter("pptoaprob", GestionPresupuestariaEnum.PRESUPUESTO_APROBADO)
                .list();
        return aatt;

    }


    public List<AsignacionTarea> getAsignacionesTareasPendientesDePlanificacion() throws Exception{
        String hql = "SELECT at from test1_AsignacionTarea at join at.ordenTrabajo ot join ot.entrada e " +
                "join e.ciclo c join c.departamento d where at.fechaFinalizacion is null and " +
                "at.gestionPresupuestaria is not null and " +
                "(at.gestionPresupuestaria = :ejsinppto or " +
                "at.gestionPresupuestaria = :pptoaprob) and " +
                "at.fechaPrevistoInicio is null and " +
                "c.estadoCiclo = 1 and c.tipoCiclo = 'OPERATIVO' and d.piso <> 'FINCA' and (ot.excluirDeMonitorizacionEncargado = false or ot.excluirDeMonitorizacionEncargado is null)";
        List<AsignacionTarea> aatt = dataManager.load(AsignacionTarea.class).query(hql)
                .parameter("ejsinppto", GestionPresupuestariaEnum.SIN_PRESUPUESTO)
                .parameter("pptoaprob", GestionPresupuestariaEnum.PRESUPUESTO_APROBADO)
                .list();
        return aatt;

    }




}