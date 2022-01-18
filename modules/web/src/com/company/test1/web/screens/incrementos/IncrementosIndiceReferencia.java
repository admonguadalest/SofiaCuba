package com.company.test1.web.screens.incrementos;

import com.company.test1.entity.TreeItem;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoAdicionalConceptoRecibo;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.ContratosService;
import com.company.test1.service.IncrementosService;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.GuiUtils;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.data.TreeItems;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import java.sql.Date;
import java.util.*;
import java.util.stream.Stream;

@UiController("test1_IncrementosIndiceReferencia")
@UiDescriptor("incrementos-indice-referencia.xml")
public class IncrementosIndiceReferencia extends Screen {
    @Inject
    private CollectionLoader<Concepto> conceptoesDl;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private Tree treeUbicacionesDepartamentos;
    @Inject
    private DataManager dataManager;
    @Inject
    private LookupField<Integer> lkpMes;
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private LookupField<Integer> lkpAnno;
    @Inject
    private IncrementosService incrementosService;
    @Inject
    private LookupField<Concepto> lkpConcepto;
    @Inject
    private LookupField<Concepto> lkpConceptoAtrasos;
    @Inject
    private DateField<Date> dteFechaAplicacion;
    @Inject
    private Notifications notifications;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private CollectionContainer<TreeItem> treeItemsDc;


    @Subscribe("tablePropietarios")
    private void onTablePropietariosSelection(Table.SelectionEvent<Propietario> event) {
        treeItemsDl.load();
    }
    @Inject
    private Table<Propietario> tablePropietarios;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        propietariosDl.load();
        conceptoesDl.load();

