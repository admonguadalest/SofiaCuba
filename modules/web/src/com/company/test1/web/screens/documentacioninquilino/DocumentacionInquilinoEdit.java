package com.company.test1.web.screens.documentacioninquilino;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentacionesinquilinos.DocumentacionInquilino;

import javax.inject.Inject;
import java.util.Date;

@UiController("test1_DocumentacionInquilino.edit")
@UiDescriptor("documentacion-inquilino-edit.xml")
@EditedEntityContainer("documentacionInquilinoDc")
@LoadDataBeforeShow
public class DocumentacionInquilinoEdit extends StandardEditor<DocumentacionInquilino> {

    @Inject
    private InstanceContainer<DocumentacionInquilino> documentacionInquilinoDc;
    @Inject
    private DataContext dataContext;

    @Subscribe
    private void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        this.getEditedEntity().setFechaRegistro(new Date());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (documentacionInquilinoDc.getItem().getColeccionArchivosAdjuntos()==null){
            ColeccionArchivosAdjuntos caa = dataContext.create(ColeccionArchivosAdjuntos.class);
            caa.setNombre("Documentos");
            documentacionInquilinoDc.getItem().setColeccionArchivosAdjuntos(caa);
        }
    }
    
    
    
}