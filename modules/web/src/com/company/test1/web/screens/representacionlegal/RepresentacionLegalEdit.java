package com.company.test1.web.screens.representacionlegal;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.RepresentacionLegal;

@UiController("test1_RepresentacionLegal.edit")
@UiDescriptor("representacion-legal-edit.xml")
@EditedEntityContainer("representacionLegalDc")
@LoadDataBeforeShow
public class RepresentacionLegalEdit extends StandardEditor<RepresentacionLegal> {

}