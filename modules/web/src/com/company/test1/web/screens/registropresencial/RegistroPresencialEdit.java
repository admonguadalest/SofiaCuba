package com.company.test1.web.screens.registropresencial;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RegistroPresencial;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Date;

@UiController("test1_RegistroPresencial.edit")
@UiDescriptor("registro-presencial-edit.xml")
@EditedEntityContainer("registroPresencialDc")
@LoadDataBeforeShow
public class RegistroPresencialEdit extends StandardEditor<RegistroPresencial> {
    @Inject
    private UserSession userSession;

    @Subscribe
    private void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        User u = userSession.getUser();
        this.getEditedEntity().setUsuario(u);
        this.getEditedEntity().setTimestamp(new Date());
        this.getEditedEntity().setAccion("FICHARDESDESOFIA");

    }
    
}