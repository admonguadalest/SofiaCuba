package com.company.test1.web.screens.ordencobro;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaJuridica;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_GeneracionOrdenCobroManualmenteSinRemesa")
@UiDescriptor("generacion-orden-cobro-manualmente-sin-remesa.xml")
public class GeneracionOrdenCobroManualmenteSinRemesa extends Screen {

    @Inject
    private TextField<String> conceptoField;
    @Inject
    private DateField<Date> fechaGiroField;
    @Inject
    private TextField<Double> importeField;
    @Inject
    private LookupPickerField<Persona> titularField;
    @Inject
    private DataContext dataContext;
    @Inject
    private LookupField<CuentaBancaria> cuentaField;
    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;

    @Inject
    private CollectionLoader<CuentaBancaria> cuentasDl;
    @Inject
    private LookupPickerField<Persona> creditorField;

    public void realizarOrdenCobro() throws Exception{
        if (creditorField.getValue()==null){
            notifications.create().withCaption("Datos incompletos").withDescription("Rellenar todos los campos").show();
            return;
        }
        if (titularField.getValue()==null){
            notifications.create().withCaption("Datos incompletos").withDescription("Rellenar todos los campos").show();
            return;
        }
        if (cuentaField.getValue() == null){
            notifications.create().withCaption("Datos incompletos").withDescription("Rellenar todos los campos").show();
            return;
        }
        if (importeField.getValue()==null){
            notifications.create().withCaption("Datos incompletos").withDescription("Rellenar todos los campos").show();
            return;
        }
        if (fechaGiroField.getValue()==null){
            notifications.create().withCaption("Datos incompletos").withDescription("Rellenar todos los campos").show();
            return;
        }
        if (conceptoField.getValue()==null){
            notifications.create().withCaption("Datos incompletos").withDescription("Rellenar todos los campos").show();
            return;
        }

        OrdenCobro oc = dataContext.create(OrdenCobro.class);
        oc.setDescripcion(conceptoField.getValue());
        oc.setEntToEntId("A rellenar por aplicativo al producir RC");
        oc.setFechaValor(fechaGiroField.getValue());
        oc.setImporte(importeField.getValue());
        oc.setRecibo(null);
        oc.setDeudor(titularField.getValue());
        oc.setCreditor(creditorField.getValue());
        oc.setCuentaBancariaDeudora(cuentaField.getValue());

        dataManager.commit(oc);

        notifications.create().withCaption("Orden de Cobro generada").withDescription("La Orden de Cobro se generó para los datos inseridos").show();
        return;
    }

    @Subscribe("titularField")
    public void onTitularFieldValueChange(HasValue.ValueChangeEvent<Persona> event) {

        cuentasDl.load();
    }

    @Install(to = "cuentasDl", target = Target.DATA_LOADER)
    private List<CuentaBancaria> cuentasDlLoadDelegate(LoadContext<CuentaBancaria> loadContext) {
        Persona titular = titularField.getValue();
        if (titular == null){
            return new ArrayList();
        }
        if (titular instanceof PersonaJuridica){
            titular = dataManager.reload(titular, "personaJuridica-view");
        }else{
            titular = dataManager.reload(titular, "personaFisica-view");
        }
        return titular.getCuentasBancarias();

    }




}