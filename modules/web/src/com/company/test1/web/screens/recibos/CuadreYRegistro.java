package com.company.test1.web.screens.recibos;

import com.company.test1.entity.TreeItem;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.recibos.*;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentValueLoader;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_CuadreYRegistro")
@UiDescriptor("cuadre-y-registro.xml")
public class CuadreYRegistro extends Screen {
    @Inject
    private CollectionLoader<DefinicionRemesa> definicionRemesasDl;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private Table<Propietario> tablePropietarios;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionContainer<Concepto> conceptoesDc;
    @Inject
    private CollectionLoader<Concepto> conceptoesDl;
    @Inject
    private CollectionLoader<Serie> seriesDl;
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private Tree<TreeItem> treeContratos;
    @Inject
    private CollectionContainer<TreeItem> treeItemsDc;
    @Inject
    private RecibosService recibosService;
    @Inject
    private DateField<java.sql.Date> fechaCargo;
    @Inject
    private DateField<java.sql.Date> fechaRealizacion;
    @Inject
    private DateField<java.sql.Date> fechaValor;
    @Inject
    private LookupField<Serie> lkpSerie;
    @Inject
    private TokenList<DefinicionRemesa> tknDefsRemesa;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;



    @Subscribe("fechaValor")
    private void onFechaValorValueChange(HasValue.ValueChangeEvent event) {
        Object o = event.getValue();
        java.sql.Date d = new java.sql.Date(((Date)o).getTime());
        fechaRealizacion.setValue(d);
        fechaCargo.setValue(d);
    }

    
    
    
    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        definicionRemesasDl.load();
        conceptoesDl.load();
        seriesDl.load();
    }

    @Subscribe("tablePropietarios")
    private void onTablePropietariosSelection(Table.SelectionEvent<Propietario> event) {
        treeItemsDl.load();
    }
    
    

    @Install(to = "treeItemsDl", target = Target.DATA_LOADER)
    private List<TreeItem> treeItemsDlLoadDelegate(LoadContext<TreeItem> loadContext) {
        List<Propietario> props = Arrays.asList(tablePropietarios.getSelected().toArray(new Propietario[0]));

        if (props.size()==0){
            return new ArrayList();

        }

        ArrayList<ContratoInquilino> ccii = new ArrayList<ContratoInquilino>();
        for (int i = 0; i < props.size(); i++) {
            Propietario prop =  props.get(i);
            List<ContratoInquilino> ccii1 = dataManager.loadValue("select ci from test1_ContratoInquilino ci JOIN ci.departamento d JOIN d.ubicacion u where u.propietario.id = '" + prop.getId() + "' and ci.estadoContrato = 3", ContratoInquilino.class).list();
            ccii.addAll(ccii1);
            ccii1 = dataManager.loadValue("select ci from test1_ContratoInquilino ci JOIN ci.departamento d where d.propietario.id = '" + prop.getId() + "' and ci.estadoContrato = 3", ContratoInquilino.class).list();
            ccii.addAll(ccii1);
        }

        //cargando datos
        List<DefinicionRemesa> ddrr = new ArrayList(tknDefsRemesa.getValue());
        ArrayList<ContratoInquilino> al = new ArrayList<ContratoInquilino>();
        for (int i = 0; i < ccii.size(); i++) {
            ContratoInquilino ci =  ccii.get(i);
            ci = dataManager.reload(ci, "contratoInquilino-cuadre-y-registro");
            //mirando si hay contrato vigente y si lo hay si su programacion recibo tiene una definicion remesa acorde

            if (ddrr.indexOf(ci.getProgramacionRecibo().getDefinicionRemesa())!=-1){
                al.add(ci);
            }else{
                //no se incluye
                int y = 2;
            }

        }

        ccii = al;

        ArrayList<TreeItem> items = new ArrayList<TreeItem>();

        HashMap<Ubicacion, TreeItem> m = new HashMap<Ubicacion, TreeItem>();
        for (int i = 0; i < ccii.size(); i++) {
            ContratoInquilino ci =  ccii.get(i);
            TreeItem tid = new TreeItem();
            tid.setUserObject(ci);
            if (!m.containsKey(ci.getDepartamento().getUbicacion())){
                TreeItem tiu = new TreeItem();
                tiu.setUserObject(ci.getDepartamento().getUbicacion());

                items.add(tiu);
                m.put(ci.getDepartamento().getUbicacion(), tiu);
            }
            tid.setParentItem(m.get(ci.getDepartamento().getUbicacion()));
            items.add(tid);
        }

        Collections.sort(items, new Comparator<TreeItem>() {
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

        return items;



    }

    @Subscribe("treeContratos")
    private void onTreeContratosSelection(Tree.SelectionEvent<TreeItem> event) {
        if (!event.isUserOriginated()) return;
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
            }else if(ti.getUserObject() instanceof ContratoInquilino){
                selection.add(ti);
            }
        }

        treeContratos.setSelected(selection);

    }

    
    
    
    private String getUserObjectTextString(Object userObject){
        if (userObject instanceof Departamento){
            Departamento d = (Departamento) userObject;
            return d.getPiso() + " " + d.getPuerta();
        }
        if (userObject instanceof Ubicacion){
            Ubicacion u = (Ubicacion) userObject;
            return u.getNombre();
        }
        return "";
    }

    @Subscribe("tknDefsRemesa")
    private void onTknDefsRemesaValueChange(HasValue.ValueChangeEvent<Collection<DefinicionRemesa>> event) {
        propietariosDl.load();
        treeItemsDl.load();
    }

    
    

    @Install(to = "propietariosDl", target = Target.DATA_LOADER)
    private List<Propietario> propietariosDlLoadDelegate(LoadContext<Propietario> loadContext) {
        List<DefinicionRemesa> ddrr = new ArrayList(tknDefsRemesa.getValue());
        ArrayList<Propietario> props = new ArrayList<Propietario>();
        for (int i = 0; i < ddrr.size(); i++) {
            DefinicionRemesa dr =  ddrr.get(i);
            List<Propietario> props_ = dataManager.loadValue("select p from test1_Propietario p JOIN p.definicionesRemesa dr WHERE dr.id = '" + dr.getId() + "'", Propietario.class).list();
            for (int j = 0; j < props_.size(); j++) {
                Propietario propietario =  props_.get(j);
                if (props.indexOf(propietario)==-1){
                    props.add(propietario);
                }

            }
        }

        return props;
    }

    public void onBtnLimpiarSeleccionPropietariosClick() {
        tablePropietarios.setSelected(new ArrayList<Propietario>());
    }

    public void onBtnInvertirSeleccionPropietariosClick() {

        notifications.create().withCaption("Desarrollo Pendiente").show();
    }

    public void onBtnLimpiarSeleccionTreeClick() {
        treeContratos.setSelected(new ArrayList());
    }

    public void onBtnSeleccionarTodosContratosClick() {
        treeContratos.setSelected(treeItemsDc.getItems());
    }

    public void onBtnRealizarClick() {

        try {

            List<Remesa> rr = generaRemesas(false);

            boolean success = recibosService.persisteRemesas(rr);

            notifications.create().withCaption("Las remesas se generaron exitósamente").show();

        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription("No se pudo realizar la remesa").show();
        }



    }

    private List<Remesa> generaRemesas(boolean previsualizacion) throws Exception{

        ArrayList<Remesa> remesas = new ArrayList<Remesa>();

        Set<TreeItem> s = treeContratos.getSelected();
        Iterator<TreeItem> i = s.iterator();

        HashMap<DefinicionRemesa, List<ContratoInquilino>> ddrrContratos = new HashMap<DefinicionRemesa, List<ContratoInquilino>>();
        while(i.hasNext()){
            TreeItem ti = i.next();
            if (ti.getUserObject() instanceof ContratoInquilino){
                ContratoInquilino ci = (ContratoInquilino) ti.getUserObject();
                DefinicionRemesa dr = ci.getProgramacionRecibo().getDefinicionRemesa();
                List<ContratoInquilino> lci = ddrrContratos.get(dr);
                if (lci==null){
                    lci = new ArrayList<ContratoInquilino>();
                    ddrrContratos.put(dr, lci);
                }
                lci.add(ci);

            }
        }




        Date fr = fechaRealizacion.getValue();
        Date fv = fechaValor.getValue();
        Date fc = fechaCargo.getValue();
        Serie serie= lkpSerie.getValue();

        String mensaje = "Proveer valores para Fecha de Realizacion, Fecha Valor, Fecha de Cargo, Serie y Contratos asociados a la nueva remesa";

        if ((fr==null)||(fv==null)||(fc==null)||(serie==null)||(s.size()==0)){
            notifications.create().withCaption("Datos Incompletos").withDescription(mensaje).show();
            return null;
        }



        try {
            Iterator<DefinicionRemesa> idr = ddrrContratos.keySet().iterator();
            if ((!previsualizacion)&&(ddrrContratos.size()!=1)){
                notifications.create().withCaption("Solo puede realizar remesas asociadas a una única Definición de Remesa. Elija 'Previsualizar' o una única Definición de Remesa").show();
                return null;
            }
            while(idr.hasNext()){
                DefinicionRemesa dr = idr.next();
                List<ContratoInquilino> ccii = ddrrContratos.get(dr);
                Remesa r = recibosService.generaRemesaAcordeADatos(fr, fv, fc, ccii, serie);
                remesas.add(r);
            }
            return remesas;

        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();
            return null;
        }

    }

    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnPrevisualizarClick() {
        try {
            List<Remesa> remesas = generaRemesas(true);
            byte[] bb = jasperReportService.listadoResumenRecibos(remesas);
            exportDisplay.show(new ByteArrayDataProvider(bb), "Remesas.pdf", ExportFormat.getByExtension("pdf"));
        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription("No se pudieron generar las remesas para los datos seleccionados").show();
            e.printStackTrace();
        }

    }
}