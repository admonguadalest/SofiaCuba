package com.company.test1.web.screens.series;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Serie;

@UiController("test1_Serie.edit")
@UiDescriptor("serie-edit.xml")
@EditedEntityContainer("serieDc")
@LoadDataBeforeShow
public class SerieEdit extends StandardEditor<Serie> {
}