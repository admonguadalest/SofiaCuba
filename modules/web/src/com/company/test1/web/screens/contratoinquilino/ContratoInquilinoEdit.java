package com.company.test1.web.screens.contratoinquilino;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.Persona;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.contratosinquilinos.*;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.UsoContratoEnum;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.ordenespago.OrdenPagoContratoInquilino;
import com.company.test1.service.ContratosService;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.NotificacionService;
import com.company.test1.service.PlantillaService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.contratoinquilino.fragments.FianzaFragment;
import com.company.test1.web.screens.contratoinquilino.fragments.ImplementacionModeloFragment;
import com.company.test1.web.screens.documentosfotograficos.VisorDocumentosFotograficos;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.haulmont.cuba.web.gui.components.WebTextField;
import org.springframework.context.ApplicationEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_ContratoInquilino.edit")
@UiDescriptor("contrato-inquilino-edit.xml")
@EditedEntityContainer("contratoInquilinoDc")
@LoadDataBeforeShow
public class ContratoInquilinoEdit extends StandardEditor<ContratoInquilino> {
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<ContratoInquilino> contratoInquilinoDc;

    @Inject
    private InstancePropertyContainer<Fianza> fianzaDc;
    @Inject
    private InstancePropertyContainer<LiquidacionSuscripcion> liquidacionSuscripcionDc;
    @Inject
    private Table<Anexo> tableAnexos;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private PlantillaService plantillaService;

    @Inject
    private InstanceContainer<ArchivoAdjunto> escaneoArchivoAdjuntoDc;
    @Inject
    private InstanceContainer<ArchivoAdjunto> escaneoRenunciaDc;
    @Inject
    private CheckBox chkCedula;
    @Inject
    FianzaFragment fianzaFragment;
    @Inject
    private PickerField<Departamento> departamentoField;

    @Inject
    private CheckBox chkLiqInicial;
    @Inject
    private CheckBox chkLiqFinal;
    @Inject
    private CheckBox chkEntregaLlaves;
    @Inject
    private CheckBox chkDocumBancaria;
    @Inject
    private CheckBox chkDoctoFotografico;
    @Inject
    private CheckBox chkContrato;
    @Inject
    private CheckBox chkCertCalifEnerg;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Button btnImprimirSeleccionados;

    @Inject
    private TabSheet tabContratoVarios;
    @Named("tabContratoVarios.tabClausuladoContrato")
    private VBoxLayout tabClausuladoContrato;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private Fragments fragments;
    @Inject
    private VBoxLayout vbClausuladoFragment;
    @Inject
    private Events events;
    @Inject
    private Table<CotitularContratoInquilino> tableCotitulares;
    @Inject
    private ContratosService contratosService;
    @Inject
    private Notifications notifications;
    @Inject
    private PickerField<Ciclo> pickerCiclo;
    @Inject
    private CollectionLoader<CarpetaDocumentosFotograficos> carpetasDocumentosFotograficosCicloDl;
    @Inject
    private Table<OrdenPagoContratoInquilino> tablePagosInquilino;
    @Inject
    private Table<NotificacionContratoInquilino> tableNotificaciones;
    @Inject
    private LookupField<CarpetaDocumentosFotograficos> lkpCDFS;

    @Inject
    private CollectionLoader<NotificacionContratoInquilino> notificacionesDl;
    @Inject
    private CollectionPropertyContainer<OrdenPagoContratoInquilino> pagosInquilinoDc;
    @Inject
    private CollectionContainer<NotificacionContratoInquilino> notificacionesDc;

