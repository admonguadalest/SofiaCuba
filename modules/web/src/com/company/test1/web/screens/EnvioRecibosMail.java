package com.company.test1.web.screens;

import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("test1_EnvioRecibosMail")
@UiDescriptor("envio-recibos-mail.xml")
public class EnvioRecibosMail extends Screen {

    public String emails = "";
    @Inject
    private TextField<String> txtEmails;

    public void onBtnEnviar(){
        this.close(WINDOW_COMMIT_AND_CLOSE_ACTION);
    }

    public void onBtnCancelar(){
        this.close(WINDOW_DISCARD_AND_CLOSE_ACTION);
    }

    @Subscribe
    public void onBeforeClose(BeforeCloseEvent event) {
        this.emails = txtEmails.getValue();
    }

    public void preDefineMails(String mails){
        txtEmails.setValue(mails);
    }



    public String getEmails(){
        return this.emails;
    }

}