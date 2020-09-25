package com.company.test1.web.screens.asignaciontarea;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ciclos.AsignacionTarea;

@UiController("test1_AsignacionTarea.edit")
@UiDescriptor("asignacion-tarea-edit.xml")
@EditedEntityContainer("asignacionTareaDc")
@LoadDataBeforeShow
public class AsignacionTareaEdit extends StandardEditor<AsignacionTarea> {



}