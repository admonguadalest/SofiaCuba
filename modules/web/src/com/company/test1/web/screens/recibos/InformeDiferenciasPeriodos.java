package com.company.test1.web.screens.recibos;

import com.company.test1.entity.TreeItem;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.Serie;
import com.company.test1.service.ContratosService;
import com.company.test1.service.JasperReportService;
import com.company.test1.web.GuiUtils;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.*;

@UiController("test1_InformeDiferenciasPeriodos")
@UiDescriptor("informe-diferencias-periodos.xml")
public class InformeDiferenciasPeriodos extends Screen {

    @Inject
    private CollectionContainer<DefinicionRemesa> definicionRemesasDc;
    @Inject
    private CollectionContainer<Propietario> propietariosDc;
    @Inject
    private CollectionLoader<DefinicionRemesa> definicionRemesasDl;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private LookupField lkpSerie;
    @Inject
    private Table tblPropietarios;
    @Inject
    private Table<DefinicionRemesa> tblDefinicionesRemesa;
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private ContratosService contratosService;
    @Inject
    private DataManager dataManager;
    @Inject
    private DateField<Date> dteFechaRealizacion;
    @Inject
    private Tree<TreeItem> treeContratos;
    @Inject
    private DateField<Date> dtePeriodoAnteriorFechaHasta;
    @Inject
    private DateField<Date> dtePeriodoAnteriorFechaDesde;
    @Inject
    private DateField<Date> dteFechaValor;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private CollectionContainer<TreeItem> treeItemsDc;
    @Inject
    private DateField<Date> dteFechaCargo;
    @Inject
    private Notifications notifications;

    boolean treeContratos_triggerSelectionEvents = true;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        definicionRemesasDl.load();
        propietariosDl.load();
    }

    @Subscribe("tblPropietarios")
    private void onTblPropietariosSelection(Table.SelectionEvent<Propietario> event) {
        treeItemsDl.load();
    }

    @Subscribe("tblDefinicionesRemesa")
    private void onTblDefinicionesRemesaSelection(Table.SelectionEvent<DefinicionRemesa> event) {
        treeItemsDl.load();
    }

    @Subscribe("treeContratos")
    private void onTreeContratosSelection(Tree.SelectionEvent<TreeItem> event) {
        if (!treeContratos_triggerSelectionEvents) return;
        List<TreeItem> ttii = new ArrayList(event.getSelected());
        List<TreeItem> selection = new ArrayList();
        for (int i = 0; i < ttii.size(); i++) {
            TreeItem ti = ttii.get(i);
            if (ti.getUserObject() instanceof Ubicacion){
                selection.add(ti);
                for (int j = 0; j < treeItemsDc.getItems().size(); j++) {
                    TreeItem t = treeItemsDc.getItems().get(j);
                    if (t.getUserObject() instanceof ContratoInquilino){
                        if (t.getParentItem() == ti){
                            selection.add(t);
                        }
                    }
                }
            }
        }
        treeContratos_triggerSelectionEvents = false;
        treeContratos.setSelected(selection);
        treeContratos_triggerSelectionEvents = true;
    }
    
    
    
    
    
    

    @Install(to = "treeItemsDl", target = Target.DATA_LOADER)
    private List<TreeItem> treeItemsDlLoadDelegate(LoadContext<TreeItem> loadContext) throws Exception {
        ArrayList<TreeItem> al = new ArrayList<TreeItem>();
        if (tblPropietarios.getSelected().size()==0){
            return al;
        }else{
            List ddrr = new ArrayList(tblDefinicionesRemesa.getSelected());
            List props = new ArrayList(tblPropietarios.getSelected());
            List<ContratoInquilino> ccii = contratosService.devuelveContratosParaPropietarios(ddrr, props);
            for (int i = 0; i < ccii.size(); i++) {
                ContratoInquilino ci = ccii.get(i);
                ci = dataManager.reload(ci, "contratoInquilino-tree");
                ccii.set(i, ci);
            }
            Hashtable treeItemsUbicacion = new Hashtable();
            //organizmaos por ubicacion
            for (int i = 0; i < ccii.size(); i++) {
                ContratoInquilino ci = ccii.get(i);
                TreeItem ti = null;
                try{
                    ti = (TreeItem) treeItemsUbicacion.get(ci.getDepartamento().getUbicacion().getId());
                    if (ti==null) throw new Exception("");//para crear el treeitem en la gestion de la excepcion
                }catch(Exception exc){
                    ti = new TreeItem();
                    ti.setUserObject(ci.getDepartamento().getUbicacion());
                    treeItemsUbicacion.put(ci.getDepartamento().getUbicacion().getId(), ti);
                    al.add(ti);
                }
                TreeItem tid = new TreeItem();
                tid.setUserObject(ci);
                tid.setParentItem(ti);
                al.add(tid);

            }
        }

        Collections.sort(al, new Comparator<TreeItem>() {
            @Override
            public int compare(TreeItem o1, TreeItem o2) {
                if ((o1.getUserObject() instanceof Ubicacion)&&(o2.getUserObject()instanceof Ubicacion)){
                    return (((Ubicacion)o1.getUserObject()).getNombre().compareTo(((Ubicacion)o2.getUserObject()).getNombre()));
                }
                if ((o1.getUserObject() instanceof ContratoInquilino)&&(o2.getUserObject()instanceof ContratoInquilino)){
                    return (((ContratoInquilino)o1.getUserObject()).getDepartamento().getRm2id().compareTo(((ContratoInquilino)o2.getUserObject()).getDepartamento().getRm2id()));
                }
                if ((o1.getUserObject() instanceof Ubicacion)&&(o2.getUserObject()instanceof ContratoInquilino)){
                    return 1;
                }
                if ((o1.getUserObject() instanceof ContratoInquilino)&&(o2.getUserObject()instanceof Ubicacion)){
                    return -1;
                }
                return 1;
            }
        });

        return al;
    }


    public void onBtnRealizarClick() {

        List l = new ArrayList(treeContratos.getSelected());
        ArrayList<ContratoInquilino> ccii = new ArrayList();
        for (int i = 0; i < l.size() ; i++) {
            TreeItem ti = (TreeItem) l.get(i);
            if (ti.getUserObject() instanceof ContratoInquilino){
                ccii.add((ContratoInquilino) ti.getUserObject());
            }
        }
        if (ccii.size()==0){
            notifications.create().withCaption("Seleccionar algun contrato").show();
            return;
        }
        if (GuiUtils.isAnyValueNull(dteFechaCargo, dteFechaValor, dteFechaRealizacion, dtePeriodoAnteriorFechaDesde, dtePeriodoAnteriorFechaHasta, lkpSerie)){
            notifications.create().withCaption("Aportar valores : Fecha de Cargo, Fecha Valor, Fecha de Realizacion, Fecha Desde, Fecha Hasta y Serie").show();
            return;
        }
        try {
            byte[] bb = jasperReportService.reportDiferenciasEntreEmisiones(ccii, dteFechaRealizacion.getValue(), dteFechaValor.getValue(), dteFechaCargo.getValue(),
                    dtePeriodoAnteriorFechaDesde.getValue(), dtePeriodoAnteriorFechaHasta.getValue(), (Serie) lkpSerie.getValue());
            exportDisplay.show(new ByteArrayDataProvider(bb), "AnalisisDiferencias.pdf");
        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage());
        }

    }
}