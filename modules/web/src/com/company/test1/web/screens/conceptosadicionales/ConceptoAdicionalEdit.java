package com.company.test1.web.screens.conceptosadicionales;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;

@UiController("test1_ConceptoAdicional.edit")
@UiDescriptor("concepto-adicional-edit.xml")
@EditedEntityContainer("conceptoAdicionalDc")
@LoadDataBeforeShow
public class ConceptoAdicionalEdit extends StandardEditor<ConceptoAdicional> {
}