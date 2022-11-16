package com.company.test1.web.screens.cuentadegasto;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.cuentadegasto.CuentaDeGasto;

@UiController("test1_CuentaDeGasto.browse")
@UiDescriptor("cuenta-de-gasto-browse.xml")
@LookupComponent("cuentaDeGastoesTable")
@LoadDataBeforeShow
public class CuentaDeGastoBrowse extends StandardLookup<CuentaDeGasto> {
}