package com.company.test1.web.screens.asignaciontarea;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.AsignacionTarea;

@UiController("test1_AsignacionTarea.edit-ext")
@UiDescriptor("asignacion-tarea-edit-ext.xml")
@EditedEntityContainer("asignacionTareaDc")
@LoadDataBeforeShow
public class AsignacionTareaEditExt extends StandardEditor<AsignacionTarea> {
}