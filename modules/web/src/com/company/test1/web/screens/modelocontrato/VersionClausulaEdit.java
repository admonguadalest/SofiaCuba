package com.company.test1.web.screens.modelocontrato;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;

@UiController("test1_VersionClausula.edit")
@UiDescriptor("version-clausula-edit.xml")
@EditedEntityContainer("versionClausulaDc")
@LoadDataBeforeShow
public class VersionClausulaEdit extends StandardEditor<VersionClausula> {
}