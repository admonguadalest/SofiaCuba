package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.NonPersistentConceptoRecibo;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.company.test1.service.ProgramacionReciboService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.ConceptoReciboEdit;
import com.company.test1.web.screens.contratoinquilino.conceptorecibo.NonPersistentConceptoReciboEdit;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("test1_ProgramacionReciboFragment")
@UiDescriptor("programacion-recibo-fragment.xml")
public class ProgramacionReciboFragment extends ScreenFragment {
    @Inject
    private InstanceContainer<ProgramacionRecibo> programacionReciboDc;
    @Inject
    private CollectionContainer<NonPersistentConceptoRecibo> nonPersistentConceptoReciboesDc;
    @Inject
    private CollectionPropertyContainer<ConceptoRecibo> conceptosReciboDc;
    @Inject
    private Table<NonPersistentConceptoRecibo> tableConceptosRecibo;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionLoader<DefinicionRemesa> definicionesRemesaDl;
    @Inject
    private ProgramacionReciboService programacionReciboService;
    @Inject
    private Notifications notifications;


    @Inject
    private CollectionLoader<NonPersistentConceptoRecibo> nonPersistentConceptoReciboesDl;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        definicionesRemesaDl.load();
    }


    @Subscribe(target = Target.PARENT_CONTROLLER)
    private void onAfterShow(Screen.AfterShowEvent event) {
        updateNonPersistentConceptosRecibosTable(null);
    }
    
    private void updateNonPersistentConceptosRecibosTable(Screen.AfterCloseEvent acl){
        try {
            if (acl!=null){
                NonPersistentConceptoReciboEdit cre = (NonPersistentConceptoReciboEdit) acl.getScreen();
                int y = 2;
            }
            List<NonPersistentConceptoRecibo> l = programacionReciboService.resumeConceptosRecibo(programacionReciboDc.getItem());
            nonPersistentConceptoReciboesDc.getMutableItems().clear();
            nonPersistentConceptoReciboesDc.getMutableItems().addAll(l);
        }catch(Exception exc){
            notifications.create().withType(Notifications.NotificationType.ERROR).withCaption("Error al actualizar conceptos").withDescription(exc.getMessage());
        }
    }



    @Subscribe("tableConceptosRecibo.create")
    private void onTableConceptosReciboCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(ConceptoRecibo.class, null, null, screenBuilders, this,
//                OpenMode.DIALOG, dataContext,
//                s->{
//                    ConceptoReciboEdit cre = (ConceptoReciboEdit) s;
//                    ConceptoRecibo cr = cre.getEditedEntity();
//                    cr.setProgramacionRecibo(programacionReciboDc.getItem());
//                    conceptosReciboDc.getMutableItems().add(cr);
//                    updateNonPersistentConceptosRecibosTable();
//                });
        Screen s = screenBuilders.editor(NonPersistentConceptoRecibo.class, this).newEntity().withListComponent(tableConceptosRecibo)
                .withParentDataContext(dataContext)
                .withLaunchMode(OpenMode.DIALOG)
                .withInitializer(npcr->{}).build();
        NonPersistentConceptoReciboEdit npcre = (NonPersistentConceptoReciboEdit) s;
        npcre.setProgramacionReciboDc(programacionReciboDc);

        s.addAfterCloseListener(acl->{
            updateNonPersistentConceptosRecibosTable(acl);
        });

        s.show();
    }

    @Subscribe("tableConceptosRecibo.edit")
    private void onTableConceptosReciboEdit(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchEditEntityScreen(tableConceptosRecibo.getSingleSelected(), null, tableConceptosRecibo, screenBuilders, this,
//                OpenMode.DIALOG, dataContext, null,
//                sab->{
//                    NonPersistentConceptoReciboEdit npcre = (NonPersistentConceptoReciboEdit) sab;
//                    npcre.setProgramacionReciboDc(programacionReciboDc);
//                    npcre.setConceptosAdicionalesDc(conceptosReciboDc);
//                });
        NonPersistentConceptoRecibo npcr = tableConceptosRecibo.getSingleSelected();
        if (npcr == null){
            notifications.create().withCaption("Seleccionar un Concepto de la Programacion a editar").show();
            return;
        }
        Screen s = screenBuilders.editor(NonPersistentConceptoRecibo.class, this).withListComponent(tableConceptosRecibo)
                .editEntity(npcr)
                .withParentDataContext(dataContext)
                .withLaunchMode(OpenMode.DIALOG)
                .build();
        NonPersistentConceptoReciboEdit npcre = (NonPersistentConceptoReciboEdit) s;
        npcre.setProgramacionReciboDc(programacionReciboDc);
        npcre.setConceptosRecibosDc(conceptosReciboDc);

        s.addAfterCloseListener(acl->{updateNonPersistentConceptosRecibosTable(null);});
        s.show();
    }

//    @Install(to = "nonPersistentConceptoReciboesDl", target = Target.DATA_LOADER)
//    private List<NonPersistentConceptoRecibo> nonPersistentConceptoReciboesDlLoadDelegate(LoadContext<NonPersistentConceptoRecibo> loadContext) {
//        return null;
//    }


    /* De momento estos metodos los coloco aqui*/
    /*
    No me gusta nada pero tengo el problema que si altero la lista de conceptos recibo, debido a que pongo una vision agregada de los conceptosrecibos
    a traves de la entidad no-persistence NonPersistentConceptoRecibo, si el poblado de la tabla de esta pagina lo hago via servicio, la alteracion
    no se ve reflejada
     */
    
    
    
    
}