package com.company.test1.web.screens.asignaciontarea;

import com.company.test1.entity.ciclos.NotaIntervencion;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.AsignacionTarea;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UiController("test1_AsignacionTarea.edit-ext")
@UiDescriptor("asignacion-tarea-edit-ext.xml")
@EditedEntityContainer("asignacionTareaDc")
@LoadDataBeforeShow
public class AsignacionTareaEditExt extends StandardEditor<AsignacionTarea> {
    @Inject
    private CollectionLoader<NotaIntervencion> notasIntervencionDl;
    @Inject
    private DataManager dataManager;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        notasIntervencionDl.load();
    }



    @Install(to = "notasIntervencionDl", target = Target.DATA_LOADER)
    private List<NotaIntervencion> notasIntervencionDlLoadDelegate(LoadContext<NotaIntervencion> loadContext) {
        AsignacionTarea at = this.getEditedEntity();
        return at.getOrdenTrabajo().getNotasIntervenciones();
    }

    @Subscribe(id = "notasIntervencionDc", target = Target.DATA_CONTAINER)
    public void onNotasIntervencionDcCollectionChange(CollectionContainer.CollectionChangeEvent<NotaIntervencion> event) {
        if (event.getChangeType() == CollectionChangeType.ADD_ITEMS){
            ArrayList al = new ArrayList(event.getChanges());
            for (int i = 0; i < al.size(); i++) {
                NotaIntervencion ni = (NotaIntervencion) al.get(i);
                ni.setOrdenTrabajo(this.getEditedEntity().getOrdenTrabajo());
                dataManager.commit(ni);
            }
        }
    }





}