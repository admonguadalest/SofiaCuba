package com.company.test1.web.screens.contratoinquilino;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.departamentos.UbicacionDeptoTreeItem;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.service.JasperReportService;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentValueLoader;
import com.haulmont.cuba.core.global.FluentValuesLoader;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.Departamento;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_DepartamentoVacio.browse")
@UiDescriptor("departamentos-vacios.xml")
@LookupComponent("departamentoesTable")
@LoadDataBeforeShow
public class DepartamentosVaciosBrowse extends StandardLookup<Departamento> {


    @Inject
    private LookupField tipoDepartamentoField;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private DataManager dataManager;

    @Inject
    private CollectionLoader<UbicacionDeptoTreeItem> deptosTreeItemsDl;
    @Inject
    private Table<Propietario> tablePropietarios;
    @Inject
    private Table<Departamento> departamentoesTable;
    @Inject
    private CollectionContainer<Departamento> departamentoesDc;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Notifications notifications;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe("treeItems")
    private void onTreeItemsSelection(Tree.SelectionEvent<UbicacionDeptoTreeItem> event) {
        List<Departamento> l = updateListaDeptosDesdeItems();
        departamentoesDc.getMutableItems().clear();
        departamentoesDc.getMutableItems().addAll(l);
        
    }
    @Inject
    private Tree<UbicacionDeptoTreeItem> treeItems;
    //treeitems
    ArrayList<UbicacionDeptoTreeItem> items = new ArrayList<UbicacionDeptoTreeItem>();

