package com.company.test1.web.screens;

import com.company.test1.entity.*;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.service.ContabiService;
import com.company.test1.web.screens.facturaproveedor.FacturaProveedorWithAttachmentEdit;
import com.company.test1.web.screens.presupuesto.PresupuestoEditWithAttachment;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.ContainerDataGridItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.impl.CollectionContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;

import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.sun.mail.imap.OlderTerm;
import com.sun.mail.imap.YoungerTerm;
import com.sun.mail.util.BASE64DecoderStream;

import org.apache.poi.util.IOUtils;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;


@UiController("test1_CargaDesdePuntoEntradaDocumentos")
@UiDescriptor("carga-desde-punto-entrada-documentos.xml")
public class CargaDesdePuntoEntradaDocumentos extends Screen {

    @Inject
    private LookupField<String> pkrUltimosN;
    @Inject
    private Label<String> lblRelPath;
    @Inject
    private Notifications notifications;


    @Inject
    private CollectionLoader<MailStructure> mailsDl;
    @Inject
    private CollectionContainer<MailStructure> mailsDc;
    @Inject
    private ScrollBoxLayout mailslist;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private BrowserFrame brwMailPreview;
    @Inject
    private DataGrid<MailStructure> dataGridMails;
    @Inject
    private VBoxLayout vboxAttachments;
    @Inject
    private BrowserFrame brwAttachmentPreview;
    @Inject
    private Button btnNuevoPpto;
    @Inject
    private Button btnBuscar;
    @Inject
    private TextField<String> txtSubjectContains;
    @Inject
    private TextField<String> txtAddressContains;
    @Inject
    private DateField<Date> dteTo;
    @Inject
    private Button btnNuevaFactura;

    @Inject
    private DateField<Date> dteFrom;
    @Inject
    private CollectionContainer<PuntoEntradaDocumentos> puntosEntradaDc;
    public String nombreDocumentoSeleccionado = null;
    public byte[] representacionSerialDocumentoSeleccionado = null;
    public String mimeTypeDocumentoSeleccionado = null;

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private PickerField<PuntoEntradaDocumentos> pkrPuntoEntradaDocumentos;
    @Inject
    private SplitPanel splt2;

    @Inject
    private VBoxLayout vboxped;
    @Inject
    private VBoxLayout vboxserverstorage;
    @Inject
    private VBoxLayout vboxmail;
    Hashtable<PuntoEntradaDocumentos, Collection<Component>> componentBuffer = new Hashtable<>();
    @Inject
    private CollectionLoader<StorageElement> storageElementsDl;

    @Inject
    private DataGrid<StorageElement> dgStorageElements;

    private String relativePathCurrPedStorage = "";
    @Inject
    private Button btnSubir;
    @Inject
    private HBoxLayout botoneraClientStorage;
    @Inject
    private HBoxLayout botoneraStorage;
    @Inject
    private FileMultiUploadField multiUploadField;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private DataManager dataManager;
    @Inject
    private FileLoader fileLoader;
    @Inject
    private CollectionContainer<StorageElement> storageElementsDc;

    EmailChecker currentEmailChecker;
    @Inject
    private VBoxLayout vbrossum;
    @Inject
    private CollectionLoader<RossumAnnotation> rossumAnnotationsDl;

    @Inject
    private DataGrid<RossumAnnotation> dataGridRossumAnnotations;

    RossumCommonDialogs rcd = null;
    @Inject
    private Dialogs dialogs;
    @Inject
    private CollectionContainer<RossumAnnotation> rossumAnnotationsDc;
    @Inject
    private ContabiService contabiService;
    @Inject
    private Button refreshRossum;
    @Inject
    private UserSession userSession;

    @Subscribe("btnBuscar")
    public void onBtnBuscarClick(Button.ClickEvent event) {
        if (currentEmailChecker!=null){
            try {
                currentEmailChecker.loadMessages(currentEmailChecker.folder);
            } catch (Exception e) {
                notifications.create().withDescription(e.getMessage()).show();
            }

        }



    }







