package com.company.test1.web.screens.ordencobro;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.ordenescobro.RealizacionCobro;
import com.company.test1.service.OrdenCobroService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenescobro.OrdenCobro;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_OrdenCobroSinRemesa.browse")
@UiDescriptor("orden-cobro-browse-sin-remesa.xml")
@LookupComponent("ordenCobroesTable")
@LoadDataBeforeShow
public class OrdenCobroBrowseSinRemesa extends StandardLookup<OrdenCobro> {
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<CuentaBancaria> cuentaBancariasDl;
    @Inject
    private CollectionLoader<OrdenCobro> ordenCobroesDl;
    @Inject
    private Notifications notifications;
    @Inject
    private OrdenCobroService ordenCobroService;
    @Inject
    private Filter filter;
    @Inject
    private GroupTable<OrdenCobro> ordenCobroesTable;
    @Inject
    private LookupField<Propietario> lkpPresentador;
    @Inject
    private LookupField<CuentaBancaria> lkpCB;

    @Install(to = "ordenCobroesDl", target = Target.DATA_LOADER)
    private List<OrdenCobro> ordenCobroesDlLoadDelegate(LoadContext<OrdenCobro> loadContext) {
//        String hql = "select e from test1_OrdenCobro e " +
//                " left join e.recibo r " +
//                " left join e.deudor d " +
//                " left join e.cuentaBancariaDeudora cb" +
//                " where r is null and (d is not null and cb is not null)";
//        List<OrdenCobro> oocc = dataManager.load(OrdenCobro.class).query(hql).view("ordenCobro-view").list();

        List<OrdenCobro> oocc = dataManager.loadList(loadContext);
        ArrayList al = new ArrayList();
        for (int i = 0; i < oocc.size(); i++) {
            OrdenCobro oc = oocc.get(i);
            if ((oc.getCreditor() != null) && (oc.getDeudor() != null)) {
                al.add(oc);
            }

        }
        return al;
    }

    @Subscribe("lkpPresentador")
    private void onLkpPresentadorValueChange(HasValue.ValueChangeEvent<Propietario> event) {
        cuentaBancariasDl.load();
    }

    public void onBtnCrearRealizacionCobroClick() {
        if (ordenCobroesTable.getSelected().size()==0){
            notifications.create().withCaption("Seleccionar una o más ordenes de cobro").show();
            return;
        }
        try {
            RealizacionCobro rc = ordenCobroService.generaRealizacionCobroParaOrdenes(new ArrayList(ordenCobroesTable.getSelected()), lkpPresentador.getValue().getPersona(), (CuentaBancaria) lkpCB.getValue());
            ordenCobroService.guardaRealizacionCobroDeOrdenesCobroRecibo(rc);
            notifications.create().withCaption("Archivo de Cobro " + rc.getIdentificador() + " guardado existosamente").withDescription("Acceda a los detalles mediante la pantalla de Realizaciones Cobros").show();
            filter.getDataLoader().load();
        } catch (Exception e) {
            e.printStackTrace();
            notifications.create().withCaption("Error").withDescription(e.getMessage()).show();
        }
    }

    @Install(to = "cuentaBancariasDl", target = Target.DATA_LOADER)
    private List<CuentaBancaria> cuentaBancariasDlLoadDelegate(LoadContext<CuentaBancaria> loadContext) {
        if (lkpPresentador.getValue()==null){
            return new ArrayList<>();
        }else{
            Propietario prop = lkpPresentador.getValue();
            List<CuentaBancaria> ccbb = prop.getPersona().getCuentasBancarias();
            for (int i = 0; i < ccbb.size(); i++) {
                CuentaBancaria cuentaBancaria =  ccbb.get(i);
                cuentaBancaria = dataManager.reload(cuentaBancaria, "cuentaBancaria-view");
                ccbb.set(i, cuentaBancaria);
            }
            return ccbb;
        }

    }



}