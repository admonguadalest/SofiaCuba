package com.company.test1.web.screens.mailinglist;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.UrlRouting;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.Base64;
import java.util.Map;

@UiController("test1_Unsubscribe")
@UiDescriptor("unsubscribe.xml")
@Route(path="unsubscribe")
public class Unsubscribe extends Screen {
    @Inject
    private TextField<String> txtemail;
    @Inject
    private Button unsubscribeBtn;
    @Inject
    private Notifications notifications;
    @Inject
    private UrlRouting urlRouting;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        Map params = urlRouting.getState().getParams();
        if (params.containsKey("mail")){
            txtemail.setValue((String) params.get("mail"));
        }
        unsubscribeBtn.addClickListener(e->{
            notifications.create().withCaption("Boton clickado!").show();
            String s = Base64.getEncoder().encodeToString("carloscz25@gmail.com".getBytes());
            byte[] bb = Base64.getDecoder().decode(s);
            String s2 = new String(bb);
            int y = 2;
        });
    }


}