package com.company.test1.web.screens.externos;

import com.company.test1.entity.Persona;
import com.company.test1.entity.TreeItem;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.company.test1.service.JasperReportService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import freemarker.template.utility.DateUtil;
import org.apache.http.client.utils.DateUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

@UiController("test1_InformeIva")
@UiDescriptor("informe-iva.xml")
public class InformeIva extends Screen {
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private CollectionContainer<TreeItem> treeItemsDc;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private CollectionContainer<Propietario> propietariosDc;
    @Inject
    private Table<Propietario> tablePropietarios;
    @Inject
    private LookupField lkpAnadirInfoTrimestral;
    @Inject
    private DateField<Date> dteFechaHasta;
    @Inject
    private LookupField lkpAnadirInfoGlobal;
    @Inject
    private Tree<TreeItem> treeUbicacionesDepartamentos;
    @Inject
    private DataManager dataManager;
    @Inject
    private DateField<Date> dteFechaDesde;
    boolean treeUbicacionesDepartamentos_triggerSelectionEvents = true;
    @Inject
    private Notifications notifications;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        propietariosDl.load();
    }

    @Subscribe("tablePropietarios")
    private void onTablePropietariosSelection(Table.SelectionEvent<Propietario> event) {
        treeItemsDl.load();
    }

    @Install(to = "treeItemsDl", target = Target.DATA_LOADER)
    private List<TreeItem> treeItemsDlLoadDelegate(LoadContext<TreeItem> loadContext) {
        List<Propietario> props = Arrays.asList(tablePropietarios.getSelected().toArray(new Propietario[0]));

        ArrayList<Departamento> deptos = new ArrayList<Departamento>();
        for (int i = 0; i < props.size(); i++) {
            Propietario prop =  props.get(i);
            List<Departamento> dd = dataManager.loadValue("select d from test1_Departamento d JOIN d.ubicacion u where u.propietario.id = '" + prop.getId() + "'", Departamento.class).list();
            deptos.addAll(dd);
            dd = dataManager.loadValue("select d from test1_Departamento d where d.propietario.id = '" + prop.getId() + "'", Departamento.class).list();
            deptos.addAll(dd);
        }





        ArrayList<TreeItem> items = new ArrayList<TreeItem>();

        HashMap<Ubicacion, TreeItem> m = new HashMap<Ubicacion, TreeItem>();
        for (int i = 0; i < deptos.size(); i++) {
            Departamento d =  deptos.get(i);
            d = dataManager.reload(d, "departamento-view");
            TreeItem tid = new TreeItem();
            tid.setUserObject(d);
            if (!m.containsKey(d.getUbicacion())){
                TreeItem tiu = new TreeItem();
                tiu.setUserObject(d.getUbicacion());

                items.add(tiu);
                m.put(d.getUbicacion(), tiu);
            }
            tid.setParentItem(m.get(d.getUbicacion()));
            items.add(tid);
        }

        Collections.sort(items, new Comparator<TreeItem>() {
            @Override
            public int compare(TreeItem o1, TreeItem o2) {
                if ((o1.getUserObject() instanceof Ubicacion)&&(o2.getUserObject()instanceof Ubicacion)){
                    return (((Ubicacion)o1.getUserObject()).getNombre().compareTo(((Ubicacion)o2.getUserObject()).getNombre()));
                }
                if ((o1.getUserObject() instanceof Departamento)&&(o2.getUserObject()instanceof Departamento)){
                    return (((Departamento)o1.getUserObject()).getRm2id().compareTo(((Departamento)o2.getUserObject()).getRm2id()));
                }
                if ((o1.getUserObject() instanceof Ubicacion)&&(o2.getUserObject()instanceof Departamento)){
                    return 1;
                }
                if ((o1.getUserObject() instanceof Departamento)&&(o2.getUserObject()instanceof Ubicacion)){
                    return -1;
                }
                return 1;
            }
        });

        return items;



    }

    @Subscribe("treeUbicacionesDepartamentos")
    private void onTreeUbicacionesDepartamentosSelection(Tree.SelectionEvent<TreeItem> event) {
        if (!treeUbicacionesDepartamentos_triggerSelectionEvents) return;
        List<TreeItem> ttii = new ArrayList(event.getSelected());
        List<TreeItem> selection = new ArrayList();
        for (int i = 0; i < ttii.size(); i++) {
            TreeItem ti = ttii.get(i);
            if (ti.getUserObject() instanceof Ubicacion){
                selection.add(ti);
                for (int j = 0; j < treeItemsDc.getItems().size(); j++) {
                    TreeItem t = treeItemsDc.getItems().get(j);
                    if (t.getUserObject() instanceof Departamento){
                        if (t.getParentItem() == ti){
                            selection.add(t);
                        }
                    }
                }
            }
        }
        treeUbicacionesDepartamentos_triggerSelectionEvents = false;
        treeUbicacionesDepartamentos.setSelected(selection);
        treeUbicacionesDepartamentos_triggerSelectionEvents = true;
    }


    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnSeleccionarTodosDeptosClick() {
        treeUbicacionesDepartamentos_triggerSelectionEvents = false;
        treeUbicacionesDepartamentos.setSelected(treeItemsDc.getItems());
        treeUbicacionesDepartamentos_triggerSelectionEvents = true;
    }

    public void onBtnVistualizarClick() {


        if ((dteFechaDesde.getValue()==null)||(dteFechaHasta.getValue()==null)){
            notifications.create().withCaption("Especificar Rango de Fechas").show();
            return;
        }
        if (tablePropietarios.getSingleSelected()==null){
            notifications.create().withCaption("Seleccionar un Propietario").show();
            return;
        }
        //pendiente
        ArrayList al = new ArrayList(treeUbicacionesDepartamentos.getSelected());
        List<Departamento> deptos = new ArrayList<Departamento>();
        for (int i = 0; i < al.size(); i++) {
            TreeItem ti = (TreeItem) al.get(i);
            if (ti.getUserObject() instanceof Departamento){
                deptos.add((Departamento) ti.getUserObject());
            }
        }
        if (deptos.size()==0){
            notifications.create().withCaption("Seleccionar Departamentos").show();
            return;
        }
        boolean anadirInfoTrimestral = false;
        try{
            anadirInfoTrimestral = (lkpAnadirInfoTrimestral.getValue().toString().compareTo("SI")==0);
        }catch(Exception exc){

        }
        boolean anadirInfoGlobal = false;
        try{
            anadirInfoGlobal = (lkpAnadirInfoGlobal.getValue().toString().compareTo("SI")==0);
        }catch(Exception exc){

        }




       try{
            byte[] bb = jasperReportService.realizaInformeIva(dteFechaDesde.getValue(), dteFechaHasta.getValue(),
                    tablePropietarios.getSingleSelected(), deptos, anadirInfoTrimestral, anadirInfoGlobal);
            exportDisplay.show(new ByteArrayDataProvider(bb), "Informe IVA.pdf");
        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage()).show();
        }




    }

    @Subscribe("dteFechaDesde")
    public void onDteFechaDesdeValueChange(HasValue.ValueChangeEvent<Date> event) {
        Date d = event.getValue();
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, c.getMaximum(Calendar.DAY_OF_MONTH));
        dteFechaHasta.setValue(c.getTime());
    }


}