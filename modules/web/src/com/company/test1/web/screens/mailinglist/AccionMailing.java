package com.company.test1.web.screens.mailinglist;

import com.company.test1.entity.MailingList;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.Collections;

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
    
    private void generarMailing(){
        String emails = pkrMailingList.getValue().getCommaSeparatedMailingList().replace(";",",");

        EmailInfo emailInfo = new EmailInfo("","","");
        emailInfo.setAddresses(emails);
        emailInfo.setCaption(txtSubject.getValue());
        emailInfo.setBody(rtaContent.getValue());
        emailInfo.setBodyContentType(EmailInfo.HTML_CONTENT_TYPE);
        emailInfo.setFrom("noreply@cgc-guadalest.com");

        emailService.sendEmailAsync(emailInfo);

        notifications.create().withCaption("Mailing programado exitósamente").show();
        this.closeWithDefaultAction();

    }



}