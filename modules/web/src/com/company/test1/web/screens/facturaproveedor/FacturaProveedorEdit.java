package com.company.test1.web.screens.facturaproveedor;

import com.company.test1.entity.*;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.service.ValidacionesService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.conceptosadicionales.RegistroAplicacionConceptoAdicionalEdit;
import com.company.test1.web.screens.imputaciondocumentoimputable.ImputacionDocumentoImputableEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.model.impl.CollectionPropertyContainerImpl;
import com.haulmont.cuba.gui.model.impl.InstanceContainerImpl;
import com.haulmont.cuba.gui.model.impl.InstancePropertyContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.*;

@UiController("test1_FacturaProveedor.edit")
@UiDescriptor("factura-proveedor-edit.xml")
@EditedEntityContainer("facturaProveedorDc")
@LoadDataBeforeShow
public class FacturaProveedorEdit extends StandardEditor<FacturaProveedor> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private InstanceContainer<FacturaProveedor> facturaProveedorDc;
    @Inject
    private Table<RegistroAplicacionConceptoAdicional> tableRegistrosAplicacionesCCAA;
    @Inject
    private Table<ImputacionDocumentoImputable> tableImputaciones;
    @Inject
    private CollectionPropertyContainer<RegistroAplicacionConceptoAdicional> registroAplicacionConceptosAdicionalesDc;

    @Inject
    private DataContext dataContext;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataComponents dataComponents;
    @Inject
    private ValidacionesService validacionesService;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstancePropertyContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;
    @Inject
    private UserSession userSession;
    @Inject
    private HBoxLayout hboxSuministros;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private BrowserFrame brwDocumentPreview;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("newEntity")){
                FacturaProveedor fp = dataContext.create(FacturaProveedor.class);

                this.setEntityToEdit(fp);
            }
        }
    }

    private void actualizaScan(){
        ArchivoAdjunto aa = facturaProveedorDc.getItem().getColeccionArchivosAdjuntos().getArchivos().get(0);
        byte[] bb = aa.getRepresentacionSerial();
        if (bb==null){
            ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
            bb = aaext.getRepresentacionSerial();
        }
        bb = Base64.getMimeDecoder().decode(bb);
        bb = Base64.getMimeDecoder().decode(bb);
        final byte[] bb_ = bb;
        brwDocumentPreview.setSource(StreamResource.class)
                .setStreamSupplier(() -> new ByteArrayInputStream(bb_))
                .setMimeType(aa.getMimeType());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (PersistenceHelper.isNew(facturaProveedorDc.getItem())) {
            ColeccionArchivosAdjuntos caa = dataContext.create(ColeccionArchivosAdjuntos.class);
            coleccionArchivosAdjuntosDc.setItem(caa);
            caa.setNombre("Factura");
            FacturaProveedor fp = facturaProveedorDc.getItem();
            fp.setColeccionArchivosAdjuntos(caa);
        }
        int y = 2;
        User user = userSession.getUser();
        if (user.getLogin().compareTo("carlosconti")==0){
            hboxSuministros.setVisible(true);
        }else{
            hboxSuministros.setVisible(false);
        }

        actualizaScan();
    }
    
    

    @Subscribe("fechaEmisionField")
    public void onFechaEmisionFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        facturaProveedorDc.getItem().setFechaDevengo(facturaProveedorDc.getItem().getFechaEmision());
    }

    @Subscribe("fechaDevengoField")
    public void onFechaDevengoFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        facturaProveedorDc.getItem().setFechaEmision(facturaProveedorDc.getItem().getFechaDevengo());
    }
    
    
    
    

    

    @Subscribe
    private void onBeforeShow(BeforeShowEvent event) {


    }

    @Subscribe
    private void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (!this.getEditedEntity().getProveedor().getModoDePagoTelematico()){
            return;
        }
        /* gestionando las validaciones */
        try {
            FacturaProveedor fp = (FacturaProveedor) validacionesService.gestionaValidacionesImputacionesDocumentoImputableDesdeDocumentoImputable(this.getEditedEntity(), true);
            this.setEntityToEdit(fp);
            this.getEditedEntityContainer().setItem(fp);
            //anadimos al contexto para que gestione el guardado de las validaciones
            dataContext.merge(fp);

            for (int i = 0; i < fp.getImputacionesDocumentoImputable().size(); i++) {
                ImputacionDocumentoImputable idi = fp.getImputacionesDocumentoImputable().get(i);
                dataContext.merge(idi);
                ValidacionImputacionDocumentoImputable vidi = idi.getValidacionImputacion();
                idi.setValidacionImputacion(null);
                idi.setValidacionImputacion(vidi);
                if (idi.getValidacionImputacion()!=null){
                    dataContext.merge(idi.getValidacionImputacion());
                }
            }

        }catch(Exception exc){
            event.preventCommit();
            notifications.create().withCaption("No se pudieron generar las validaciones para el documento: " + exc.getMessage()).show();
        }

    }
    
    
    
    

    @Subscribe("proveedorField")
    private void onProveedorFieldValueChange(HasValue.ValueChangeEvent<Proveedor> event) {
        if (!event.isUserOriginated()) return;
        Proveedor prov = facturaProveedorDc.getItem().getProveedor();
        if (prov!=null){
            List<ProgramacionConceptoAdicional> pca = prov.getProgramacionesConceptosAdicionales();
            facturaProveedorDc.getItem().getRegistroAplicacionConceptosAdicionales().clear();
            for(int i = 0;i < pca.size();i++) {
                RegistroAplicacionConceptoAdicional raca = dataContext.create(RegistroAplicacionConceptoAdicional.class);
                raca.setNifDni(prov.getPersona().getNifDni());
                raca.setDocumentoImputable(this.getEditedEntity());
                raca.setFechaValor(this.getEditedEntity().getFechaEmision());
                raca.setNumDocumento(this.getEditedEntity().getNumDocumento());
                raca.setConceptoAdicional(pca.get(i).getConceptoAdicional());
                raca.setBase(0.0);
                raca.setImporteAplicado(0.0);
                registroAplicacionConceptosAdicionalesDc.getMutableItems().add(raca);
            }


        }
    }

    private void actualizaInformacionRegistrosConceptosAdicionales(){
        Proveedor prov = facturaProveedorDc.getItem().getProveedor();
        if (prov == null){
            return;
        }
        List<RegistroAplicacionConceptoAdicional> rraaccaa = facturaProveedorDc.getItem().getRegistroAplicacionConceptosAdicionales();
        for(int i = 0;i < rraaccaa.size();i++) {
            RegistroAplicacionConceptoAdicional raca = rraaccaa.get(i);
            raca.setNifDni(prov.getPersona().getNifDni());
            raca.setDocumentoImputable(this.getEditedEntity());
            raca.setFechaValor(this.getEditedEntity().getFechaEmision());
            raca.setNumDocumento(this.getEditedEntity().getNumDocumento());

        }
    }

    @Subscribe("fechaEmisionField")
    public void onFechaEmisionFieldValueChange1(HasValue.ValueChangeEvent<Date> event) {
        if (!event.isUserOriginated()) return;
        actualizaInformacionRegistrosConceptosAdicionales();
    }

    @Subscribe("numDocumentoField")
    public void onNumDocumentoFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (!event.isUserOriginated()) return;
        actualizaInformacionRegistrosConceptosAdicionales();
    }

    private void actualizaImportePostCCAA(double importeTotalBase){
        List<RegistroAplicacionConceptoAdicional> items = registroAplicacionConceptosAdicionalesDc.getMutableItems();
        double importeTotal = importeTotalBase;
        for(int i = 0;i < items.size();i++){
            RegistroAplicacionConceptoAdicional raca = items.get(i);
            //en 18012021 acordamos con Isabel uqe al actualizar el importe total base no se actualicen los importes de los
            //conceptos adicionales
//            raca.setBase(importeTotalBase);
            if (raca.getPorcentaje()!=null){
                raca.setImporteAplicado(raca.getPorcentaje()*raca.getBase());
            }
            if (raca.getConceptoAdicional().getAdicionSustraccion()){
                importeTotal += raca.getImporteAplicado();
            }else{
                importeTotal -= raca.getImporteAplicado();
            }

        }
        facturaProveedorDc.getItem().setImportePostCCAA(importeTotal);
    }

    @Subscribe("importeTotalBaseField")
    private void onImporteTotalBaseFieldValueChange(HasValue.ValueChangeEvent<Double> event) {

        actualizaImportePostCCAA(event.getValue());

    }
    
    

    @Subscribe(id = "registroAplicacionConceptosAdicionalesDc", target = Target.DATA_CONTAINER)
    private void onRegistroAplicacionConceptosAdicionalesDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<RegistroAplicacionConceptoAdicional> event) {
        
    }

    

    public Component generateCellPorcentajeRACA(RegistroAplicacionConceptoAdicional raca){
        LookupField<Double> lkf = uiComponents.create(LookupField.NAME);

        Double[] options = null;
        if (raca.getConceptoAdicional().getAbreviacion().compareTo("IVA")==0){
            options = new Double[]{0.00, 0.04, 0.1, 0.21};
        }
        if (raca.getConceptoAdicional().getAbreviacion().compareTo("IRPF")==0){
            options = new Double[]{0.00, 0.01, 0.2, 0.15};
        }
        lkf.setOptionsList(Arrays.asList(options));

        InstanceContainer<RegistroAplicacionConceptoAdicional> ici = dataComponents.createInstanceContainer(RegistroAplicacionConceptoAdicional.class);
        ici.setItem(raca);

        lkf.addValueChangeListener(e->{
            ici.getItem().setImporteAplicado(ici.getItem().getBase() * e.getValue());
            actualizaImportePostCCAA(facturaProveedorDc.getItem().getImporteTotalBase());
        });

        lkf.setValueSource(new ContainerValueSource(ici, "porcentaje"));

        
        return lkf;

    }

    @Subscribe("tableImputaciones.create")
    private void onTableImputacionesCreate(Action.ActionPerformedEvent event) {
        ImputacionDocumentoImputable idi = new ImputacionDocumentoImputable();
        idi.setDocumentoImputable(this.getEditedEntity());

        HashMap hm = new HashMap();
        hm.put("fromScreen", this.getId());
        MapScreenOptions mso = new MapScreenOptions(hm);
//        ScreenLaunchUtil.launchEditEntityScreen(idi, null, tableImputaciones, screenBuilders, this, OpenMode.DIALOG,
//                dataContext, null);

        EditorBuilder eb = screenBuilders.editor(ImputacionDocumentoImputable.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .newEntity(idi).withParentDataContext(dataContext).withListComponent(tableImputaciones).withOptions(mso);
        Screen s = eb.build().show();


    }

    @Subscribe("tableImputaciones.edit")
    private void onTableImputacionesEdit(Action.ActionPerformedEvent event) {
        ImputacionDocumentoImputable idi = tableImputaciones.getSingleSelected();
        idi.setDocumentoImputable(this.getEditedEntity());
        Persona p = this.getEditedEntity().getProveedor().getPersona();
        if (p instanceof PersonaFisica){
            p = dataManager.reload(p, "personaFisica-view");
        }
        if (p instanceof PersonaJuridica){
            p = dataManager.reload(p, "personaJuridica-view");
        }
        this.getEditedEntity().getProveedor().setPersona(p);
        ScreenLaunchUtil.launchEditEntityScreen(tableImputaciones.getSingleSelected(), null, tableImputaciones, screenBuilders, this, OpenMode.DIALOG,
                dataContext, null);
    }
    
    public void OnBtnNuevoRCA(){
        StandardEditor se = (StandardEditor) screenBuilders.editor(RegistroAplicacionConceptoAdicional.class, this).newEntity()
                .withInitializer(e->{
                    e.setDocumentoImputable(facturaProveedorDc.getItem());
                    e.setNifDni(facturaProveedorDc.getItem().getProveedor().getPersona().getNifDni());
                    e.setNumDocumento(facturaProveedorDc.getItem().getNumDocumento());
                    e.setFechaValor(facturaProveedorDc.getItem().getFechaEmision());

                }).withListComponent(tableRegistrosAplicacionesCCAA).withOpenMode(OpenMode.DIALOG).withParentDataContext(dataContext).build();
        se.addAfterCloseListener(e->{
            //table refresh
            actualizaImportePostCCAA(facturaProveedorDc.getItem().getImporteTotalBase());
        });
        se.show();
    }

    public void OnBtnEditarRCA(){
        RegistroAplicacionConceptoAdicional raca = tableRegistrosAplicacionesCCAA.getSingleSelected();
        if (raca == null){
            notifications.create().withDescription("Seleccionar un registro de conceptos adicionales").show();
            return;
        }
        StandardEditor se = (StandardEditor) screenBuilders.editor(RegistroAplicacionConceptoAdicional.class, this).editEntity(raca)
                .withListComponent(tableRegistrosAplicacionesCCAA).withOpenMode(OpenMode.DIALOG).withParentDataContext(dataContext).build();
        se.addAfterCloseListener(e->{
            actualizaImportePostCCAA(facturaProveedorDc.getItem().getImporteTotalBase());

        });
        se.show();
    }



    public void OnBtnEliminarRCA(){
        RegistroAplicacionConceptoAdicional raca = tableRegistrosAplicacionesCCAA.getSingleSelected();
        raca.setDocumentoImputable(null);
        facturaProveedorDc.getItem().getRegistroAplicacionConceptosAdicionales().remove(raca);
        dataContext.remove(raca);
    }


    
    

    
}