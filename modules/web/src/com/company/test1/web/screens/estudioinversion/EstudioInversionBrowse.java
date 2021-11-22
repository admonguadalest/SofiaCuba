package com.company.test1.web.screens.estudioinversion;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.EstudioInversion;

@UiController("test1_Inversion.browse")
@UiDescriptor("estudio-inversion-browse.xml")
@LookupComponent("estudioInversionsTable")
@LoadDataBeforeShow
public class EstudioInversionBrowse extends StandardLookup<EstudioInversion> {
}