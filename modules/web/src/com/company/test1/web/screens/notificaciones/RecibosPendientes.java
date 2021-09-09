package com.company.test1.web.screens.notificaciones;

import com.company.test1.entity.Direccion;
import com.company.test1.entity.TreeItem;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.NombreTipoDireccion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.nonpersistententities.HelperInyeccionPlantilla;
import com.company.test1.entity.notificaciones.HelperNotificacionesAumentos;
import com.company.test1.entity.notificaciones.HelperNotificacionesAumentosIPC;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.service.ContratosService;
import com.company.test1.service.NotificacionService;
import com.company.test1.service.PdfService;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.*;

@UiController("test1_RecibosPendientes")
@UiDescriptor("recibos-pendientes.xml")
public class RecibosPendientes extends Screen {


    @Inject
    private Table<Propietario> tblPropietarios;
    @Inject
    private CollectionContainer<TreeItem> treeItemsDc;
    @Inject
    private CollectionLoader<TreeItem> treeItemsDl;
    @Inject
    private CollectionLoader<Propietario> propietariosDl;
    @Inject
    private CollectionContainer<Propietario> propietariosDc;
    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;
    @Inject
    private Table tblValoresPlantilla;
    @Inject
    private CollectionContainer<HelperInyeccionPlantilla> helperInyeccionPlantillasDc;


    @Inject
    private CollectionLoader<Plantilla> plantillasDl;
    @Inject
    private LookupField<Plantilla> lkpPlantilla;

    @Inject
    private Tree<TreeItem> treeDepartamentos;
    @Inject
    private ContratosService contratosService;
    @Inject
    private NotificacionService notificacionService;
    @Inject
    private PdfService pdfService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private ComponentsFactory componentsFactory;


    @Inject
    private CheckBox chkVerCamposVacios;
    @Inject
    private RecibosService recibosService;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {

        propietariosDl.load();
        plantillasDl.load();
    }

    @Subscribe("tblPropietarios")
    private void onTblPropietariosSelection(Table.SelectionEvent<Propietario> event) {
        treeItemsDl.load();
    }

    @Subscribe("treeDepartamentos")
    private void onTreeDepartamentosSelection(Tree.SelectionEvent<TreeItem> event) {
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
            if (ti.getUserObject() instanceof Departamento){

                    selection.add(ti);

            }

        }

