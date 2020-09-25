package com.company.test1.web.screens.cedulahabitabilidad;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.departamentos.CedulaHabitabilidad;

@UiController("test1_CedulaHabitabilidad.edit")
@UiDescriptor("cedula-habitabilidad-edit.xml")
@EditedEntityContainer("cedulaHabitabilidadDc")
@LoadDataBeforeShow
public class CedulaHabitabilidadEdit extends StandardEditor<CedulaHabitabilidad> {
}