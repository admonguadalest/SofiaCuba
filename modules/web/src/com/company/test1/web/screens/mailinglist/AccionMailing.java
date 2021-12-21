package com.company.test1.web.screens.mailinglist;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.MailingList;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.web.screens.coleccionarchivosadjuntos.ColeccionArchivosAdjuntosFragment;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import javax.validation.constraints.Email;
import java.lang.reflect.Array;
import java.util.*;

@UiController("test1_AccionMailing")
@UiDescriptor("accion-mailing.xml")
public class AccionMailing extends Screen {
    @Inject
    private Button btnCerrar;
    @Inject
    private Button btnGenerar;
    @Inject
    private Label<String> lblmails;
    @Inject
    private PickerField<MailingList> pkrMailingList;
    @Inject
    private PickerField<Plantilla> pkrPlantilla;
    @Inject
    private RichTextArea rtaContent;
    @Inject
    private TextField<String> txtSubject;
    @Inject
    private EmailService emailService;
    @Inject
    private Notifications notifications;
    @Inject
    private InstanceContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;
    @Inject
    private ColeccionArchivosAdjuntosFragment coleccionArchivosAdjuntosFragment;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        btnGenerar.addClickListener(e->{

        });
        pkrPlantilla.addValueChangeListener(e->{
           Plantilla p = e.getValue();
           rtaContent.setValue(p.getContenidoPlantilla());
        });
        pkrMailingList.addValueChangeListener(e->{
            lblmails.setValue(e.getValue().getCommaSeparatedMailingList());
        });
        
        btnGenerar.addClickListener(e->{
            generarMailing();
        });

        btnCerrar.addClickListener(e->{
            this.closeWithDefaultAction();
        });
    }

    InstanceContainer<ColeccionArchivosAdjuntos> instanceContainer = null;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        instanceContainer = coleccionArchivosAdjuntosFragment.getColeccionArchivosAdjuntosDc();
        coleccionArchivosAdjuntosDc.setItem(new ColeccionArchivosAdjuntos());
        coleccionArchivosAdjuntosDc.getItem().setNombre("Adjuntos");
    }


    
    private void generarMailing(){
        String emails = pkrMailingList.getValue().getCommaSeparatedMailingList().replace(";",",");


        EmailInfo emailInfo = new EmailInfo("","","");
        emailInfo.setAddresses(emails);
        emailInfo.setCaption(txtSubject.getValue());
        emailInfo.setBody(rtaContent.getValue());
        emailInfo.setBodyContentType(EmailInfo.HTML_CONTENT_TYPE);
        emailInfo.setFrom("noreply@cgc-guadalest.com");

        //email attachments
        List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
        List<ArchivoAdjunto> aas = coleccionArchivosAdjuntosFragment.getArchivosAdjuntosDc().getItems();

        for (int i = 0; i < aas.size(); i++) {
            ArchivoAdjunto aa = aas.get(i);
            byte[] bytes = aa.getRepresentacionSerial();
            bytes = Base64.getMimeDecoder().decode(bytes);
            bytes = Base64.getMimeDecoder().decode(bytes);
            EmailAttachment emailAttachment = new EmailAttachment(bytes, aa.getNombreArchivo());

            attachments.add(emailAttachment);
        }
        emailInfo.setAttachments(attachments.toArray(new EmailAttachment[0]));
        emailService.sendEmailAsync(emailInfo);

        notifications.create().withCaption("Mailing programado exitósamente").show();
        this.closeWithDefaultAction();

    }



}