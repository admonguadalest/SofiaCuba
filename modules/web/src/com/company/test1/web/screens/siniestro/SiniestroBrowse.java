package com.company.test1.web.screens.siniestro;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.Siniestro;

@UiController("test1_Siniestro.browse")
@UiDescriptor("siniestro-browse.xml")
@LookupComponent("siniestroesTable")
@LoadDataBeforeShow
public class SiniestroBrowse extends StandardLookup<Siniestro> {
}