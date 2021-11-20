package com.company.test1.web.screens.oferta;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.service.CicloService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.Oferta;

import javax.inject.Inject;

@UiController("test1_Oferta.browse")
@UiDescriptor("oferta-browse.xml")
@LookupComponent("ofertasTable")
@LoadDataBeforeShow
public class OfertaBrowse extends StandardLookup<Oferta> {

    @Inject
    private GroupTable<Oferta> ofertasTable;
    @Inject
    private Notifications notifications;
    @Inject
    private CicloService cicloService;
    @Inject
    private ScreenBuilders screenBuilders;

    public void verCicloDeOferta(){
        Oferta o = ofertasTable.getSingleSelected();
        if (o!=null){
            try {
                Ciclo c = cicloService.devuelveCicloActivoOperativoDeDepartamento(o.getDepartamento());

                screenBuilders.editor(Ciclo.class, this).editEntity(c).withOpenMode(OpenMode.NEW_TAB)
                        .build().show();
            } catch (Exception e) {
                notifications.create().withCaption(e.getMessage());
            }
        }else{
            notifications.create().withCaption("Seleccionar una oferta").show();
        }
    }
}