    @Inject
    private NotificacionService notificacionService;
    @Inject
    private InstancePropertyContainer<LiquidacionExtincion> liquidacionExtincionDc;
    @Inject
    private InstancePropertyContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("newEntity")){
                ContratoInquilino ci = dataContext.create(ContratoInquilino.class);
                if (mso.getParams().containsKey("tipo")){
                    String tipo = (String) mso.getParams().get("tipo");
                    if (tipo.compareTo("vivienda")==0){
                        ci.setUsoContrato(UsoContratoEnum.VIVIENDA);
                    }
                    if (tipo.compareTo("local")==0){
                        ci.setUsoContrato(UsoContratoEnum.LOCAL);
                    }
                }

                this.setEntityToEdit(ci);
            }
        }
        
        
    }

    @Inject
    private PickerField<Persona> inquilinoField;


    @Subscribe
    private void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (contratoInquilinoDc.getItem().getNombreTipoIndiceIncrementos()==null){
            contratoInquilinoDc.getItem().setNombreTipoIndiceIncrementos("IPC");
        }
        int y = 2;
    }

    
    
    

    
    
    

    @Subscribe
    private void onAfterShow1(AfterShowEvent event) {
        if (this.getEditedEntity().getEstadoContrato()== EstadoContratoInquilinoEnum.VIGENTE){
            departamentoField.setEditable(false);
        }
        //cargando Notificaciones
        notificacionesDl.load();
    }
    
    

    

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        if (contratoInquilinoDc.getItem().getColeccionArchivosAdjuntos()==null){
            contratoInquilinoDc.getItem().setColeccionArchivosAdjuntos(dataContext.create(ColeccionArchivosAdjuntos.class));
            contratoInquilinoDc.getItem().getColeccionArchivosAdjuntos().setNombre("Contrato");

        }else{
            contratoInquilinoDc.getItem().getColeccionArchivosAdjuntos().setNombre("Contrato");
        }
        fianzaFragment.loadDocumentacionesInquilinos();

    }



    

    @Subscribe(id = "escaneoArchivoAdjuntoDc", target = Target.DATA_CONTAINER)
    private void onEscaneoArchivoAdjuntoDcItemChange(InstanceContainer.ItemChangeEvent<ArchivoAdjunto> event) {
        if (escaneoArchivoAdjuntoDc.getItemOrNull()!=null){
            if (escaneoArchivoAdjuntoDc.getItem().getRepresentacionSerial()!=null){
                contratoInquilinoDc.getItem().setEscaneoContrato(escaneoArchivoAdjuntoDc.getItem());
            }
        }else{
            return;
        }

    }

    @Subscribe(id = "escaneoRenunciaDc", target = Target.DATA_CONTAINER)
    private void onEscaneoRenunciaDcItemChange(InstanceContainer.ItemChangeEvent<ArchivoAdjunto> event) {
        if(escaneoRenunciaDc.getItemOrNull()!=null){
            if (escaneoRenunciaDc.getItem().getRepresentacionSerial()!=null){
                contratoInquilinoDc.getItem().setArchivoAdjuntoRenuncia(escaneoRenunciaDc.getItem());
            }
        }
    }
    
    
    
    
    

    @Subscribe("tableAnexos.create")
    private void onTableAnexosCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(Anexo.class, null, tableAnexos, screenBuilders, this, OpenMode.DIALOG,
//                dataContext, null);
        Screen s = screenBuilders.editor(Anexo.class, this).newEntity()
                .withInitializer(a->{a.setContratoInquilino(contratoInquilinoDc.getItem());})
                .withLaunchMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withListComponent(tableAnexos)
                .build();
        s.show();
        
    }

    @Subscribe("tableAnexos.edit")
    private void onTableAnexosEdit(Action.ActionPerformedEvent event) {
        Anexo anexo = tableAnexos.getSingleSelected();
        if (anexo == null){
            notifications.create().withDescription("Seleccionar un anexo").show();
            return;
        }
        Screen s = screenBuilders.editor(Anexo.class, this).editEntity(anexo)
                .withLaunchMode(OpenMode.DIALOG)
                .withParentDataContext(dataContext)
                .withListComponent(tableAnexos)
                .build();
        s.show();
    }

    @Subscribe("tabContratoVarios")
    public void onTabContratoVariosSelectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        if (event.getSelectedTab().getName().compareTo("tabClausuladoContrato")==0){
            vbClausuladoFragment.removeAll();
            if (contratoInquilinoDc.getItem().getImplementacionModelo()==null){
                ImplementacionModelo im = dataContext.create(ImplementacionModelo.class);
                im.setContratoInquilino(contratoInquilinoDc.getItem());
                contratoInquilinoDc.getItem().setImplementacionModelo(im);

            }
            ScreenFragment sf = fragments.create(this, ImplementacionModeloFragment.class);
            vbClausuladoFragment.add(sf.getFragment());
        }
        if (event.getSelectedTab().getName().compareTo("tabLiquidacionExtincion")==0){
            //precargo datos sino estan
            LiquidacionExtincion le = liquidacionExtincionDc.getItem();
            if (le.getTotalesGarantias()==null){
                le.setTotalesGarantias(fianzaDc.getItem().getFianzaLegal()+fianzaDc.getItem().getFianzaComplementaria());

            }

        }
    }

    
    public void irCicloAsociado(){
        Ciclo c = contratoInquilinoDc.getItem().getCiclo();
        Screen s = screenBuilders.editor(Ciclo.class, this).withOpenMode(OpenMode.NEW_TAB).editEntity(c)
                .build();
        s.show();
    }



    public void onBtnImprimirSeleccionadosClicked() {
        Boolean[] bools = new Boolean[]{chkContrato.getValue(), chkLiqInicial.getValue(), chkCedula.getValue(), chkLiqFinal.getValue(), chkDocumBancaria.getValue(),
                                            chkEntregaLlaves.getValue(), chkDoctoFotografico.getValue(), chkCertCalifEnerg.getValue()};
        byte[] bb = new byte[0];
        try {
            bb = jasperReportService.realizaImprimiblesContratoInquilino(bools, contratoInquilinoDc.getItem());
        } catch (Exception e) {
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();
            e.printStackTrace();
        }
        exportDisplay.show(new ByteArrayDataProvider(bb), "Imprimibles.pdf");

    }
    
    
    /*
    Boton de creacion de Cotitular
     */
    public void onBtnCreateCotitular(){
        Screen s = screenBuilders.editor(CotitularContratoInquilino.class, this).newEntity().withInitializer(c->{c.setContratoInquilino(this.getEditedEntity());})
                .withListComponent(tableCotitulares).withOpenMode(OpenMode.DIALOG).withParentDataContext(dataContext).build();
        s.show();
    }

    @Subscribe("departamentoField")
    public void onDepartamentoFieldValueChange(HasValue.ValueChangeEvent<Departamento> event) {
        String numeroContrato = null;
        try {
            numeroContrato = contratosService.getNuevoNumeroParaContrato(this.getEditedEntity());
        } catch (Exception e) {
            notifications.create().withCaption("No se pudo obtener numeración para contrato").show();
            numeroContrato = "";
        }
        contratoInquilinoDc.getItem().setNumeroContrato(numeroContrato);
    }

    public void imprimirAnexo(){
        Anexo a = tableAnexos.getSingleSelected();
        if (a==null){
            notifications.create().withCaption("Seleccionar un Anexo").show();
            return;
        }
        try {
            byte[] bb = jasperReportService.realizaImpresionAnexo(a);
            exportDisplay.show(new ByteArrayDataProvider(bb), a.getNombreAnexo()+".pdf");
        } catch (Exception e) {
            notifications.create().withCaption("No se pudo imprimir el anexo seleccionado").show();
            return;
        }

    }

    @Subscribe("pickerCiclo")
    public void onPickerCicloValueChange(HasValue.ValueChangeEvent<Ciclo> event) {
        if (event.isUserOriginated()){
            if (pickerCiclo.getValue().getDepartamento().getUuid().compareTo(contratoInquilinoDc.getItem().getDepartamento().getUuid())==0){

                carpetasDocumentosFotograficosCicloDl.load();
            }else{
                notifications.create().withCaption("El ciclo seleccionado no corresponde al departamento asociado al contrato").show();
                pickerCiclo.setValue(null);
                return;
            }
        }

    }

    @Install(to = "carpetasDocumentosFotograficosCicloDl", target = Target.DATA_LOADER)
    private List<CarpetaDocumentosFotograficos> carpetasDocumentosFotograficosCicloDlLoadDelegate(LoadContext<CarpetaDocumentosFotograficos> loadContext) {
        //tomamos la lista de Carpetas de Documentos Fotograficos
        Ciclo c = pickerCiclo.getValue();
        if (c == null) return new ArrayList();
        c = dataManager.reload(c, "ciclo-view");
        ArrayList al = new ArrayList();
        for (int i = 0; i < c.getCarpetasDocumentosFotograficos().size(); i++) {
            CarpetaDocumentosFotograficos cdf = c.getCarpetasDocumentosFotograficos().get(i);
            al.add(cdf);
        }
        return al;
    }

    public void visualizarCarpetaDocumentosFotograficosFirma(){
        CarpetaDocumentosFotograficos cdf = lkpCDFS.getValue();
        if (cdf==null){
            notifications.create().withCaption("Seleccione una CDF. Asegúrese de haber seleccionado previamente un Ciclo").show();
        }
        cdf = dataManager.reload(cdf, "carpetaDocumentosFotograficos-view");

        Screen s2 = screenBuilders.screen(this).withScreenClass(VisorDocumentosFotograficos.class).withLaunchMode(OpenMode.DIALOG).build();
        ((VisorDocumentosFotograficos)s2).setCarpeta(cdf);
        s2.show();
    }


    public void actionCreatePagoInquilino(){
        OrdenPagoContratoInquilino opci = dataContext.create(OrdenPagoContratoInquilino.class);
        opci.setEmisor(contratoInquilinoDc.getItem().getDepartamento().getPropietarioEfectivo().getPersona());
        opci.setBeneficiario(contratoInquilinoDc.getItem().getInquilino());
        opci.setContratoInquilino(contratoInquilinoDc.getItem());
        Screen s = screenBuilders.editor(OrdenPagoContratoInquilino.class, this).editEntity(opci).withOpenMode(OpenMode.DIALOG).withListComponent(tablePagosInquilino).withParentDataContext(dataContext).build();
        s.show();
    }

    public void actionEditPagoInquilino(){
        OrdenPagoContratoInquilino opci = tablePagosInquilino.getSingleSelected();
        if (opci==null){
            notifications.create().withCaption("Seleccionar un Pago a un Inquilino").show();
            return;
        }
        Screen s = screenBuilders.editor(OrdenPagoContratoInquilino.class, this).editEntity(opci).withOpenMode(OpenMode.DIALOG).withListComponent(tablePagosInquilino).withParentDataContext(dataContext).build();
        s.show();
    }

    @Install(to = "notificacionesDl", target = Target.DATA_LOADER)
    private List<NotificacionContratoInquilino> notificacionesDlLoadDelegate(LoadContext<NotificacionContratoInquilino> loadContext) {

        try {
            List<NotificacionContratoInquilino> nnccii = notificacionService.devuelveNotificacionesAsociadasAContrato(contratoInquilinoDc.getItem());
            return nnccii;
        } catch (Exception e) {
            notifications.create().withCaption(e.getMessage());
        }
        return new ArrayList();
    }

    public void onBtnVerNotificacion(){
        NotificacionContratoInquilino nci = tableNotificaciones.getSingleSelected();
        if (nci==null){
            notifications.create().withCaption("Seleccionar una Notificación").show();
            return;
        }
        byte[] bb = nci.getVersionPdf();
        exportDisplay.show(new ByteArrayDataProvider(bb), nci.getTitulo()+".pdf");
    }


    
    
    
    


    
    
    
    
    
}