        List<Integer> years = new ArrayList<Integer>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years.add(year-i);
        }
        lkpAnno.setOptionsList(years);

        lkpMes.setOptionsList(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12));
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
        if (!event.isUserOriginated()) return;
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

        treeUbicacionesDepartamentos.setSelected(selection);

    }

    private boolean datosValidos(){
        boolean valido = ((lkpMes.getValue()!=null)&&(lkpAnno.getValue()!=null)&&(dteFechaAplicacion.getValue()!=null));
        if (!valido){
            notifications.create().withCaption("Entrada Incompleta de Datos").withDescription("Proveer mes, año y fecha de aplicación").show();
            return false;
        }
        valido = ((lkpConcepto.getValue()!=null)&&(lkpConceptoAtrasos.getValue()!=null));
        if (!valido){
            notifications.create().withCaption("Entrada Incompleta de Datos").withDescription("Especificar concepto de aplicación de incrementos").show();
            return false;
        }
        return true;
    }

    
    
    
    
    
    public void onBtnProcesarIncrementosRecibosClick() {
        if (!datosValidos()){
            return;
        }
        Hashtable<ContratoInquilino, String> ajusteMesAnno = new Hashtable<>();
        List<Departamento> deptos = this.devuelveDepartamentosDeTreeUbicacionesDepartamentos();
        ArrayList al = new ArrayList();
        try{

            for (int i = 0; i < deptos.size(); i++) {
                /*Departamento departamento = deptos.get(i);
                ContratoInquilino contratoInquilino = AppBeans.get(ContratosService.class).devuelveContratoVigenteParaDepartamento(departamento, View.BASE);
                if (contratoInquilino == null) continue;

                String mesAnnoIpc = contratoInquilino.getMesAnyoAplicacionIPC();
                String mes = mesAnnoIpc.substring(0,2);
                Integer mes_ = Integer.valueOf(mes);
                if (!(lkpMes.getValue().intValue()==mes_.intValue())){
                    continue;
                }*/
                Departamento departamento = deptos.get(i);
                ContratoInquilino contratoInquilino = AppBeans.get(ContratosService.class).devuelveContratoVigenteParaDepartamento(departamento, View.BASE);
                if (contratoInquilino == null) continue;

                String mesAnnoIpc = contratoInquilino.getMesAnyoAplicacionIPC();
                String mes = mesAnnoIpc.substring(0,2);
                Integer mes_ = null;
                try {
                    mes_ = Integer.valueOf(mes);
                }catch(Exception exc){
                    notifications.create().withCaption("Error").withDescription("No se pudieron cargar los conceptios recibos para incrementos índice de referencia:  " + departamento.getNombreDescriptivoCompleto() + " " + exc.getMessage()).show();
                    return;
                }
                if (!(lkpMes.getValue().intValue()==mes_.intValue())){
                    continue;
                }
                //comprobacion que el año es el mismo
                if (!(lkpAnno.getValue().intValue()==Integer.valueOf(mesAnnoIpc.substring(2)).intValue())){
                    continue;
                }
                String anno = mesAnnoIpc.substring(2);
                String newMesAnno = mes.toString() + ((Integer)(Integer.valueOf(anno)+1)).toString();
                if (newMesAnno.length()==5){
                    newMesAnno = "0"+newMesAnno;
                }

                ajusteMesAnno.put(contratoInquilino, newMesAnno);

                ConceptoRecibo[] arr_ccrr = incrementosService.creaConceptosReciboParaIncrementosIndiceReferencia(
                        lkpConcepto.getValue(),
                        contratoInquilino,
                        lkpMes.getValue(),
                        lkpAnno.getValue(),
                        dteFechaAplicacion.getValue(),
                        lkpConceptoAtrasos.getValue(),
                        0
                );
                al.add(arr_ccrr);
            }

        }catch(Exception ex){
            notifications.create().withCaption("Error").withDescription("No se pudieron cargar los conceptios recibos para incrementos índice de referencia").show();
            return;
        }

        if(al.isEmpty()){
            notifications.create().withCaption("Error").withDescription("La seleccion no devolvio ningun resultado").show();
            return;
        }

        ArrayList entitiesToPersist = new ArrayList();
        for (int i = 0; i < al.size(); i++) {
            ConceptoRecibo[] ccrr = (ConceptoRecibo[]) al.get(i);
            //primero
            ConceptoRecibo cr0 = ccrr[0];
            entitiesToPersist.add(cr0);
            if (cr0.getIncremento()!=null){
                entitiesToPersist.add(cr0.getIncremento());
            }
            for (int j = 0; j < cr0.getConceptosAdicionalesConceptoRecibo().size(); j++) {
                ConceptoAdicionalConceptoRecibo cacr = cr0.getConceptosAdicionalesConceptoRecibo().get(j);
                entitiesToPersist.add(cacr);
            }
            //segundo
            if (ccrr.length==2){
                ConceptoRecibo cr1 = ccrr[1];
                entitiesToPersist.add(cr1);
                if (cr0.getIncremento()!=null){
                    entitiesToPersist.add(cr1.getIncremento());
                }
                for (int j = 0; j < cr1.getConceptosAdicionalesConceptoRecibo().size(); j++) {
                    ConceptoAdicionalConceptoRecibo cacr = cr1.getConceptosAdicionalesConceptoRecibo().get(j);
                    entitiesToPersist.add(cacr);
                }
            }

        }
        //finalmente guardo
        dataManager.commit(new CommitContext(entitiesToPersist));
        notifications.create().withCaption("Conceptos generados y registrados exitósamente").show();

        //realizando el ajuste de mesAnnoAplicacionIPC
        Iterator<ContratoInquilino> ici = ajusteMesAnno.keySet().iterator();
        while(ici.hasNext()){
            ContratoInquilino ci = ici.next();
            ci.setMesAnyoAplicacionIPC(ajusteMesAnno.get(ci));
            dataManager.commit(ci);
        }

    }

    public void onBtnPrevisualizarContratosAfectadosClick() {
        if(!datosValidos()){
            return;
        }
        Departamento d_ = null;
        List<Departamento> deptos = this.devuelveDepartamentosDeTreeUbicacionesDepartamentos();
        ArrayList al = new ArrayList();
        try{

            for (int i = 0; i < deptos.size(); i++) {
                Departamento departamento = deptos.get(i);
                d_ = departamento;
                ContratoInquilino contratoInquilino = AppBeans.get(ContratosService.class).devuelveContratoVigenteParaDepartamento(departamento, View.BASE);
                if (contratoInquilino == null) continue;

                String mesAnnoIpc = contratoInquilino.getMesAnyoAplicacionIPC();
                String mes = mesAnnoIpc.substring(0,2);
                Integer mes_ = null;
                try {
                    mes_ = Integer.valueOf(mes);
                }catch(Exception exc){
                    notifications.create().withCaption("Error").withDescription("No se pudieron cargar los conceptios recibos para incrementos índice de referencia:  " + departamento.getNombreDescriptivoCompleto() + " " + exc.getMessage()).show();
                    return;
                }
                if (!(lkpMes.getValue().intValue()==mes_.intValue())){
                    continue;
                }
                //comprobacion que el año es el mismo
                if (!(lkpAnno.getValue().intValue()==Integer.valueOf(mesAnnoIpc.substring(2)).intValue())){
                    continue;
                }

                try{
                    contratoInquilino = dataManager.reload(contratoInquilino, "contratoInquilino-view");
                    ConceptoRecibo[] arr_ccrr = incrementosService.creaConceptosReciboParaIncrementosIndiceReferencia(
                            lkpConcepto.getValue(),
                            contratoInquilino,
                            lkpMes.getValue(),
                            lkpAnno.getValue(),
                            dteFechaAplicacion.getValue(),
                            lkpConceptoAtrasos.getValue(),
                            0
                    );
                    al.add(arr_ccrr);
                }catch(Exception exc){
                   notifications.create().withCaption("Error").withDescription("Error al cargar información Incr.Ind.Referencia para " + contratoInquilino.getNumeroContrato() + ":  " + exc.getMessage()).show();
                    return;
                }

            }

        }catch(Exception ex){
            notifications.create().withCaption("Error").withDescription("No se pudieron cargar los conceptios recibos para incrementos índice de referencia:  " + ex.getMessage()).show();
            return;
        }

        if(al.isEmpty()){
            notifications.create().withCaption("Error").withDescription("La seleccion no devolvio ningun resultado").show();
            return;
        }

        /**
         * Para el report solo incluyo el primer concepto
         */
        ArrayList al_ = new ArrayList();
        for (int i = 0; i < al.size(); i++) {
            ConceptoRecibo[] arr_cr = (ConceptoRecibo[]) al.get(i);
            al_.add(arr_cr[0]);
        }


        byte[] bb = jasperReportService.reportIncrementosIndiceReferencia(al_, 0, lkpMes.getValue(), lkpAnno.getValue(), dteFechaAplicacion.getValue());

        exportDisplay.show(new ByteArrayDataProvider(bb), "Incrementos.pdf", ExportFormat.getByExtension("pdf"));

    }

    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnSeleccionarTodosUbicacionesDepartamentosClick() {
        GuiUtils.selectAll(treeUbicacionesDepartamentos);
    }

    public void onBtnSelTodosPropsClick() {
        GuiUtils.selectAll(tablePropietarios);
    }

    public void onBtnInvertirPropsClick() {
        GuiUtils.invertSelection(tablePropietarios);
    }

    public void onBtnInvertirUbicacionesDepartamentosClick() {
        GuiUtils.invertSelection(treeUbicacionesDepartamentos);
    }

    private List<Departamento> devuelveDepartamentosDeTreeUbicacionesDepartamentos(){
        List<Departamento> dd = new ArrayList<Departamento>();
        Set s = treeUbicacionesDepartamentos.getSelected();
        List l = new ArrayList(s);
//        TreeItems<TreeItem> c = treeUbicacionesDepartamentos.getItems();
//        List<TreeItem> ttii = new ArrayList<TreeItem>();
//        Stream<TreeItem> stream = c.getItems();
//        Object[] oo =  stream.toArray();
//        List l = Arrays.asList(oo);
        for (int i = 0; i < l.size(); i++) {
            TreeItem o =  (TreeItem) l.get(i);

            if (o.getUserObject() instanceof Departamento){
                dd.add((Departamento) o.getUserObject());
            }

        }
        return dd;
    }
}