    @Subscribe("tablePropietarios")
    private void onTablePropietariosSelection(Table.SelectionEvent<Propietario> event) {
        Set<Propietario> propietarios = event.getSelected();
        deptosTreeItemsDl.load();
    }

    
    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        tipoDepartamentoField.setOptionsList(Arrays.asList("TODOS", "VIVIENDAS", "LOCALES"));
    }


    @Install(to = "deptosTreeItemsDl", target = Target.DATA_LOADER)
    private List<UbicacionDeptoTreeItem> deptosTreeItemsDlLoadDelegate(LoadContext<UbicacionDeptoTreeItem> loadContext) {
        Propietario[] props  = tablePropietarios.getSelected().toArray(new Propietario[0]);
        items.clear();

        List<Departamento> departamentos = new ArrayList<Departamento>();
        for (int i = 0; i < props.length; i++) {
            Propietario prop = props[i];
            String hql = "select d FROM test1_Departamento d JOIN d.ubicacion u LEFT JOIN d.propietario pd LEFT JOIN u.propietario pu WHERE (pd.id = :pdid or pu.id = :puid)";
            List<Departamento> l = dataManager.loadValue(hql, Departamento.class).parameter("pdid", prop.getId()).parameter("puid", prop.getId()).list();
            for (int j = 0; j < l.size(); j++) {
                Departamento departamento =  l.get(j);
                departamento = dataManager.reload(departamento, "departamento-view-for-tree");

                if (departamento.getPropietarioEfectivo()==null){
                    int y = 2;
                    continue;
                }

                if (departamento.getPropietarioEfectivo().getId().compareTo(prop.getId())==0){
                    departamentos.add(departamento);
                }


            }
        }


        Map<Ubicacion, UbicacionDeptoTreeItem> mapUbicaciones = new HashMap<Ubicacion, UbicacionDeptoTreeItem>();

        for (int i = 0; i < departamentos.size(); i++) {
            Departamento d = departamentos.get(i);
            UbicacionDeptoTreeItem udti = new UbicacionDeptoTreeItem();
            udti.setDepartamento(d);
            udti.setUbicacion(d.getUbicacion());

            items.add(udti);
            if (!mapUbicaciones.containsKey(d.getUbicacion())){

                UbicacionDeptoTreeItem udti_ = new UbicacionDeptoTreeItem();
                udti_.setParentItem(null);
                udti_.setUbicacion(d.getUbicacion());
                items.add(udti_);
                mapUbicaciones.put(d.getUbicacion(), udti_);

            }
            udti.setParentItem(mapUbicaciones.get(d.getUbicacion()));

        }

        Collections.sort(items, new Comparator<UbicacionDeptoTreeItem>() {
            @Override
            public int compare(UbicacionDeptoTreeItem o1, UbicacionDeptoTreeItem o2) {

                if ((o1.getParentItem()==null)&&(o2.getParentItem()==null)){
                    return o1.getUbicacion().getNombre().compareTo(o2.getUbicacion().getNombre());
                }
                if ((o1.getParentItem()!=null)&&(o2.getParentItem()!=null)){
                    if ((o1.getDepartamento().getRm2id()==null)||(o2.getDepartamento().getRm2id()==null)){
                        return 1;
                    }else
                        return o1.getDepartamento().getRm2id().compareTo(o2.getDepartamento().getRm2id());
                }
                if ((o1.getParentItem()!=null)&&(o2.getParentItem()==null)){
                    return -1;
                }
                if ((o1.getParentItem()==null)&&(o2.getParentItem()!=null)){
                    return 1;
                }
                return 1;
            }
        });

        List<Departamento> l = updateListaDeptosDesdeItems();
        departamentoesDc.getMutableItems().clear();
        departamentoesDc.getMutableItems().addAll(l);

        return items;
    }

    @Subscribe("tipoDepartamentoField")
    private void onTipoDepartamentoFieldValueChange(HasValue.ValueChangeEvent event) {

        List<Departamento> deptos = updateListaDeptosDesdeItems();



        departamentoesDc.getMutableItems().clear();
        departamentoesDc.getMutableItems().addAll(deptos);
        
    }




    

    @Install(to = "departamentoesDl", target = Target.DATA_LOADER)
    private List<Departamento> departamentoesDlLoadDelegate(LoadContext<Departamento> loadContext) {
        return updateListaDeptosDesdeItems();

    }

    private List<Departamento> updateListaDeptosDesdeItems(){
        UbicacionDeptoTreeItem[] itemsarr = treeItems.getSelected().toArray(new UbicacionDeptoTreeItem[0]);
        List<UbicacionDeptoTreeItem> listItems = Arrays.asList(itemsarr);
        ArrayList<Departamento> l = new ArrayList<Departamento>();
        for (int i = 0; i < items.size(); i++) {
            UbicacionDeptoTreeItem udti =  items.get(i);
            if (udti.getParentItem()!=null){

                if(listItems.size()>0){
                    if (listItems.indexOf(udti)==-1){
                        continue;
                    }
                }

                Departamento d = udti.getDepartamento();
                if (tipoDepartamentoField.getValue()==null){
                    l.add(d);
                    continue;
                }
                //pendiente elimiinar este if
                if (d.getViviendaLocal()==null){
                    l.add(d);
                    continue;
                }
                if (((String)tipoDepartamentoField.getValue()).compareTo("TODOS")==0){
                    l.add(d);
                }
                if (((String)tipoDepartamentoField.getValue()).compareTo("VIVIENDAS")==0){
                    if (d.getViviendaLocal()){
                        l.add(d);
                    }
                }
                if (((String)tipoDepartamentoField.getValue()).compareTo("LOCALES")==0){
                    if (!d.getViviendaLocal()){
                        l.add(d);
                    }
                }
            }
        }
        //comprobando si los departamentos seleccionados tienen contrato vigente
        ArrayList<Departamento> al = new ArrayList<Departamento>();
        for (int i = 0; i < l.size(); i++) {
            Departamento departamento =  l.get(i);
            String hql = "select ci FROM test1_ContratoInquilino ci JOIN ci.departamento d WHERE d.id = :did and ci.estadoContrato = 3";
            List<ContratoInquilino> ccii = dataManager.loadValue(hql, ContratoInquilino.class).parameter("did", departamento.getId()).list();
//            boolean incluirDepto = true;
//            for (int j = 0; j < ccii.size(); j++) {
//                ContratoInquilino ci =  ccii.get(j);
//                if (ci.getEstadoContrato()== EstadoContratoInquilinoEnum.VIGENTE){
//                    incluirDepto = false;
//                }
//            }
//            if (incluirDepto){
//                al.add(departamento);
//            }
            if(ccii.size()==0){
                if (!departamento.getExcluirDeMonitorizacionParaBusquedaDePisosVacios()){
                    al.add(departamento);
                }

            }
        }
        return al;
    }


    public void onBtnSeleccionarTodosClick() {
        treeItems.setSelected(items);
    }

    public void onBtnLimpiarSeleccionClick() {
        treeItems.setSelected(new ArrayList<UbicacionDeptoTreeItem>());
    }

    public void onBtnImprimirClick() {
        try {
            byte[] bb = jasperReportService.departamentosVacios(new ArrayList<Departamento>(departamentoesTable.getItems().getItems()));
            exportDisplay.show(new ByteArrayDataProvider(bb), "Departamentos Vacios.pdf", ExportFormat.getByExtension("pdf"));
        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription("No se pudo imprimir el report").show();
            e.printStackTrace();
        }
    }
}