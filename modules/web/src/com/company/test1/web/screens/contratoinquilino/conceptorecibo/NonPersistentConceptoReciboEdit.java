package com.company.test1.web.screens.contratoinquilino.conceptorecibo;

import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.NonPersistentConceptoRecibo;

import javax.inject.Inject;

@UiController("test1_NonPersistentConceptoRecibo.edit")
@UiDescriptor("non-persistent-concepto-recibo-edit.xml")
@EditedEntityContainer("nonPersistentConceptoReciboDc")
@LoadDataBeforeShow
public class NonPersistentConceptoReciboEdit extends StandardEditor<NonPersistentConceptoRecibo> {
    InstanceContainer<ProgramacionRecibo> programacionReciboDc = null;

    @Inject
    private InstanceContainer<NonPersistentConceptoRecibo> nonPersistentConceptoReciboDc;
    @Inject
    private CollectionContainer<ConceptoRecibo> conceptosRecibosDc;
    @Inject
    private Table<ConceptoRecibo> tableConceptosRecibo;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;

    @Subscribe("tableConceptosRecibo.create")
    private void onTableConceptosReciboCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(ConceptoRecibo.class, null, tableConceptosRecibo, screenBuilders, this,
                OpenMode.DIALOG, dataContext,
                s->{
                    ConceptoReciboEdit cre = (ConceptoReciboEdit) s;
                    ConceptoRecibo cr = cre.getEditedEntity();
                    cr.setProgramacionRecibo(programacionReciboDc.getItem());
                    dataContext.merge(cr);
                    conceptosRecibosDc.getMutableItems().add(cr);
                    programacionReciboDc.getItem().getConceptosRecibo().add(cr);
                    dataContext.merge(programacionReciboDc.getItem());



                });
    }

    @Subscribe("tableConceptosRecibo.edit")
    private void onTableConceptosReciboEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableConceptosRecibo.getSingleSelected(), null, tableConceptosRecibo, screenBuilders, this,
                OpenMode.DIALOG, dataContext, null);
    }


    public InstanceContainer<ProgramacionRecibo> getProgramacionReciboDc() {
        return programacionReciboDc;
    }

    public void setProgramacionReciboDc(InstanceContainer<ProgramacionRecibo> programacionReciboDc) {
        this.programacionReciboDc = programacionReciboDc;
    }

    public CollectionContainer<ConceptoRecibo> getConceptosRecibosDc() {
        return conceptosRecibosDc;
    }

    public void setConceptosRecibosDc(CollectionContainer<ConceptoRecibo> conceptosRecibosDc) {
        this.conceptosRecibosDc = conceptosRecibosDc;
    }
}