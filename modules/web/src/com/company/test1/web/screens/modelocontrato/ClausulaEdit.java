package com.company.test1.web.screens.modelocontrato;

import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;
import com.company.test1.service.ModeloContratoInquilinoService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.RichTextArea;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.modeloscontratosinquilinos.Clausula;

import javax.inject.Inject;

@UiController("test1_Clausula.edit")
@UiDescriptor("clausula-edit.xml")
@EditedEntityContainer("clausulaDc")
@LoadDataBeforeShow
public class
ClausulaEdit extends StandardEditor<Clausula> {


    @Inject
    private Table<VersionClausula> tableVersiones;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private InstanceContainer<Clausula> clausulaDc;
    @Inject
    private ModeloContratoInquilinoService modeloContratoInquilinoService;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        Clausula c = clausulaDc.getItem();
        if (c.getOrdenacion()==null){
            Integer i = modeloContratoInquilinoService.getValorOrdenacionNuevaClausula(c.getSeccion());
            c.setOrdenacion(i);
        }
    }
    
    

    @Subscribe("tableVersiones.create")
    private void onTableVersionesClausulasCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(VersionClausula.class, null, tableVersiones, screenBuilders, this, OpenMode.DIALOG, dataContext, null, null);

    }

    @Subscribe("tableVersiones.edit")
    private void onTableVersionesClausulasEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableVersiones.getSingleSelected(), null, tableVersiones, screenBuilders, this, OpenMode.DIALOG, dataContext, null);
    }

    public RichTextArea getTextoVersionColumn(VersionClausula vc){
        RichTextArea rta = uiComponents.create(RichTextArea.NAME);
        rta.setValue(vc.getTextoVersion());
        rta.setEditable(false);
        rta.setWidth("100%");
        rta.setStyleName("textAreaInTableCell");
        return rta;
    }
}