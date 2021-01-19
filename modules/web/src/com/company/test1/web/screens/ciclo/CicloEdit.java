package com.company.test1.web.screens.ciclo;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.ciclos.Entrada;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.service.CicloService;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.documentosfotograficos.CarpetaDocumentosFotograficosEdit;
import com.company.test1.web.screens.documentosfotograficos.VisorDocumentosFotograficos;
import com.company.test1.web.screens.entrada.EntradaEdit;
import com.company.test1.web.screens.evento.EventoEdit;
import com.company.test1.web.screens.imputaciondocumentoimputable.ImputacionDocumentoImputableEdit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.model.impl.InstanceContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.Ciclo;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_Ciclo.edit")
@UiDescriptor("ciclo-edit.xml")
@EditedEntityContainer("cicloDc")
@LoadDataBeforeShow
public class CicloEdit extends StandardEditor<Ciclo> {


    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<Ciclo> cicloDc;
    @Inject
    private CollectionContainer<Evento> eventosDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private CollectionLoader<Entrada> entradasDl;

    @Inject
    private CollectionLoader<ImputacionDocumentoImputable> imputacionDocumentoImputablesDl;
    @Inject
    private Table<Entrada> tableEntradas;

    @Inject
    private Table<ImputacionDocumentoImputable> tableImputaciones;
    @Inject
    private TextField<String> tituloCicloField;
    @Inject
    private TextField<String> codigoCicloField;
    @Inject
    private LookupField<Evento> lkpEventos;

    @Inject
    private Table<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosesTable;
    @Inject
    private CollectionLoader<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosDl;
    @Inject
    private Notifications notifications;
    @Inject
    private CicloService cicloService;
    @Inject
    private PickerField<Departamento> departamentoField;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private InstanceLoader<Ciclo> cicloDl;
    @Inject
    private CollectionLoader<Evento> eventosDl;

