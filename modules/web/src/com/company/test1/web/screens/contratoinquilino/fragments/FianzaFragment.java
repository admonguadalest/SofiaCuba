package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.contratosinquilinos.Fianza;
import com.company.test1.entity.documentacionesinquilinos.DocumentacionInquilino;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.documentacioninquilino.DocumentacionInquilinoBrowse;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;


import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;

@UiController("test1_FianzaFragment")
@UiDescriptor("fianza-fragment.xml")
public class FianzaFragment extends ScreenFragment {


    @Inject
    private Table<DocumentacionInquilino> tblDocumentacionesInquilinos;
    @Inject
    private InstanceContainer<Fianza> fianzaDc;
    @Inject
    private DataManager dataManager;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionLoader<DocumentacionInquilino> documentacionInquilinoesDl;
    @Inject
    private Notifications notifications;
    @Inject
    private DataContext dataContext;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        int y = 2;
    }


    public void onBtnAnadirClick() {
        Screen s = screenBuilders.lookup(DocumentacionInquilino.class, this)
                .withSelectHandler(e->{
                    Object[] oarr = e.toArray();
                    DocumentacionInquilino di = (DocumentacionInquilino) oarr[0];
                    ContratoInquilino ci = fianzaDc.getItem().getContratoInquilino();
                    di.setContratoInquilino(ci);
                    dataManager.commit(di);
                    documentacionInquilinoesDl.load();
                }).build();

        s.show();


    }

    @Install(to = "documentacionInquilinoesDl", target = Target.DATA_LOADER)
    private List<DocumentacionInquilino> documentacionInquilinoesDlLoadDelegate(LoadContext<DocumentacionInquilino> loadContext) {
        ContratoInquilino ci = fianzaDc.getItem().getContratoInquilino();
        if (ci==null){
            return new ArrayList();
        }
        String hql = "SELECT di FROM test1_DocumentacionInquilino di JOIN di.contratoInquilino ci WHERE ci.id = :cid";
        List<DocumentacionInquilino> ddii = dataManager.loadValue(hql, DocumentacionInquilino.class).parameter("cid", ci.getId()).list();
        return ddii;
    }

    public void loadDocumentacionesInquilinos(){
        documentacionInquilinoesDl.load();
    }


    public void onBtnSuprimirClick() {
        DocumentacionInquilino di = tblDocumentacionesInquilinos.getSingleSelected();
        if (di==null){
            notifications.create().withCaption("Seleccionar la Documentacion Inquilino a Suprimir");
            return;
        }
        di.setContratoInquilino(null);
        dataManager.commit(di);
        documentacionInquilinoesDl.load();
    }

    public void onBtnVerClick() {
        if (tblDocumentacionesInquilinos.getSingleSelected()==null){
            notifications.create().withCaption("Seleccioanr la Documentacion Inquilino a Visualizar");
            return;
        }
        ScreenLaunchUtil.launchEditEntityScreen(tblDocumentacionesInquilinos.getSingleSelected(), null, tblDocumentacionesInquilinos, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }
}