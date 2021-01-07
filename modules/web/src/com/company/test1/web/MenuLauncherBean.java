package com.company.test1.web;

import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.web.AppUI;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(MenuLauncherBean.NAME)
public class MenuLauncherBean {
    public static final String NAME = "test1_MenuLauncherBean";
    @Inject
    private ScreenBuilders screenBuilders;

    public void launchNuevoReciboIndividualizado(){
        String screenId = "test1_ReciboIndividualizado.edit";
        AppUI ui = AppUI.getCurrent();
        Window window = ui.getTopLevelWindow();
        Recibo r = new Recibo();
        if (window != null) {
            screenBuilders.editor(Recibo.class, window.getFrameOwner())
                    .editEntity(r)
                    .withScreenId(screenId)
                    .build()
                    .show();
        }
    }
}