    private SearchTerm buildSearchTermFromCriteriaComponents(){
        ArrayList searchTerms = new ArrayList();
        if (dteFrom.getValue()!=null){
            Date dateFrom = dteFrom.getValue();
            int secsFrom = (int) ((long)dateFrom.getTime()/(long) 1000);
            SentDateTerm rdt = new SentDateTerm(ReceivedDateTerm.GE, dateFrom);
            OlderTerm ot = new OlderTerm(secsFrom);
            searchTerms.add(rdt);
        }
        if (dteTo.getValue()!=null){
            Date dateTo = dteTo.getValue();
            int secsTo = (int) ((long)dateTo.getTime()/(long) 1000);
            SentDateTerm yt = new SentDateTerm(ComparisonTerm.LE, dateTo);
            searchTerms.add(yt);
        }
        String addressContains = txtAddressContains.getValue();
        if ((addressContains!=null)&&(addressContains.length()>0)){
            FromStringTerm st = new FromStringTerm(addressContains);
            searchTerms.add(st);
        }
        String subjectContains = txtSubjectContains.getValue();
        if ((subjectContains!=null)&&(subjectContains.length()>0)){
            SubjectTerm st = new SubjectTerm(subjectContains);
            searchTerms.add(st);
        }



        if (searchTerms.size()==0){
            return null;
        }

        AndTerm andTerm = new AndTerm((SearchTerm[])searchTerms.toArray(new SearchTerm[0]));
        return andTerm;
    }

    public void refrescar(){
        PuntoEntradaDocumentos pde = pkrPuntoEntradaDocumentos.getValue();
        if (pde!=null) {
            if (pde.getTipo()==TipoPuntoEntradaDocumentosEnum.ROSSUM){
                cargarFacturasRossum(null);
            }else{
                notifications.create().withCaption("Only for Rossum Entry Point").show();
            }

        }
    }

    public void purgarAnnotationSeleccionada(){
        User user = userSession.getUser();
        this.purgarAnnotationSeleccionada(user);
    }

    public void purgarAnnotationSeleccionada(User cubaUser){
        try {
            RossumCommonDialogs rcd = new RossumCommonDialogs();
            rcd.getAuthToken(cubaUser, "", "");
            RossumAnnotation ra = dataGridRossumAnnotations.getSingleSelected();
            Integer annotationId = ra.getAnnotationId();
            Integer queueId = ra.getQueueId();
            if (ra == null) {
                notifications.create().withDescription("Seleccionar registro").show();
                return;
            }

            boolean deleted = rcd.switchAnnotationToDeleted(annotationId);
            boolean purged = rcd.purgeDeletedAnnotations(queueId);
            notifications.create().withCaption("La anotación seleccionada fue purgada exitósamente").show();
            rossumAnnotationsDc.getMutableItems().remove(ra);
        }catch(Exception exc){
            notifications.create().withCaption("No se pudo purgar la anotación seleccionada: " + exc.getMessage()).show();;
            return;
        }
    }


