package com.company.test1.web.screens.contrsenas;

import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RegistroContrasena;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("test1_RegistroContrasena.browse")
@UiDescriptor("registro-contrasena-browse.xml")
@LookupComponent("registroContrasenasTable")
@LoadDataBeforeShow
public class RegistroContrasenaBrowse extends StandardLookup<RegistroContrasena> {


    @Inject
    private UserSession userSession;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataGrid<RegistroContrasena> registroContrasenasTable;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<RegistroContrasena> registroContrasenasDl;

    @Subscribe("registroContrasenasTable.edit")
    public void onRegistroContrasenasTableEdit(Action.ActionPerformedEvent event) {
        User u = userSession.getUser();
        boolean allow = false;
        if (u.getLogin().compareTo("carlosconti")==0){
            allow = true;
        }else{
            RegistroContrasena rc = registroContrasenasTable.getSingleSelected();
            if (rc.getPrivadoPublico()){
                User u2 = rc.getUsuario();
                if (u.getId().compareTo(u2.getId())==0){
                    allow = true;
                }else{
                    allow = false;
                }
            }else{
                allow = true;
            }
        }
        if (allow){
            screenBuilders.editor(RegistroContrasena.class, this)
                    .editEntity(registroContrasenasTable.getSingleSelected())
                    .withOpenMode(OpenMode.NEW_TAB)
                    .build().show();
        }else{
            notifications.create(Notifications.NotificationType.SYSTEM).withCaption("Acceso Denegado")
                    .withDescription("Solo autorizado para el usuario autor y para superadministradores")
                    .show();
        }

    }

    @Subscribe("registroContrasenasTable.remove")
    public void onRegistroContrasenasTableRemove(Action.ActionPerformedEvent event) {
        User u = userSession.getUser();
        RegistroContrasena rc = registroContrasenasTable.getSingleSelected();
        boolean allow = false;
        if (u.getLogin().compareTo("carlosconti")==0){
            allow = true;
        }else{
            allow = false;
        }
        if (allow){
            dataManager.remove(rc);
            registroContrasenasDl.load();
            notifications.create(Notifications.NotificationType.SYSTEM).withCaption("Registro Eliminado")
                    .withDescription("Registro Eliminado").show();
        }else{
            notifications.create(Notifications.NotificationType.SYSTEM).withCaption("Acceso Denegado")
                    .withDescription("Solo autorizado para el usuario autor y para superadministradores")
                    .show();
        }
    }





}