    @Inject
    private LookupField<Proveedor> lkpProveedoresImputaciones;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        if (event.getOptions() instanceof MapScreenOptions){
            MapScreenOptions mso = (MapScreenOptions) event.getOptions();
            if (mso.getParams().containsKey("newEntity")){
                this.setEntityToEdit(new Ciclo());

            }
        }
        
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        int Y = 2;
    }

    @Subscribe
    public void onAfterShow1(AfterShowEvent event) {
        if (cicloDc.getItem().getColeccionAdjuntos()==null){
            ColeccionArchivosAdjuntos caa = dataContext.create(ColeccionArchivosAdjuntos.class);
            caa.setNombre("(Nombre Coleccion)");
            cicloDc.getItem().setColeccionAdjuntos(caa);
        }
    }

    @Install(to = "proveedoresImputacionesDl", target = Target.DATA_LOADER)
    private List<Proveedor> proveedoresImputacionesDlLoadDelegate(LoadContext<Proveedor> loadContext) {
        List<ImputacionDocumentoImputable> idis = cicloDc.getItem().getImputacionesDocumentoImputable();
        ArrayList<Proveedor> provs = new ArrayList<Proveedor>();
        for (int i = 0; i < idis.size(); i++) {
            ImputacionDocumentoImputable idi = idis.get(i);
            DocumentoImputable di = idi.getDocumentoImputable();
            if(di instanceof DocumentoProveedor){
                DocumentoProveedor dp = (DocumentoProveedor) di;
                if (dp instanceof FacturaProveedor){
                    dp = dataManager.reload(dp, "facturaProveedor-view");
                }
                if (dp instanceof Presupuesto){
                    dp = dataManager.reload(dp, "presupuesto-view");
                }
                Proveedor pr = dp.getProveedor();
                if (provs.indexOf(pr)==-1){
                    provs.add(pr);
                }
            }
        }
        Collections.sort(provs, new Comparator<Proveedor>(){
            public int compare(Proveedor p1, Proveedor p2){
                return p1.getPersona().getNombreCompleto().compareTo(p2.getPersona().getNombreCompleto());
            }
        });
        return provs;
    }




    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        entradasDl.load();
        imputacionDocumentoImputablesDl.load();
        carpetaDocumentosFotograficosDl.load();
    }

    @Subscribe("departamentoField")
    public void onDepartamentoFieldValueChange(HasValue.ValueChangeEvent<Departamento> event) {
        try {
            if (event.isUserOriginated()){
                String cc = cicloService.calculaNuevoCodigoCiclo(departamentoField.getValue());
                String tc = cicloService.calculaNuevoTituloCiclo(departamentoField.getValue());
                codigoCicloField.setValue(cc);
                tituloCicloField.setValue(tc);
            }


        } catch (Exception e) {
            notifications.create().withCaption("No se pudo generar el codigo de ciclo");
        }
    }
    
    
    

    

    



    @Install(to = "entradasDl", target = Target.DATA_LOADER)
    private List<Entrada> entradasDlLoadDelegate(LoadContext<Entrada> loadContext) {
        Evento e = lkpEventos.getValue();
        if (e!=null){
            ArrayList<Entrada> al = new ArrayList<Entrada>();
            List<Entrada> l = cicloDc.getItem().getEntradas();
            for(int i = 0;i < l.size();i++){
                if (l.get(i).getEvento()==null) continue;
                if (l.get(i).getEvento().getId().compareTo(e.getId())==0){
                    al.add(l.get(i));
                }
            }
            return al;
        }else{
            if (cicloDc.getItemOrNull()!=null){
                return cicloDc.getItem().getEntradas();
            }else{
                return new ArrayList();
            }

        }

    }

    @Install(to = "imputacionDocumentoImputablesDl", target = Target.DATA_LOADER)
    private List<ImputacionDocumentoImputable> imputacionDocumentoImputablesDlLoadDelegate(LoadContext<ImputacionDocumentoImputable> loadContext) {
        Evento e = lkpEventos.getValue();
        if (e!=null){
            ArrayList<ImputacionDocumentoImputable> al = new ArrayList<ImputacionDocumentoImputable>();
            List<ImputacionDocumentoImputable> l = cicloDc.getItem().getImputacionesDocumentoImputable();
            for(int i = 0;i < l.size();i++){
                if (l.get(i).getEvento()==null) continue;
                if (l.get(i).getEvento().getId().compareTo(e.getId())==0){
                    ImputacionDocumentoImputable idi = l.get(i);
                    DocumentoImputable di = idi.getDocumentoImputable();
                    if (di instanceof FacturaProveedor){
                        di = (FacturaProveedor) dataManager.reload(di, "facturaProveedor-view");
                        idi.setDocumentoImputable(di);
                    }
                    if (di instanceof Presupuesto){
                        di = (Presupuesto) dataManager.reload(di, "presupuesto-view");
                        idi.setDocumentoImputable(di);
                    }
                    //si hay un proveedor seleccionado filtrar por él, sino no filtrar
                    if (lkpProveedoresImputaciones.getValue()==null){
                        al.add(idi);
                    }else{
                        if (((DocumentoProveedor)idi.getDocumentoImputable()).getProveedor().getId().compareTo(lkpProveedoresImputaciones.getValue().getId())==0){
                            al.add(idi);
                        }
                    }

                }
            }
            return al;
        }else{
            if (cicloDc.getItemOrNull()!=null){
                ArrayList al = new ArrayList();
                for (int i = 0; i < cicloDc.getItem().getImputacionesDocumentoImputable().size(); i++) {
                    ImputacionDocumentoImputable idi = cicloDc.getItem().getImputacionesDocumentoImputable().get(i);
                    DocumentoImputable di = idi.getDocumentoImputable();
                    if (di instanceof FacturaProveedor){
                        di = (FacturaProveedor) dataManager.reload(di, "facturaProveedor-view");
                        idi.setDocumentoImputable(di);
                    }
                    if (di instanceof Presupuesto){
                        di = (Presupuesto) dataManager.reload(di, "presupuesto-view");
                        idi.setDocumentoImputable(di);
                    }
                    //si hay un proveedor seleccionado filtrar por él, sino no filtrar
                    if (lkpProveedoresImputaciones.getValue()==null){
                        al.add(idi);
                    }else{
                        if (((DocumentoProveedor)idi.getDocumentoImputable()).getProveedor().getId().compareTo(lkpProveedoresImputaciones.getValue().getId())==0){
                            al.add(idi);
                        }
                    }
                }
                return al;
            }else{
                return new ArrayList();
            }

        }
    }

    @Install(to = "carpetaDocumentosFotograficosDl", target = Target.DATA_LOADER)
    private List<CarpetaDocumentosFotograficos> carpetaDocumentosFotograficosDlLoadDelegate(LoadContext<CarpetaDocumentosFotograficos> loadContext) {
        Evento e = lkpEventos.getValue();
        if (e!=null){
            ArrayList<CarpetaDocumentosFotograficos> al = new ArrayList<CarpetaDocumentosFotograficos>();
            List<CarpetaDocumentosFotograficos> l = cicloDc.getItem().getCarpetasDocumentosFotograficos();
            for(int i = 0;i < l.size();i++){
                if (l.get(i).getEvento()==null) continue;
                if (l.get(i).getEvento().getId().compareTo(e.getId())==0){
                    al.add(l.get(i));
                }
            }
            return al;
        }else{
            if (cicloDc.getItemOrNull()!=null){
                return cicloDc.getItem().getCarpetasDocumentosFotograficos();
            }else{
                return new ArrayList();
            }

        }
    }

    @Install(to = "eventosDl", target = Target.DATA_LOADER)
    private List<Evento> eventosDlLoadDelegate(LoadContext<Evento> loadContext) {
        ArrayList al = new ArrayList(cicloDc.getItem().getEventos());
        Collections.sort(al, new Comparator<Evento>(){

            public int compare(Evento ev1, Evento ev2){
                try {
                    /**
                     * Existe un error de datos en la tabla evento. Hay 206 registros sin fecha asignada.
                     * Se debería corregir
                     * PENDIENTE
                     */
                    if ((ev1.getFecha() == null)||(ev2.getFecha()==null)){
                        return -1;
                    }
                    return -1 * ev1.getFecha().compareTo(ev2.getFecha());
                }catch(Exception exc){
                    int y = 2;
                }
                return 0;
            }

        });
        return al;
    }


    
    



    @Subscribe("lkpEventos")
    private void onLkpEventosValueChange(HasValue.ValueChangeEvent<Evento> event) {
        LookupField<Evento> lkp = (LookupField<Evento>)event.getComponent();
        Evento e = lkp.getValue();


    }

    @Subscribe("lkpEventos")
    private void onLkpEventosValueChange1(HasValue.ValueChangeEvent<Evento> event) {
        entradasDl.load();
        imputacionDocumentoImputablesDl.load();
        carpetaDocumentosFotograficosDl.load();
    }


    
    
    
    

    
    
    
    
    




    public void onBtnNuevoEventoClick() {
        ScreenLaunchUtil.launchNewEntityStreen(Evento.class, screenBuilders, this, OpenMode.DIALOG, dataContext,
                (s)->{
                    Evento ev = ((EventoEdit)s).getEditedEntity();
                    cicloDc.getItem().getEventos().add(ev);
                    ev.setCiclo(cicloDc.getItem());
                    eventosDc.getMutableItems().add(ev);
                    dataContext.merge(ev);
                });
    }

    @Subscribe("tableEntradas.create")
    private void onTableEntradasCreate(Action.ActionPerformedEvent event) {
//        Entrada e = dataContext.create(Entrada.class);
//        e.setCiclo(cicloDc.getItem());
//        if (lkpEventos.getValue()!=null){
//            e.setEvento(lkpEventos.getValue());
//        }
//        ScreenLaunchUtil.launchEditEntityScreen(e, null, tableEntradas, screenBuilders, this, OpenMode.DIALOG, dataContext,
//                ev->{
//                    eventosDl.load();
//                });
        Screen s = screenBuilders.editor(Entrada.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .withListComponent(tableEntradas)
                .withParentDataContext(dataContext)
                .newEntity()
                .withInitializer(entrada->{
                    entrada.setCiclo(cicloDc.getItem());
                    if (lkpEventos.getValue()!=null){
                        entrada.setEvento(lkpEventos.getValue());
                    }
                })
                .build();
        s.addAfterCloseListener(ev->{
            //por si se ha creado un nuevo evento
            StandardCloseAction dca = (StandardCloseAction) ev.getCloseAction();
            if (dca.getActionId().compareTo("commit")==0){
                EntradaEdit ee = (EntradaEdit) ev.getScreen();
                eventosDl.load();
                if (cicloDc.getItem().getEntradas().indexOf(ee.getEditedEntity())==-1){
                    cicloDc.getItem().getEntradas().add(ee.getEditedEntity());
                    entradasDl.load();
                }
            }

        });
        s.show();
    }

    @Subscribe("tableEntradas.edit")
    private void onTableEntradasEdit(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchEditEntityScreen(tableEntradas.getSingleSelected(), null, tableEntradas, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
        Screen s = screenBuilders.editor(Entrada.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .withListComponent(tableEntradas)
                .withParentDataContext(dataContext)

                .editEntity(tableEntradas.getSingleSelected())
                .build();
        s.addAfterCloseListener(e->{
            eventosDl.load();
        });
        s.show();
    }

    @Subscribe("tableImputaciones.create")
    private void onTableImputacionesCreate(Action.ActionPerformedEvent event) {
        HashMap hm = new HashMap();
        hm.put("fromScreen", this.getId());
        MapScreenOptions mso = new MapScreenOptions(hm);
        Screen s = screenBuilders.editor(ImputacionDocumentoImputable.class, this).newEntity().withInitializer(idi->{idi.setCiclo(cicloDc.getItem());})
                .withLaunchMode(OpenMode.DIALOG)
                .withListComponent(tableImputaciones)
                .withParentDataContext(dataContext)
                .withOptions(mso)
                .build();

        s.show();
    }

    @Subscribe("tableImputaciones.edit")
    private void onTableImputacionesEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableImputaciones.getSingleSelected(), null, tableImputaciones, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    @Subscribe("carpetaDocumentosFotograficosesTable.create")
    public void onCarpetaDocumentosFotograficosesTableCreate(Action.ActionPerformedEvent event) {
        Screen s = screenBuilders.editor(CarpetaDocumentosFotograficos.class, this).newEntity()
                .withLaunchMode(OpenMode.DIALOG)
                .withListComponent(carpetaDocumentosFotograficosesTable)
                .withParentDataContext(dataContext)
                .build();

        ((CarpetaDocumentosFotograficosEdit)s).setCiclo(this.getEditedEntity());
        s.addAfterCloseListener(e->{
            CarpetaDocumentosFotograficosEdit cdfe = (CarpetaDocumentosFotograficosEdit)s;
            cicloDc.getItem().getCarpetasDocumentosFotograficos().add(cdfe.getEditedEntity());
            cdfe.getEditedEntity().setCiclo(cicloDc.getItem());
            dataContext.merge(cdfe.getEditedEntity());
        });

        s.show();
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        int y = 2;
    }



    

    @Subscribe("carpetaDocumentosFotograficosesTable.edit")
    public void onCarpetaDocumentosFotograficosesTableEdit(Action.ActionPerformedEvent event) {
        if (carpetaDocumentosFotograficosesTable.getSingleSelected()==null){
            notifications.create().withCaption("Seleccionar una carpeta").show();
            return;
        }
//        dataContext.merge(carpetaDocumentosFotograficosesTable.getSingleSelected());
        Screen s = screenBuilders.editor(CarpetaDocumentosFotograficos.class, this).editEntity(carpetaDocumentosFotograficosesTable.getSingleSelected())
                .withLaunchMode(OpenMode.DIALOG)
                .withListComponent(carpetaDocumentosFotograficosesTable)
                .withParentDataContext(dataContext)
                .build();
        ((CarpetaDocumentosFotograficosEdit)s).setCiclo(this.getEditedEntity());
        s.show();
    }



    public void visualizarCarpeta(){
        if (carpetaDocumentosFotograficosesTable.getSingleSelected()==null){
            notifications.create().withCaption("Seleccionar una carpeta").show();
            return;
        }

        CarpetaDocumentosFotograficos cdf = dataManager.reload(carpetaDocumentosFotograficosesTable.getSingleSelected(), "carpetaDocumentosFotograficos-view");

        Screen s2 = screenBuilders.screen(this).withScreenClass(VisorDocumentosFotograficos.class).withLaunchMode(OpenMode.DIALOG).build();
        ((VisorDocumentosFotograficos)s2).setCarpeta(cdf);
        s2.show();
    }

    public Button VerColumn(ImputacionDocumentoImputable idi){

        Button b = uiComponents.create(Button.NAME);
        b.setCaption("Ver");
        b.addClickListener(e->{
            ArchivoAdjunto aa = idi.getDocumentoImputable().getColeccionArchivosAdjuntos().getArchivos().get(0);
            ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
            byte[] bb = aaext.getRepresentacionSerial();
            bb = Base64.getMimeDecoder().decode(bb);
            bb = Base64.getMimeDecoder().decode(bb);
            exportDisplay.show(new ByteArrayDataProvider(bb), aa.getNombreArchivo());
        });
        return b;
    }

    public Component getColumnNombreProveedor(ImputacionDocumentoImputable idi){
        DocumentoImputable di = idi.getDocumentoImputable();
        String nombreProveedor = "";
        if (di instanceof FacturaProveedor){

            di = dataManager.reload(di, "facturaProveedor-view");
            nombreProveedor = ((FacturaProveedor)di).getProveedor().getPersona().getNombreCompleto();
        }else if(di instanceof Presupuesto){
            di = dataManager.reload(di, "presupuesto-view");
            nombreProveedor = ((Presupuesto)di).getProveedor().getPersona().getNombreCompleto();
        }
        Label l = uiComponents.create(Label.NAME);
        l.setValue(nombreProveedor);
        return l;
    }

    @Subscribe("lkpProveedoresImputaciones")
    public void onLkpProveedoresImputacionesValueChange(HasValue.ValueChangeEvent<Proveedor> event) {
        imputacionDocumentoImputablesDl.load();
    }


    


}