package com.company.test1.web.screens.conceptosadicionales;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;

@UiController("test1_ConceptoAdicional.browse")
@UiDescriptor("concepto-adicional-browse.xml")
@LookupComponent("conceptoAdicionalsTable")
@LoadDataBeforeShow
public class ConceptoAdicionalBrowse extends StandardLookup<ConceptoAdicional> {
}