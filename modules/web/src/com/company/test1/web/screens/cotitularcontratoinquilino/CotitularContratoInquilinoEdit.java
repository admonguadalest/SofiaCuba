package com.company.test1.web.screens.cotitularcontratoinquilino;

import com.haulmont.cuba.gui.model.impl.CollectionContainerImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.CotitularContratoInquilino;

@UiController("test1_CotitularContratoInquilino.edit")
@UiDescriptor("cotitular-contrato-inquilino-edit.xml")
@EditedEntityContainer("cotitularContratoInquilinoDc")
@LoadDataBeforeShow
public class CotitularContratoInquilinoEdit extends StandardEditor<CotitularContratoInquilino> {

}