    @Subscribe("btnNuevaFactura")
    public void onBtnNuevaFacturaClick(Button.ClickEvent event) {
        String email = "";
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()== TipoPuntoEntradaDocumentosEnum.MAIL){
            email = dataGridMails.getSingleSelected().getFrom().trim();
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()== TipoPuntoEntradaDocumentosEnum.STORAGE){
            email = "";
            StorageElement se = dgStorageElements.getSingleSelected();
            this.nombreDocumentoSeleccionado = se.getElementName();
            this.representacionSerialDocumentoSeleccionado = se.getRepresentacionSerial();
            this.mimeTypeDocumentoSeleccionado = se.getMimeType();
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()== TipoPuntoEntradaDocumentosEnum.CLIENTSTORAGE){
            email = "";
            StorageElement se = dgStorageElements.getSingleSelected();
            this.nombreDocumentoSeleccionado = se.getElementName();
            this.representacionSerialDocumentoSeleccionado = se.getRepresentacionSerial();
            this.mimeTypeDocumentoSeleccionado = se.getMimeType();
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()==TipoPuntoEntradaDocumentosEnum.ROSSUM){
            email = "";
            try {
                this.nombreDocumentoSeleccionado = "adjunto.pdf";
                Integer documentId = dataGridRossumAnnotations.getSingleSelected().getDocumentId();
                this.representacionSerialDocumentoSeleccionado = this.rcd.getOriginalDocumentFromDocumentId(documentId);
                this.mimeTypeDocumentoSeleccionado = dataGridRossumAnnotations.getSingleSelected().getContentType();
            }catch(Exception exc){

            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("newEntityWithAttachment", new Object[]{email, this.nombreDocumentoSeleccionado, this.representacionSerialDocumentoSeleccionado, this.mimeTypeDocumentoSeleccionado});
        MapScreenOptions mso = new MapScreenOptions(map);
        final FacturaProveedorWithAttachmentEdit fpae = (FacturaProveedorWithAttachmentEdit) screenBuilders.editor(FacturaProveedor.class, this).withScreenId("test1_FacturaProveedorWithAttachment.edit")
                .withOptions(mso).withOpenMode(OpenMode.NEW_TAB).build();
        fpae.setRossumAnnotation(dataGridRossumAnnotations.getSingleSelected());
        fpae.addAfterCloseListener(e->{
            if (pkrPuntoEntradaDocumentos.getValue().getTipo()==TipoPuntoEntradaDocumentosEnum.ROSSUM) {
                if (!fpae.hasUnsavedChanges()) {
                    dialogs.createOptionDialog().withCaption("¿Desea purgar la anotación seleccionada en Rossum?").withActions(
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e2 -> {
                                User user = userSession.getUser();
                                try {
                                    RossumCommonDialogs rcd = new RossumCommonDialogs();
                                    rcd.getAuthToken(user, "", "");
                                    RossumAnnotation ra = dataGridRossumAnnotations.getSingleSelected();
                                    Integer annotationId = ra.getAnnotationId();
                                    Integer queueId = ra.getQueueId();
                                    if (ra == null) {
                                        notifications.create().withDescription("Seleccionar registro").show();
                                        return;
                                    }

                                    boolean deleted = rcd.switchAnnotationToDeleted(annotationId);
                                    boolean purged = rcd.purgeDeletedAnnotations(queueId);

                                    if ((deleted) && (purged)) {
                                        notifications.create().withCaption("La anotación fue eliminada satisfactoriamente").show();
                                        dialogs.createOptionDialog().withCaption("¿Desea contabilizar en Contabilidad la factura creada?")
                                                .withActions(
                                                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e3 -> {
                                                            User cubaUser = userSession.getUser();
                                                            try {
                                                                FacturaProveedor fprov = fpae.getEditedEntity();
                                                                boolean res = contabiService.publicaContabilizacionFacturaProveedor(cubaUser, (FacturaProveedor) fprov);
                                                                if (res){
                                                                    notifications.create().withCaption("Factura publicada corréctamente").show();
                                                                }
                                                            } catch (Exception ee) {
                                                                notifications.create().withCaption("Error al publicar").withDescription(ee.getMessage()).show();
                                                                return;
                                                            }
                                                        })
                                                        ,new DialogAction(DialogAction.Type.NO)
                                                        )
                                                .show();
                                        rossumAnnotationsDc.getMutableItems().remove(ra);
                                        return;
                                    } else {
                                        notifications.create().withCaption("La operación de eliminación de la anotación no finalizó exitósamente. Por favor realizar comprobación manual.").show();
                                        return;
                                    }

                                } catch (Exception exc) {
                                    notifications.create().withDescription("No se pudo eliminar la anotación. Por favor proceder manualmente.").show();
                                    return;
                                }
                            }),
                            new DialogAction(DialogAction.Type.NO)
                    ).show();
                }
            }
        });

