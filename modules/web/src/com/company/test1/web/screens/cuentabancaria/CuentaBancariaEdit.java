package com.company.test1.web.screens.cuentabancaria;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.UiComponentsGenerator;
import com.haulmont.cuba.gui.model.impl.DataContextImpl;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.CuentaBancaria;
import com.haulmont.cuba.web.gui.components.WebTextField;
import com.haulmont.cuba.web.widgets.CubaTextField;

import javax.inject.Inject;

@UiController("test1_CuentaBancaria.edit")
@UiDescriptor("cuenta-bancaria-edit.xml")
@EditedEntityContainer("cuentaBancariaDc")
@LoadDataBeforeShow
public class CuentaBancariaEdit extends StandardEditor<CuentaBancaria> {

    @Inject
    private TextField<String> codigoBICField;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private UiComponentsGenerator uiComponentsGenerator;
    @Inject
    private TextField<String> txtFormatoIban;
    @Inject
    private TextField<String> txtFormatoTradicional;
    @Inject
    private TextField<String> paisField;
    @Inject
    private TextField<String> digigosControlIBANField;
    @Inject
    private TextField<String> entidadField;
    @Inject
    private TextField<String> oficinaField;
    @Inject
    private TextField<String> digitosControlField;
    @Inject
    private TextField<String> numeroCuentaField;
    @Inject
    private Notifications notifications;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        WebTextField<String> wtf = (WebTextField<String>)codigoBICField;
        CubaTextField ctf = (CubaTextField) wtf.getComposition();

        int y = 2;
    }

    @Subscribe("codigoBICField")
    public void onCodigoBICFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("digigosControlIBANField")
    public void onDigigosControlIBANFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("digitosControlField")
    public void onDigitosControlFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("domicilioEntidadBancariaField")
    public void onDomicilioEntidadBancariaFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("entidadField")
    public void onEntidadFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("infoContactoOficinaField")
    public void onInfoContactoOficinaFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("nombreEntidadBancariaField")
    public void onNombreEntidadBancariaFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("numeroCuentaField")
    public void onNumeroCuentaFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("oficinaField")
    public void onOficinaFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }

    @Subscribe("paisField")
    public void onPaisFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        updateCuentasBancarias();
    }


    
    private void updateCuentasBancarias(){
        try {
            String pais = paisField.getValue();
            if (pais == null) pais = "[PAIS]";
            String dcIban = digigosControlIBANField.getValue();
            if (dcIban == null) dcIban = "[DCIBAN]";
            String entidad = entidadField.getValue();
            if (entidad == null) entidad = "[ENTIDAD]";
            String oficina = oficinaField.getValue();
            if (oficina == null) oficina = "[OFICINA]";
            String dc = digitosControlField.getValue();
            if (dc == null) dc = "[DC]";
            String ncta = numeroCuentaField.getValue();
            if (ncta == null) ncta = "[____NCTA____]";
            String agregadoIban = pais + dcIban + " " + entidad + " " + oficina + " " + dc + ncta.substring(0, 2) + " " + ncta.substring(2, 6) + " " + ncta.substring(6, 10);
            String agregado = entidad + " " + oficina + " " + dc + " " + ncta;
            txtFormatoTradicional.setValue(agregado);
            txtFormatoIban.setValue(agregadoIban);
        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription("No se pudo reconstruir el numero de cuenta: por favor verifique los valores son validos.").show();
        }

    }
}