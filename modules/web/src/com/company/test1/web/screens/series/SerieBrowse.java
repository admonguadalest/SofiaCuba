package com.company.test1.web.screens.series;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Serie;

@UiController("test1_Serie.browse")
@UiDescriptor("serie-browse.xml")
@LookupComponent("seriesTable")
@LoadDataBeforeShow
public class SerieBrowse extends StandardLookup<Serie> {
}