        fpae.show();

    }

    @Subscribe("pkrPuntoEntradaDocumentos")
    public void onPkrPuntoEntradaDocumentosValueChange1(HasValue.ValueChangeEvent<PuntoEntradaDocumentos> event) {
        PuntoEntradaDocumentos pde = pkrPuntoEntradaDocumentos.getValue();
        if (pde!=null){
            if (pde.getTipo() == TipoPuntoEntradaDocumentosEnum.ROSSUM){
                refreshRossum.setEnabled(true);
            }else{
                refreshRossum.setEnabled(false);
            }
        }
    }



    @Subscribe("btnNuevoPpto")
    public void onBtnNuevoPptoClick(Button.ClickEvent event) {
        String email = "";
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()== TipoPuntoEntradaDocumentosEnum.MAIL){
            email = dataGridMails.getSingleSelected().getFrom().trim();
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()== TipoPuntoEntradaDocumentosEnum.STORAGE){
            email = "";
            StorageElement se = dgStorageElements.getSingleSelected();
            this.nombreDocumentoSeleccionado = se.getElementName();
            this.representacionSerialDocumentoSeleccionado = se.getRepresentacionSerial();
            this.mimeTypeDocumentoSeleccionado = se.getMimeType();
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()== TipoPuntoEntradaDocumentosEnum.CLIENTSTORAGE){
            email = "";
            StorageElement se = dgStorageElements.getSingleSelected();
            this.nombreDocumentoSeleccionado = se.getElementName();
            this.representacionSerialDocumentoSeleccionado = se.getRepresentacionSerial();
            this.mimeTypeDocumentoSeleccionado = se.getMimeType();
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()==TipoPuntoEntradaDocumentosEnum.ROSSUM){
            email = "";
            try {
                this.nombreDocumentoSeleccionado = "adjunto.pdf";
                Integer documentId = dataGridRossumAnnotations.getSingleSelected().getDocumentId();
                this.representacionSerialDocumentoSeleccionado = this.rcd.getOriginalDocumentFromDocumentId(documentId);
                this.mimeTypeDocumentoSeleccionado = dataGridRossumAnnotations.getSingleSelected().getContentType();
            }catch(Exception exc){

            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("newEntityWithAttachment", new Object[]{email, this.nombreDocumentoSeleccionado, this.representacionSerialDocumentoSeleccionado, this.mimeTypeDocumentoSeleccionado});
        MapScreenOptions mso = new MapScreenOptions(map);

        final PresupuestoEditWithAttachment pewa = (PresupuestoEditWithAttachment) screenBuilders.editor(Presupuesto.class, this).withScreenId("test1_PresupuestoWithAttachment.edit")
                .withOptions(mso).withOpenMode(OpenMode.NEW_TAB).build();
        pewa.setRossumAnnotation(dataGridRossumAnnotations.getSingleSelected());
        pewa.addAfterCloseListener(e->{
            if (pkrPuntoEntradaDocumentos.getValue().getTipo()==TipoPuntoEntradaDocumentosEnum.ROSSUM) {
                if (!pewa.hasUnsavedChanges()) {
                    dialogs.createOptionDialog().withCaption("¿Desea purgar la anotación seleccionada en Rossum?").withActions(
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e2 -> {
                                User user = userSession.getUser();
                                try {
                                    RossumCommonDialogs rcd = new RossumCommonDialogs();
                                    rcd.getAuthToken(user, "", "");
                                    RossumAnnotation ra = dataGridRossumAnnotations.getSingleSelected();
                                    Integer annotationId = ra.getAnnotationId();
                                    Integer queueId = ra.getQueueId();
                                    if (ra == null) {
                                        notifications.create().withDescription("Seleccionar registro").show();
                                        return;
                                    }

                                    boolean deleted = rcd.switchAnnotationToDeleted(annotationId);
                                    boolean purged = rcd.purgeDeletedAnnotations(queueId);

                                    if ((deleted) && (purged)) {
                                        notifications.create().withCaption("La anotación fue eliminada satisfactoriamente").show();
                                        rossumAnnotationsDc.getMutableItems().remove(ra);
                                        return;
                                    } else {
                                        notifications.create().withCaption("La operación de eliminación de la anotación no finalizó exitósamente. Por favor realizar comprobación manual.").show();
                                        return;
                                    }

                                } catch (Exception exc) {
                                    notifications.create().withDescription("No se pudo eliminar la anotación. Por favor proceder manualmente.").show();
                                    return;
                                }
                            }),
                            new DialogAction(DialogAction.Type.NO)
                    ).show();
                }
            }
        });
        pewa.show();
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        vboxped.remove(vboxmail);
        vboxped.remove(vboxserverstorage);


    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {

        btnSubir.addClickListener(e->{
           String s = this.relativePathCurrPedStorage.substring(0,relativePathCurrPedStorage.lastIndexOf("/"));
           this.relativePathCurrPedStorage = s;

           if (this.relativePathCurrPedStorage.length()==0){
               btnSubir.setEnabled(false);
           }
           storageElementsDl.load();
           lblRelPath.setValue(this.relativePathCurrPedStorage);
        });

        dgStorageElements.addItemClickListener(e->{
            StorageElement se = e.getItem();
            if (e.isDoubleClick()){
                if (se.getIsFolder()){
                    this.relativePathCurrPedStorage += "/"+se.getElementName();
                    lblRelPath.setValue(this.relativePathCurrPedStorage);
                    storageElementsDl.load();
                    btnSubir.setEnabled(true);
                }
            }else{
                if (se.getIsFolder()==false){
                    this.previsualizaStorageElement(se);
                }
            }
        });

        List<String> l = Arrays.asList("50", "100", "150","200");
        pkrUltimosN.setOptionsList(l);
        pkrUltimosN.setValue("50");
        pkrUltimosN.addValueChangeListener(e->{
            PuntoEntradaDocumentos ped = pkrPuntoEntradaDocumentos.getValue();
            cargarCorreos(ped);
        });

        inicializaMultiUploadField();

    }

    @Subscribe("dataGridRossumAnnotations")
    public void onDataGridRossumAnnotationsItemClick(DataGrid.ItemClickEvent<RossumAnnotation> event) {


    }


    private void inicializaMultiUploadField() {
        multiUploadField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            for (Map.Entry<UUID, String> entry : multiUploadField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileDescriptor fd = fileUploadingAPI.getFileDescriptor(fileId, fileName);
                try {
                    fileUploadingAPI.putFileIntoStorage(fileId, fd);
                } catch (FileStorageException e) {
                    throw new RuntimeException("Error saving file to FileStorage", e);
                }
                dataManager.commit(fd);
                try {

                    InputStream is = fileLoader.openStream(fd);
                    byte[] bb = IOUtils.toByteArray(is);
                    StorageElement se = new StorageElement();
                    se.setRepresentacionSerial(bb);
                    se.setIsFolder(false);
                    se.setElementParentFolder("");
                    se.setElementPath(fileName);
                    se.setElementName(fileName);

                    storageElementsDc.getMutableItems().add(se);
                } catch (Exception exc) {

                }
            }
        });
    }









    @Subscribe("dataGridMails")
    public void onDataGridMailsSelection(DataGrid.SelectionEvent<MailStructure> event) {

        //reseteando el previsualizador de archivos
        brwAttachmentPreview.setSource(StreamResource.class)
                .setStreamSupplier(() -> new ByteArrayInputStream(new String("").getBytes()))
                .setMimeType("text/plain");
        this.nombreDocumentoSeleccionado = null;
        this.representacionSerialDocumentoSeleccionado = null;
        this.mimeTypeDocumentoSeleccionado = null;

        if (event.getSelected().size()==1){

            vboxAttachments.removeAll();
            MailStructure ms = (MailStructure) new ArrayList(event.getSelected()).get(0);
            try {
                ms.loadBodyParts();
            } catch (Exception e) {
                notifications.create().withCaption("Error al cargar mensaje").show();
            }
            String content = (String) ms.resolveDisplayableBodyPart();
            brwMailPreview.setSource(StreamResource.class)
                    .setStreamSupplier(() -> new ByteArrayInputStream(content.getBytes()))
                    .setMimeType("text/html");
            //attachments
            Hashtable<String, Object> attachments = ms.getOnlyAttachments();
            Iterator<String> it = attachments.keySet().iterator();
            while(it.hasNext()){
                String contentType = it.next();
                if (contentType.indexOf("name")!=-1){

                    Button l = uiComponents.create(Button.NAME);
                    l.setCaption("unnamed");
                    String mime = "";
                    String name = null;
                    try{
                        String namepart = contentType.split(";")[1];
                        name = namepart.split("=")[1];
                        name = name.trim().replace("\"","");
                        mime = contentType.split(";")[0];
                        l.setCaption(name);
                    }catch(Exception exc){

                    }
                    final String mimeContent = mime.trim();
                    final String nombreDocumento = name;
                    l.addClickListener(e->{
                        Object o = attachments.get(contentType);
                        byte[] bb_ = null;
                        if (o instanceof String){
                            bb_ = ((String)o).getBytes();
                        }else if(o instanceof ByteArrayOutputStream){
                            bb_ = ((ByteArrayOutputStream) o).toByteArray();
                        }
                        representacionSerialDocumentoSeleccionado = bb_;
                        nombreDocumentoSeleccionado = nombreDocumento;
                        mimeTypeDocumentoSeleccionado = mimeContent;
                        if ((nombreDocumento.indexOf(".pdf")!=-1) && (mimeContent.indexOf("application/octet-stream")!=-1)){
                            //realizo override para poder visualizar el documento
                            mimeTypeDocumentoSeleccionado = "application/pdf";
                        }
                        final byte[] bb2_ = bb_;
                        brwAttachmentPreview.setSource(StreamResource.class)
                                .setStreamSupplier(() -> new ByteArrayInputStream(bb2_))
                                .setMimeType(mimeTypeDocumentoSeleccionado);
                    });


                    vboxAttachments.add(l);

                }
            }
        }else{
            notifications.create().withDescription("Seleccionar un correo");
        }

    }

    private void cargarCorreos(PuntoEntradaDocumentos ped){
        currentEmailChecker = null;
        EmailChecker echk = new EmailChecker(ped);
        currentEmailChecker = echk;
        echk.connect();
        vboxped.add(vboxmail);
        vboxmail.setWidth("100%");
        vboxmail.setHeight("100%");
        vboxped.setHeightFull();
        splt2.setSplitPosition(50, SizeUnit.PERCENTAGE);
    }

    public void cargarFacturasRossum(PuntoEntradaDocumentos ped){
        vboxped.add(vbrossum);
        vbrossum.setWidth("100%");
        vbrossum.setHeight("100%");
        vboxped.setHeightFull();
        splt2.setSplitPosition(50, SizeUnit.PERCENTAGE);

        rossumAnnotationsDl.load();
    }

    @Install(to = "rossumAnnotationsDl", target = Target.DATA_LOADER)
    private List<RossumAnnotation> rossumAnnotationsDlLoadDelegate(LoadContext<RossumAnnotation> loadContext) {
        //loggeando e rossum
        User user = userSession.getUser();
        ArrayList<RossumAnnotation> invoices = new ArrayList<RossumAnnotation>();
        try{
            this.rcd = new RossumCommonDialogs();
            rcd.getAuthToken(user, "","");
            // INICIO AJUSTE QUEUES ESCISION
            //desde la escision asigno a cada usuario la cola que le pertenece mediante HARDCODE
            //rcd.getQueues();

            //ArrayList<Integer> queues =  new ArrayList(rcd.queues.keySet());
            //Integer queueId = queues.get(0);
            //Map confirmedAnnotations = rcd.getAnnotationsWithStatus(queueId, "confirmed");
            //ArrayList<Integer> annotationsIds = new ArrayList(confirmedAnnotations.keySet());

            String userLogin = user.getLogin();
            Map<Integer, Integer[]> queueAnnotationsIds = new HashMap<>();
            Integer[] queueIds = null;
            //los siguientes valores se extraen de rossum! -> seleccionar cola -> ajustes y scroll down
            if ((userLogin.compareTo("bellamateos")==0)||(userLogin.compareTo("pilarconti")==0)){
                queueIds = new Integer[]{291745};
            }
            if (userLogin.compareTo("carlosconti")==0){
                queueIds = new Integer[]{2573590,2573591,2592865};
            }
            for (int i = 0; i < queueIds.length; i++) {
                Integer queueId = queueIds[i];
                Map confirmedAnnotations = rcd.getAnnotationsWithStatus(queueId, "confirmed");
                ArrayList<Integer> queueAnnotations = new ArrayList(confirmedAnnotations.keySet());
                queueAnnotationsIds.put(queueId, queueAnnotations.toArray(new Integer[0]));
            }

            // FIN AJUSTE QUEUES


            for (int i = 0; i < queueIds.length; i++) {
                Integer queueId = queueIds[i];
                Integer[] annotationIds = queueAnnotationsIds.get(queueId);
                for (int j = 0; j < annotationIds.length; j++) {
                    Integer annotationId = annotationIds[j];
                    Map annotationExportedData = rcd.getAnnotationExportedDataAndOriginalUrl(queueId, annotationId);
                    RossumAnnotation rbi = rcd.getInvoiceStructFromMap(queueId, annotationId,annotationExportedData);
                    invoices.add(rbi);
                }

            }
        }catch(Exception exc){
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption("Error").withDescription(exc.getMessage()).show();
            int y = 2;
        }
        return invoices;
    }

    @Subscribe("dataGridRossumAnnotations")
    public void onDataGridRossumAnnotationsSelection(DataGrid.SelectionEvent<RossumAnnotation> event) {
        if (!event.isUserOriginated()){
            return;
        }
        RossumAnnotation ra = dataGridRossumAnnotations.getSingleSelected();
        if (ra==null){
            notifications.create().withDescription("Seleccionar registro").show();
            return;
        }
        try {
            byte[] bb = this.rcd.getOriginalDocumentFromDocumentId(ra.getDocumentId());
            brwAttachmentPreview.setSource(StreamResource.class)
                    .setStreamSupplier(() -> new ByteArrayInputStream(bb))
                    .setMimeType("application/pdf");
        }catch(Exception exc){
            notifications.create().withDescription("No se pudo cargar el documento").show();
            return;
        }
    }




    @Subscribe("pkrPuntoEntradaDocumentos")
    public void onPkrPuntoEntradaDocumentosValueChange(HasValue.ValueChangeEvent event) {
        PuntoEntradaDocumentos ped = (PuntoEntradaDocumentos)event.getValue();
        vboxped.removeAll();
        if (ped.getTipo()==TipoPuntoEntradaDocumentosEnum.MAIL){
            cargarCorreos(ped);

        }
        if (ped.getTipo()==TipoPuntoEntradaDocumentosEnum.STORAGE){
            storageElementsDl.load();
            vboxped.add(vboxserverstorage);
            vboxped.setHeightFull();
            botoneraClientStorage.setVisible(false);
            botoneraStorage.setVisible(true);
        }
        if (ped.getTipo()==TipoPuntoEntradaDocumentosEnum.CLIENTSTORAGE){
            storageElementsDc.getMutableItems().clear();
            vboxped.add(vboxserverstorage);
            vboxped.setHeightFull();
            botoneraClientStorage.setVisible(true);
            botoneraStorage.setVisible(false);
        }
        if (ped.getTipo()==TipoPuntoEntradaDocumentosEnum.ROSSUM){
            cargarFacturasRossum(ped);
        }
    }



    private List<StorageElement> getStorageElementsFromListOfFiles(File[] files){
        ArrayList<StorageElement> al = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            StorageElement se = new StorageElement();
            se.setIsFolder(false);
            if (f.isDirectory()){
                se.setIsFolder(true);
            }
            se.setElementName(f.getName());
            se.setElementPath(f.getAbsolutePath());
            se.setElementParentFolder(f.getParent());
            al.add(se);
        }
        return al;
    }






    public class EmailChecker{
        Properties properties = new Properties();
        JSONObject jo = null;
        Folder folder = null;
        public EmailChecker(PuntoEntradaDocumentos ped){
            String json = ped.getPropiedadesJson();
            jo = new JSONObject(json);
            JSONObject properties = (JSONObject) jo.get("properties");
            Iterator<String> keys = properties.keySet().iterator();
            while(keys.hasNext()){
                String k = keys.next();
                this.properties.put(k, properties.get(k));
            }

        }

        public void connect(){
            try {
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication((String) jo.get("user"), (String)jo.get("pwd"));
                    }
                });



                Store store = session.getStore(properties.getProperty("store"));
                store.connect((String) jo.get("mailserver"), (String) jo.get("user"), (String) jo.get("pwd"));
                Folder inbox = store.getFolder("INBOX");
                this.folder = inbox;
                inbox.open(Folder.READ_ONLY);

                loadMessages(inbox);

            }catch(Exception exc){
                notifications.create().withDescription(exc.getMessage()).show();
            }

        }

        private void loadMessages(Folder inbox) throws Exception{
            int messageCount = inbox.getMessageCount();
            int ultimosN = Integer.valueOf(pkrUltimosN.getValue());

            SearchTerm st = buildSearchTermFromCriteriaComponents();
            Message[] messages = null;
            if (st==null){
                messages = inbox.getMessages(messageCount - ultimosN, messageCount);
            }else{
                messages = inbox.search(st);
            }

            ArrayList<MailStructure> mails = new ArrayList<MailStructure>();


            for (int i = 0; i < messages.length; i++) {
                Hashtable<String, Object> bbpp = null;
                Message m = messages[i];
                String contentType = m.getContentType();
                if (contentType.indexOf("multipart")!=-1){
//                        bbpp = getAllBodyParts((MimeMultipart)m.getContent());
//                        int y = 2;
                }
                else if (contentType.indexOf("text/html")!=-1){
                    bbpp = new Hashtable<String, Object>();
                    String html = (String) m.getContent();
                    bbpp.put("text/html", html);
                }else{
                    int y = 2;
                }
                MailStructure mstruct = new MailStructure();
                String from = "";
                for (int j = 0; j < m.getFrom().length; j++) {
                    Address a = m.getFrom()[j];
                    from += a.toString() + " ";
                }
                mstruct.setFrom(from);
                mstruct.setSentDate(m.getSentDate());
                mstruct.setSubject(m.getSubject());
                mstruct.setOriginalJavaMailMessage(m);
                mails.add(mstruct);



            }
            mailsDc.setItems(mails);
        }

        private Hashtable<String,Object> getAllBodyParts(MimeMultipart mm) throws Exception{
            Hashtable<String,Object> ht = new Hashtable<>();
            ArrayList<MimeMultipart> parentBodyParts = new ArrayList();
            ArrayList<Integer> positions = new ArrayList();
            int currPos = 0;

            int size = mm.getCount();
            int bodyPartCounter = 0;
            int i = 0;
            while(true){
                for (; i < size; i++) {
                    BodyPart bp = mm.getBodyPart(i);
                    if (bp.getContent() instanceof MimeMultipart){
                        parentBodyParts.add(mm);
                        positions.add(i);
                        mm = (MimeMultipart) bp.getContent();
                        i = -1;
                        size = mm.getCount();
                    }
                    else if (bp.getContent() instanceof String){
                        ht.put(bp.getContentType(), bp.getContent());
                    }
                    else if (bp.getContent() instanceof BASE64DecoderStream){
                        BASE64DecoderStream b64ds = (BASE64DecoderStream) bp.getContent();
                        ByteArrayOutputStream bais = new ByteArrayOutputStream();
                        int b;
                        while((b = b64ds.read())!=-1){
                            bais.write(b);
                        }
                        String contentType = bp.getContentType();
                        ht.put(contentType, bais);
                    }else{
                        int y = 2;
                    }


                }
                if (parentBodyParts.size()>0){
                    try{
                        mm = (MimeMultipart) parentBodyParts.get(parentBodyParts.size()-1);
                        parentBodyParts.remove(mm);
                        i = positions.get(positions.size()-1);
                        positions.remove(positions.size()-1);
                        i++;
                        size = mm.getCount();
                    }catch(Exception exc){
                        int y = 2;
                        return null;
                    }

                }else{
                    break;
                }

            }

            return ht;
        }

    }



    @Subscribe("btnActualizarCorreos")
    public void onBtnActualizarCorreosClick(Button.ClickEvent event) {
        PuntoEntradaDocumentos ped = pkrPuntoEntradaDocumentos.getValue();
        cargarCorreos(ped);
    }





    public static void check(String host, String storeType, String user,
                             String password)
    {
        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "25");
            properties.put("mail.smtp.starttls.enable", "false");
            properties.put("mail.smtp.ssl.enable", "false");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Install(to = "storageElementsDl", target = Target.DATA_LOADER)
    private List<StorageElement> storageElementsDlLoadDelegate(LoadContext<StorageElement> loadContext) {
        PuntoEntradaDocumentos peds = pkrPuntoEntradaDocumentos.getValue();
        if (peds==null){
            return new ArrayList();
        }
        JSONObject jo = new JSONObject(peds.getPropiedadesJson());
        String rootPath = (String) jo.get("rootPath");
        Path root = Paths.get(rootPath);
        File[] files = new File(rootPath + this.relativePathCurrPedStorage).listFiles();

        List<StorageElement> l = getStorageElementsFromListOfFiles(files);
        Collections.sort(l, new Comparator<StorageElement>() {
            @Override
            public int compare(StorageElement o1, StorageElement o2) {
                if (o1.getIsFolder().compareTo(o2.getIsFolder())==0){
                    return o1.getElementName().compareTo(o2.getElementName());
                }else{
                    return -o1.getIsFolder().compareTo(o2.getIsFolder());
                }
            }
        });
        return l;
    }

    private void previsualizaStorageElement(StorageElement se){
        if (se==null){
            notifications.create().withCaption("Seleccionar un archivo").show();
            return;
        }
        if (pkrPuntoEntradaDocumentos.getValue().getTipo()==TipoPuntoEntradaDocumentosEnum.STORAGE) {
            try {
                FileInputStream fis = new FileInputStream(new File(se.getElementPath()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int b = -1;
                while ((b = fis.read()) != -1) {
                    baos.write(b);
                }
                se.setRepresentacionSerial(baos.toByteArray());
                brwAttachmentPreview.setSource(StreamResource.class)
                        .setStreamSupplier(() -> new ByteArrayInputStream(se.getRepresentacionSerial()))
                        .setMimeType(se.getMimeType());
            } catch (Exception exc) {
                notifications.create().withCaption("Error accediendo a archivo : " + se.getElementName()).show();
            }
        }else{
            try {
                brwAttachmentPreview.setSource(StreamResource.class)
                        .setStreamSupplier(() -> new ByteArrayInputStream(se.getRepresentacionSerial()))
                        .setMimeType(se.getMimeType());
            } catch (Exception exc) {
                notifications.create().withCaption("Error accediendo a archivo : " + se.getElementName()).show();
            }
        }
    }

    /*

    METODOS PARA CATEGORIA ROSSUM

     */





}