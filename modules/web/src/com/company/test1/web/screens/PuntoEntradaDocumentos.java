package com.company.test1.web.screens;

import com.company.test1.entity.MailStructure;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.sun.mail.util.BASE64DecoderStream;
import org.codehaus.jackson.annotate.JsonSubTypes;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@UiController("test1_PuntoEntradaDocumentos")
@UiDescriptor("punto-entrada-documentos.xml")
public class PuntoEntradaDocumentos extends Screen {





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
    private Button btnNuevaFactura;

    public String nombreDocumentoSeleccionado = null;
    public byte[] representacionSerialDocumentoSeleccionado = null;
    public String mimeTypeDocumentoSeleccionado = null;

    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("btnNuevaFactura")
    public void onBtnNuevaFacturaClick(Button.ClickEvent event) {
        String email = dataGridMails.getSingleSelected().getFrom().trim();
        HashMap<String, Object> map = new HashMap<>();
        map.put("newEntityWithAttachment", new Object[]{email, this.nombreDocumentoSeleccionado, this.representacionSerialDocumentoSeleccionado, this.mimeTypeDocumentoSeleccionado});
        MapScreenOptions mso = new MapScreenOptions(map);
        screenBuilders.editor(FacturaProveedor.class, this).withScreenId("test1_FacturaProveedorWithAttachment.edit")
                .withOptions(mso).withOpenMode(OpenMode.NEW_TAB).build().show();

    }

    @Subscribe("btnNuevoPpto")
    public void onBtnNuevoPptoClick(Button.ClickEvent event) {

    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        EmailChecker checker = new EmailChecker();
        checker.connect();

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
                        final byte[] bb2_ = bb_;
                        brwAttachmentPreview.setSource(StreamResource.class)
                                .setStreamSupplier(() -> new ByteArrayInputStream(bb2_))
                                .setMimeType(mimeContent);
                    });


                    vboxAttachments.add(l);

                }
            }
        }else{
            notifications.create().withDescription("Seleccionar un correo");
        }

    }




    public class EmailChecker{
        Properties properties = new Properties();
        public EmailChecker(){
            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.starttls.enable", "false");
            properties.put("mail.smtp.host", "mail.cgc-guadalest.com");
            properties.put("mail.smtp.port", "25");
            properties.put("mail.smtp.ssl.enable", "false");

        }

        public void connect(){
            try {
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("info@cgc-guadalest.com", "r21613a");
                    }
                });


                //store = session.getStore("imaps");
                Store store = session.getStore("pop3");
                store.connect("mail.cgc-guadalest.com", "info@cgc-guadalest.com", "r21613a");
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                Message[] messages = inbox.getMessages();
                ArrayList<MailStructure> mails = new ArrayList<MailStructure>();

                for (int i = messages.length-50; i < messages.length; i++) {
                    Hashtable<String, Object> bbpp = null;
                    Message m = messages[i];
                    String contentType = m.getContentType();
                    if (contentType.indexOf("multipart")!=-1){
                        bbpp = getAllBodyParts((MimeMultipart)m.getContent());
                        int y = 2;
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
                    mstruct.setBodyParts(bbpp);
                    mails.add(mstruct);



                }
                mailsDc.setItems(mails);
            }catch(Exception exc){
                notifications.create().withDescription(exc.getMessage()).show();
            }

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
        consultaCorreo();
    }



    private void consultaCorreo(){
        String host = "mail.cgc-guadalest.com";// change accordingly
        String mailStoreType = "pop3";
        String username = "info@cgc-guadalest.com";// change accordingly
        String password = "r21613a";// change accordingly

        check(host, mailStoreType, username, password);


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


}