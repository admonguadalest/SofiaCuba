package com.company.test1.web.screens.notificaciones;

import com.company.test1.entity.TreeItem;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.NombreTipoDireccion;
import com.company.test1.entity.nonpersistententities.HelperInyeccionPlantilla;
import com.company.test1.entity.notificaciones.Notificacion;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.service.ContratosService;
import com.company.test1.service.NotificacionService;
import com.company.test1.service.PdfService;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_Recibospendientesnotificacionperiodica")
@UiDescriptor("recibos-pendientes-notificacion-periodica.xml")
public class Recibospendientesnotificacionperiodica extends Screen {

    ContratoInquilino contratoNotificacion = null;
    Notificacion notificacion = null;
    @Inject
    private CollectionLoader<Plantilla> plantillasDl;
    @Inject
    private NotificacionService notificacionService;
    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;
    @Inject
    private DateField<java.sql.Date> dteRecibosEmisionHastaFecha;
    @Inject
    private ContratosService contratosService;
    @Inject
    private LookupField<Plantilla> lkpPlantilla;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private PdfService pdfService;
    @Inject
    private Table<HelperInyeccionPlantilla> tblValoresPlantilla;
    @Inject
    private CheckBox chkVerCamposVacios;
    @Inject
    private TextField<String> txtContrato;
    @Inject
    private CollectionContainer<HelperInyeccionPlantilla> helperInyeccionPlantillasDc;
    @Inject
    private UiComponents uiComponents;

    public void setContratoNotificacion(ContratoInquilino ci){
        ContratoInquilino ci_ = dataManager.reload(ci, "contratoInquilino-notificaciones-aumentos-view");
        this.contratoNotificacion = ci_;
        Departamento d = ci.getDepartamento();
        txtContrato.setValue(ci.getInquilino().getNombreCompleto() + " " + d.getNombreDescriptivoCompleto());
    }
    public Notificacion getNotificacion(){
        return this.notificacion;
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        plantillasDl.load();
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

            ArrayList<ContratoInquilino> ccii = new ArrayList();
            ccii.add(this.contratoNotificacion);
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
//                ht_objetos.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().getdteRecibosEmisionHastaFechaDireccionDesdeNombre(Direccion.NOMBRE_DIRECCION_PROPIETARIO_CONTRATO_N19));
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

                    if (dteRecibosEmisionHastaFecha.getValue()!=null) {
                        ht_objetos.put("TEXTO_RECIBOS_PENDIENTES", c.getTextoRecibosPendientes(dteRecibosEmisionHastaFecha.getValue()));
                        ht_objetos.put("NUM_RECIBOS_PENDIENTES", c.getNumRecibosPendientes(dteRecibosEmisionHastaFecha.getValue()));
                        ht_objetos.put("IMPORTE_TOTAL_PENDIENTE_FORMATEADO", c.getImporteTotalPendienteFormateado(dteRecibosEmisionHastaFecha.getValue()));


                    }else{
                        ht_objetos.put("TEXTO_RECIBOS_PENDIENTES", c.getTextoRecibosPendientes());
                        ht_objetos.put("NUM_RECIBOS_PENDIENTES", c.getNumRecibosPendientes());
                        ht_objetos.put("IMPORTE_TOTAL_PENDIENTE_FORMATEADO", c.getImporteTotalPendienteFormateado());
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

    private boolean isDataValid(){

        return lkpPlantilla.getValue() != null;
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

            ArrayList<ContratoInquilino> ccii = new ArrayList();
            ccii.add(this.contratoNotificacion);
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

                    if (dteRecibosEmisionHastaFecha.getValue()!=null) {
                        ht_objetos.put("TEXTO_RECIBOS_PENDIENTES", c.getTextoRecibosPendientes(dteRecibosEmisionHastaFecha.getValue()));
                        ht_objetos.put("NUM_RECIBOS_PENDIENTES", c.getNumRecibosPendientes(dteRecibosEmisionHastaFecha.getValue()));
                        ht_objetos.put("IMPORTE_TOTAL_PENDIENTE_FORMATEADO", c.getImporteTotalPendienteFormateado(dteRecibosEmisionHastaFecha.getValue()));


                    }else{
                        ht_objetos.put("TEXTO_RECIBOS_PENDIENTES", c.getTextoRecibosPendientes());
                        ht_objetos.put("NUM_RECIBOS_PENDIENTES", c.getNumRecibosPendientes());
                        ht_objetos.put("IMPORTE_TOTAL_PENDIENTE_FORMATEADO", c.getImporteTotalPendienteFormateado());
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
                this.notificacion = (Notificacion) notificaciones.get(0);
            }

        }catch(Exception exc){
            notifications.create().withCaption(exc.getMessage()).show();
        }
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

    public TextField getColumnForInyeccion(HelperInyeccionPlantilla hip){
        TextField tf = uiComponents.create(TextField.NAME);
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