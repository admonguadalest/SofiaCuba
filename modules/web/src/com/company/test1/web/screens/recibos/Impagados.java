package com.company.test1.web.screens.recibos;

import com.company.test1.entity.TreeItem;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.recibos.ReciboGradoImpago;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.Serie;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import org.apache.http.client.utils.DateUtils;

import javax.inject.Inject;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@UiController("test1_Impagados")
@UiDescriptor("impagados.xml")
public class Impagados extends Screen {

    @Inject
    private Tree<TreeItem> treeUbicacionesDepartamentos;
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private CollectionContainer<TreeItem> treeItemsDc;
    @Inject
    private Table<Recibo> tableResultados;
    @Inject
    private Table<Propietario> tablePropietarios;


    @Inject
    private DateField<Date> fechaDesde;
    @Inject
    private DateField<Date> fechaHasta;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private RecibosService recibosService;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionContainer<Recibo> recibosDc;
    @Inject
    private CollectionLoader<Serie> seriesDl;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private LookupField lkpContratos;
    @Inject
    private LookupField<ReciboGradoImpago> lkpGradoIncobrable;
    @Inject
    private LookupField<Serie> lkpSeries;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {

        propietariosDl.load();
        seriesDl.load();
        fechaDesde.setValue(Date.valueOf("2000-01-01"));
        fechaHasta.setValue(Date.valueOf(LocalDate.now()));

        lkpContratos.setOptionsList(Arrays.asList(EstadoContratoInquilinoEnum.VIGENTE, EstadoContratoInquilinoEnum.RENUNCIADO));
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
            d = dataManager.reload(d, "departamento-view-for-tree");
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
        if (event.isUserOriginated()){
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
                }else if (ti.getUserObject() instanceof Departamento){
                    selection.add(ti);
                }
            }
            treeUbicacionesDepartamentos.setSelected(selection);
        }


    }
    
    

    @Subscribe("tablePropietarios")
    private void onTablePropietariosSelection(Table.SelectionEvent<Propietario> event) {

        treeItemsDl.load();
        
    }


    public void onBtnLimpiarSeleccionTreeClick() {
        treeUbicacionesDepartamentos.setSelected(new ArrayList());

    }

    public void onBtnLimpiarSeleccionPropietariosClick() {

        tablePropietarios.setSelected(new ArrayList());

    }

    public void onBtnInvertirSeleccionPropietariosClick() {

    }

    public void onBtnSeleccionarTodosContratosClick() {

        treeUbicacionesDepartamentos.setSelected(treeItemsDc.getItems());

    }

    public void onBtnActualizarClick() {
        ArrayList<Propietario> props = new ArrayList<Propietario>();
        props.addAll(tablePropietarios.getSelected());
        ArrayList<Departamento> dtos = new ArrayList<Departamento>();
        List<TreeItem> ttii = new ArrayList(treeUbicacionesDepartamentos.getSelected());
        for (int i = 0; i < ttii.size(); i++) {
            TreeItem treeItem =  ttii.get(i);
            if (treeItem.getUserObject() instanceof Departamento){
                dtos.add((Departamento) treeItem.getUserObject());
            }
        }
        List<Recibo> rbosDevueltos = null;
        try {
            String estadoContrato = null;
            if (lkpContratos.getValue()!=null){
                estadoContrato = lkpContratos.getValue().toString();
            }
            String gradoImpago = null;
            if (lkpGradoIncobrable.getValue()!=null){
                gradoImpago = lkpGradoIncobrable.getValue().toString();
            }
            rbosDevueltos = recibosService.devuelveImpagadosAsociados(props, dtos, fechaDesde.getValue(), fechaHasta.getValue(), estadoContrato, gradoImpago, lkpSeries.getValue());
        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();
            return;
        }
        ArrayList<Recibo> recargados = new ArrayList<Recibo>();
        for (int i = 0; i < rbosDevueltos.size(); i++) {
            Recibo recibo =  rbosDevueltos.get(i);
            recibo = dataManager.reload(recibo, "recibo-view");
            recargados.add(recibo);
        }
        Collections.sort(recargados, new Comparator<Recibo>() {
            @Override
            public int compare(Recibo recibo, Recibo t1) {
               return recibo.getUtilitarioContratoInquilino().getDepartamento().getRm2id().compareTo(t1.getUtilitarioContratoInquilino().getDepartamento().getRm2id());
            }
        });
        recibosDc.getMutableItems().clear();
        recibosDc.getMutableItems().addAll(recargados);
    }

    public void onBtnPrevisualizarClick() {
        byte[] bb = new byte[0];
        try {
            //recargamos datos
            List<Recibo> rr = new ArrayList<Recibo>();
            for (int i = 0; i < recibosDc.getItems().size(); i++) {
                Recibo recibo =  recibosDc.getItems().get(i);
                recibo = dataManager.reload(recibo, "recibo-detalle-view");
                rr.add(recibo);
            }
            if (rr.size()==0){
                notifications.create().withCaption("Debe localizar al menos un resultado").show();
                return;
            }
            bb = jasperReportService.impagados(tablePropietarios.getSingleSelected(), rr, fechaDesde.getValue(), fechaHasta.getValue(), "", "");
            exportDisplay.show(new ByteArrayDataProvider(bb), "Impagados.pdf", ExportFormat.getByExtension("pdf"));
        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription("No se pudo producir el report de Recibos Impagados").show();
            e.printStackTrace();
        }

    }

    public void onBtnCerrarClick(){
        this.closeWithDefaultAction();
    }
}