package com.company.test1.web.screens.cuentabancaria;

import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.CuentaBancaria;

@UiController("test1_CuentaBancaria.browse")
@UiDescriptor("cuenta-bancaria-browse.xml")
@LookupComponent("cuentaBancariasTable")
@LoadDataBeforeShow
public class CuentaBancariaBrowse extends StandardLookup<CuentaBancaria> {
}