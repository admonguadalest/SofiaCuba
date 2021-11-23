package com.company.test1.web.screens.mailinglist;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstancePropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.MailingList;

import javax.inject.Inject;

@UiController("test1_MailingList.edit")
@UiDescriptor("mailing-list-edit.xml")
@EditedEntityContainer("mailingListDc")
@LoadDataBeforeShow
public class MailingListEdit extends StandardEditor<MailingList> {
    @Inject
    private DataContext dataContext;
    @Inject
    private InstancePropertyContainer<ColeccionArchivosAdjuntos> coleccionArchivosAdjuntosDc;
    @Inject
    private InstanceContainer<MailingList> mailingListDc;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        MailingList ml =  mailingListDc.getItem();
        if (ml.getColeccionArchivosAdjuntos()==null){
            ml.setColeccionArchivosAdjuntos(dataContext.create(ColeccionArchivosAdjuntos.class));
            ml.getColeccionArchivosAdjuntos().setNombre("Documentos Mailing List");
        }
    }



}