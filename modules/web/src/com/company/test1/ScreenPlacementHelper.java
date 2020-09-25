package com.company.test1;

import com.company.test1.web.ExtMainScreen;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.AppUI;
import org.springframework.stereotype.Component;

@Component("app_ScreenPlacementHelper")
public class ScreenPlacementHelper {

    public void showScreen(Screen screen) {
        // Get a reference to extended main screen
//        ExtMainScreen extMainScreen = (ExtMainScreen) UiControllerUtils
//                .getScreen(AppUI.getCurrent().getTopLevelWindowNN().getFrameOwner());
//
//        VBoxLayout customPanel = extMainScreen.getCustomPanel();
//
//        // Remove old content
//        customPanel.removeAll();
//        // Add new content
//        customPanel.add(screen.getWindow());
    }
}