        treeDepartamentos.setSelected(selection);

    }



    @Install(to = "treeItemsDl", target = Target.DATA_LOADER)
    private List<TreeItem> treeItemsDlLoadDelegate(LoadContext<TreeItem> loadContext) {



        List<TreeItem> ttii = new ArrayList<TreeItem>();
        List<Propietario> pp = new ArrayList(tblPropietarios.getSelected());
        if (pp.size()==0){
            notifications.create().withCaption("Seleccionar un Propietario");
        }
        List<UUID> idsprops = new ArrayList<UUID>();
        for (int i = 0; i < pp.size() ; i++) {
            idsprops.add(pp.get(i).getId());
        }

        String hql = "select d from test1_Departamento d JOIN d.ubicacion u " +
                " LEFT JOIN d.propietario p1 LEFT JOIN u.propietario p2 " +
                " WHERE p1.id in :idsprops OR p2.id in :idsprops";

        List<Departamento> l = dataManager.loadValue(hql, Departamento.class).parameter("idsprops", idsprops).list();
        for (int i = 0; i < l.size(); i++) {
            l.set(i,dataManager.reload(l.get(i), "departamento-view"));
        }
        HashMap<Ubicacion, TreeItem> ht = new HashMap<Ubicacion,TreeItem>();
        for (int i = 0; i < l.size(); i++) {
            Departamento d = l.get(i);
            TreeItem tiu = null;
            if (ht.containsKey(d.getUbicacion())){
                tiu = ht.get(d.getUbicacion());
            }else{
                TreeItem t = new TreeItem();
                t.setUserObject(d.getUbicacion());
                tiu = t;
                ht.put(d.getUbicacion(), tiu);
                ttii.add(tiu);
            }
            TreeItem tid = new TreeItem();
            tid.setUserObject(d);
            tid.setParentItem(tiu);
            ttii.add(tid);
        }
        return ttii;

    }

    @Subscribe("lkpPlantilla")
    private void onLkpPlantillaValueChange(HasValue.ValueChangeEvent<Plantilla> event) {
        Plantilla p = event.getValue();
        if (p==null) return;
        List l = Arrays.asList(p.getListaParametrosPlantillaLibres().split(";"));
        List helpers = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            String s = (String) l.get(i);
            HelperInyeccionPlantilla hi = new HelperInyeccionPlantilla();
            hi.setTitulo(s);
            helpers.add(hi);
        }
        helperInyeccionPlantillasDc.setItems(helpers);
    }


    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnPrevisualizarClick() {
        boolean verCamposVacios = chkVerCamposVacios.getValue();
        if (!isDataValid()){
            notifications.create().withCaption("Datos incompletos").show();
            return;
        }
        if (lkpPlantilla.getValue()==null){
            notifications.create().withCaption("Seleccionar una plantilla").show();
            return;
        }
        Plantilla plantilla = lkpPlantilla.getValue();

        try {
            List<TreeItem> ttii = new ArrayList(treeDepartamentos.getSelected());
            ArrayList<ContratoInquilino> ccii = new ArrayList();
            for (int i = 0; i < ttii.size(); i++) {
                if (ttii.get(i).getUserObject() instanceof Departamento) {
                    ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento((Departamento) ttii.get(i).getUserObject());
                    if (ci != null) {
                        ccii.add(ci);
                    }
                }
            }
            if (ccii.isEmpty()) {
                notifications.create().withCaption("Ninguno de los Departamentos seleccionados tiene contratos vigentes asociados").show();
                return;
            }

            Hashtable hti = new Hashtable();
            List<HelperInyeccionPlantilla> hhii = new ArrayList(tblValoresPlantilla.getItems().getItems());
            for (int i = 0; i < hhii.size(); i++) {
                HelperInyeccionPlantilla helperInyeccion = hhii.get(i);
                String valor = "";
                if (helperInyeccion.getValor()!=null){
                    valor = helperInyeccion.getValor();
                }
                hti.put(helperInyeccion.getTitulo(), valor);
            }


            //ordenacion de los contratos

            Collections.sort(ccii, new ContratoInquilinoIdDepartamento());
            List<ContratoInquilino> contratos = ccii;

            List<byte[]> inputStreams = new ArrayList<byte[]>();

            for (int i = 0; i < contratos.size(); i++) {
                ContratoInquilino c = contratos.get(i);

                try {

                    NotificacionContratoInquilino nc = new NotificacionContratoInquilino();
                    nc.setContratoInquilino(c);
                    nc.setTitulo(plantilla.getNombrePlantilla());
                    nc.setPlantilla(plantilla);

                    Hashtable ht_objetos = new Hashtable();
                    ht_objetos.clear();
                    ht_objetos.put("admin", c.getDepartamento().getPropietarioEfectivo().getPersona());
                    ht_objetos.put("prop", c.getDepartamento().getPropietarioEfectivo());
//                ht_objetos.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().getDireccionDesdeNombre(Direccion.NOMBRE_DIRECCION_PROPIETARIO_CONTRATO_N19));
                    ht_objetos.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_CONTRACTUAL.getId()));
                    ht_objetos.put("dirinqui", c.getInquilino().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_INQUILINO.getId()));//pendiente

                    ht_objetos.put("inqui", c.getInquilino());
                    ht_objetos.put("contr", c);

//                nc.setObjetos(ht_objetos);
                    nc.setFechaProgramadaEnvio(new Date());
                    //anexando los inyecciones plantilla en el hashtable de objetos para hacerlos accesibles
                    Iterator iterhip = hti.keySet().iterator();
                    while(iterhip.hasNext()){
                        String k = (String) iterhip.next();
                        ht_objetos.put(k, hti.get(k));
                    }

                    nc = (NotificacionContratoInquilino) notificacionService.implementaContenido(nc, ht_objetos, verCamposVacios);
                    nc = (NotificacionContratoInquilino) notificacionService.implementaVersionPdfVersionFlexReport(nc);

                    byte[] bb = nc.getVersionPdf();

                    inputStreams.add(bb);
                } catch (Exception ex) {
                    notifications.create().withCaption(ex.getMessage()).show();
                    return;
                }
            }

            byte[] outputbb = pdfService.concatPdfs(inputStreams, false);
            exportDisplay.show(new ByteArrayDataProvider(outputbb), "Notificaciones.pdf");
        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage()).show();
        }
    }

    public void onBtnRealizarClick() {
        ArrayList notificaciones = new ArrayList();
        boolean verCamposVacios = chkVerCamposVacios.getValue();
        if (!isDataValid()){
            notifications.create().withCaption("Datos incompletos").show();
            return;
        }
        if (lkpPlantilla.getValue()==null){
            notifications.create().withCaption("Seleccionar una plantilla").show();
            return;
        }
        Plantilla plantilla = lkpPlantilla.getValue();

        try {
            List<TreeItem> ttii = new ArrayList(treeDepartamentos.getSelected());
            ArrayList<ContratoInquilino> ccii = new ArrayList();
            for (int i = 0; i < ttii.size(); i++) {
                if (ttii.get(i).getUserObject() instanceof Departamento) {
                    ContratoInquilino ci = contratosService.devuelveContratoVigenteParaDepartamento((Departamento) ttii.get(i).getUserObject());
                    if (ci != null) {
                        ccii.add(ci);
                    }
                }
            }
            if (ccii.isEmpty()) {
                notifications.create().withCaption("Ninguno de los Departamentos seleccionados tiene contratos vigentes asociados").show();
                return;
            }

            Hashtable hti = new Hashtable();
            List<HelperInyeccionPlantilla> hhii = new ArrayList(tblValoresPlantilla.getItems().getItems());
            for (int i = 0; i < hhii.size(); i++) {
                HelperInyeccionPlantilla helperInyeccion = hhii.get(i);
                String valor = "";
                if (helperInyeccion.getValor()!=null){
                    valor = helperInyeccion.getValor();
                }
                hti.put(helperInyeccion.getTitulo(), valor);
            }


            //ordenacion de los contratos

            Collections.sort(ccii, new ContratoInquilinoIdDepartamento());
            List<ContratoInquilino> contratos = ccii;

            List<byte[]> inputStreams = new ArrayList<byte[]>();

            for (int i = 0; i < contratos.size(); i++) {
                ContratoInquilino c = contratos.get(i);

                try {

                    NotificacionContratoInquilino nc = new NotificacionContratoInquilino();
                    nc.setContratoInquilino(c);
                    nc.setTitulo(plantilla.getNombrePlantilla());
                    nc.setPlantilla(plantilla);

                    Hashtable ht_objetos = new Hashtable();
                    ht_objetos.clear();
                    ht_objetos.put("admin", c.getDepartamento().getPropietarioEfectivo().getPersona());
                    ht_objetos.put("prop", c.getDepartamento().getPropietarioEfectivo());
//                ht_objetos.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().getDireccionDesdeNombre(Direccion.NOMBRE_DIRECCION_PROPIETARIO_CONTRATO_N19));
                    ht_objetos.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_CONTRACTUAL.getId()));
                    ht_objetos.put("dirinqui", c.getInquilino().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_INQUILINO.getId()));//pendiente

                    ht_objetos.put("inqui", c.getInquilino());
                    ht_objetos.put("contr", c);

//                nc.setObjetos(ht_objetos);
                    nc.setFechaProgramadaEnvio(new Date());
                    //anexando los inyecciones plantilla en el hashtable de objetos para hacerlos accesibles
                    Iterator iterhip = hti.keySet().iterator();
                    while(iterhip.hasNext()){
                        String k = (String) iterhip.next();
                        ht_objetos.put(k, hti.get(k));
                    }

                    nc = (NotificacionContratoInquilino) notificacionService.implementaContenido(nc, ht_objetos, verCamposVacios);
                    nc = (NotificacionContratoInquilino) notificacionService.implementaVersionPdfVersionFlexReport(nc);



                    notificaciones.add(nc);
                } catch (Exception ex) {
                    notifications.create().withCaption(ex.getMessage()).show();
                    return;
                }
            }
            if (notificaciones.size()>0){
                dataManager.commit(new CommitContext(notificaciones));
                notifications.create().withCaption("Notificaciones generadas exitósamente").show();
            }

        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage()).show();
        }
    }

    private boolean isDataValid(){

        return lkpPlantilla.getValue() != null;
    }

    public void onBtnSeleccionarTodosPropietariosClick() {
        tblPropietarios.setSelected(tblPropietarios.getItems().getItems());
    }

    public void onBtnSeleccionarTodosDepartamentosClick() {
        treeDepartamentos.setSelected(treeItemsDc.getItems());
    }

    public TextField getColumnForInyeccion(HelperInyeccionPlantilla hip){
        TextField tf = componentsFactory.createComponent(TextField.NAME);
        String valor = "";
        if (hip.getValor()!=null){
            valor = hip.getValor();
        }
        tf.setValue(valor);
        tf.addValueChangeListener(e->{
            hip.setValor((String) tf.getValue());
        });
        return tf;
    }
}