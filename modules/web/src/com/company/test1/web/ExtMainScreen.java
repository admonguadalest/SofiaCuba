package com.company.test1.web;

import com.company.test1.ScreenPlacementHelper;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.widgets.CubaHorizontalSplitPanel;
import com.vaadin.server.Sizeable;

import javax.inject.Inject;


@UiController("topMenuMainScreen")
@UiDescriptor("ext-main-screen.xml")
public class ExtMainScreen extends MainScreen {
//    @Inject
//    private SplitPanel foldersSplit;
////    @Inject
//    private FoldersPane foldersPane;
    @Inject
    private AppWorkArea workArea;
    @Inject
    private WebConfig webConfig;
    @Inject
    private AppMenu mainMenu;
    //    @Inject
//    private VBoxLayout customPanel;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Screens screens;
    @Inject
    private ScreenPlacementHelper screenPlacementHelper;

//    public VBoxLayout getCustomPanel() {
//        return customPanel;
//    }

    @Subscribe
    private void onAfterShow1(AfterShowEvent event) {

        //test mostrar pantalla
//        Screen s = screens.create(Testextmain.class);
//        screenPlacementHelper.showScreen(s);

        
    }



    public ExtMainScreen() {
        addInitListener(this::initLayout);
    }

    protected void initLayout(@SuppressWarnings("unused") InitEvent event) {
//        if (webConfig.getFoldersPaneEnabled()) {
//            if (webConfig.getFoldersPaneVisibleByDefault()) {
//                foldersSplit.setSplitPosition(webConfig.getFoldersPaneDefaultWidth(), SizeUnit.PIXELS);
//            } else {
//                foldersSplit.setSplitPosition(0);
//            }
//            CubaHorizontalSplitPanel vSplitPanel = (CubaHorizontalSplitPanel) WebComponentsHelper.unwrap(foldersSplit);
//            vSplitPanel.setDefaultPosition(webConfig.getFoldersPaneDefaultWidth() + "px");
//            vSplitPanel.setMaxSplitPosition(50, Sizeable.Unit.PERCENTAGE);
//            vSplitPanel.setDockable(true);
//        } else {
////            foldersPane.setEnabled(false);
////            foldersPane.setVisible(false);
//            foldersSplit.remove(workArea);
//            int foldersSplitIndex = getWindow().indexOf(foldersSplit);
//            getWindow().remove(foldersSplit);
//            getWindow().add(workArea, foldersSplitIndex);
//            getWindow().expand(workArea);
//        }
    }
}