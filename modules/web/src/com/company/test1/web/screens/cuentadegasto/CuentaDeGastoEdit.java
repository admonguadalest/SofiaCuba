package com.company.test1.web.screens.cuentadegasto;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.cuentadegasto.CuentaDeGasto;

@UiController("test1_CuentaDeGasto.edit")
@UiDescriptor("cuenta-de-gasto-edit.xml")
@EditedEntityContainer("cuentaDeGastoDc")
@LoadDataBeforeShow
public class CuentaDeGastoEdit extends StandardEditor<CuentaDeGasto> {
}