package com.company.test1.web.screens.persona;

import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.web.screens.DynamicReportHelper;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.Persona;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;

@UiController("test1_Persona.browse")
@UiDescriptor("persona-browse.xml")
@LookupComponent("personaeTable")
@LoadDataBeforeShow
public class PersonaBrowse extends StandardLookup<Persona> {


    @Inject
    private GroupTable<Persona> personaeTable;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private ExportDisplay exportDisplay;

    public void onBtnCreatePersonaFisicaClick() {

        ScreenLaunchUtil.launchNewEntityStreen(PersonaFisica.class, screenBuilders, this, OpenMode.NEW_TAB, null, null);

    }

    public void onBtnCreatePersonaJuridicaClick() {
        ScreenLaunchUtil.launchNewEntityStreen(PersonaJuridica.class, screenBuilders, this, OpenMode.NEW_TAB, null, null);
    }

    public void pdf(){
        byte[] bb = DynamicReportHelper.getReportDinamico("Listado de Personas", Persona.class, personaeTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Listado Personas.pdf");
    }
}