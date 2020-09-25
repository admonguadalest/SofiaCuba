package com.company.test1.web.screens.conceptosadicionales;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;

@UiController("test1_ProgramacionConceptoAdicional.edit")
@UiDescriptor("programacion-concepto-adicional-edit.xml")
@EditedEntityContainer("programacionConceptoAdicionalDc")
@LoadDataBeforeShow
public class ProgramacionConceptoAdicionalEdit extends StandardEditor<ProgramacionConceptoAdicional> {
}