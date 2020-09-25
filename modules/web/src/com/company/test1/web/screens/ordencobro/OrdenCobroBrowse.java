package com.company.test1.web.screens.ordencobro;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.ordenescobro.RealizacionCobro;
import com.company.test1.entity.ordenespago.OrdenPago;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import com.company.test1.entity.ordenespago.OrdenPagoProveedor;
import com.company.test1.service.OrdenCobroService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.haulmont.cuba.gui.screen.LookupComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@UiController("test1_OrdenCobro.browse")
@UiDescriptor("orden-cobro-browse.xml")
@LookupComponent("ordenCobroesTable")
@LoadDataBeforeShow
public class OrdenCobroBrowse extends StandardLookup<OrdenCobro> {

    @Inject
    private Table<OrdenCobro> ordenCobroesTable;

    @Inject
    private CollectionContainer<OrdenCobro> ordenCobroesDc;
    @Inject
    private LookupField lkpCB;
    @Inject
    private LookupField<Propietario> lkpPresentador;
    @Inject
    private CollectionLoader<CuentaBancaria> cuentaBancariasDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private DataContext dataContext;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Filter filter;

    @Subscribe("lkpPresentador")
    private void onLkpPresentadorValueChange(HasValue.ValueChangeEvent<Propietario> event) {
        cuentaBancariasDl.load();
    }

    private HashMap<OrdenCobro,CheckBox> checkBoxes = new HashMap<OrdenCobro,CheckBox>();

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        
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


    @Inject
    private UiComponents uiComponents;
    @Inject
    private Notifications notifications;
    @Inject
    private OrdenCobroService ordenCobroService;

    @Subscribe("ordenCobroesTable.remove")
    private void onOrdenCobroesTableRemove(Action.ActionPerformedEvent event) {
        notifications.create().withCaption("Desarrollo Pendietne").show();
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
            notifications.create().withCaption("Error").withDescription(e.getMessage());
        }
    }

    public void onBtnSeleccionarTodos(){

        ordenCobroesTable.setSelected(ordenCobroesDc.getItems());

    }



    public void onBtnInvertirSeleccion(){
        ArrayList selected = new ArrayList(ordenCobroesTable.getSelected());
        ArrayList newselection = new ArrayList();
        ordenCobroesTable.setSelected(new ArrayList());
        List<OrdenCobro> oocc = ordenCobroesDc.getItems();
        for (int i = 0; i < oocc.size(); i++) {
            OrdenCobro oc = oocc.get(i);
            if (selected.indexOf(oc)==-1){
                newselection.add(oc);
            }else{
                //nada
            }
        }
        ordenCobroesTable.setSelected(newselection);
    }

    public void onBtnRemove(){
        OrdenCobro oc = ordenCobroesTable.getSingleSelected();
        if (oc == null){
            notifications.create().withCaption("Seleccionar una Orden de Cobro").show();
            return;
        }
        if (oc.getRealizacionCobro()!=null){
            notifications.create().withCaption("La Orden de Cobro ya dispone de Realización Cobro. Retroceda la Realización Cobro.").show();
            return;
        }
        //PENDIENTE VERIFICAR UQE NO SE PUEDAN RETROCEDER ORDENES DE COBRO ASOCIADAS A REMESAS
        if (oc.getRecibo().getOrdenanteRemesa().getRemesa()!=null){
            notifications.create().withCaption("La Orden de Cobro se generó en el contexto de una Remesa. Retroceda la remesa.").show();
            return;
        }
        //PENDIENTE incorporar diálogo de confirmación
        dialogs.createOptionDialog(Dialogs.MessageType.CONFIRMATION).withCaption("Confirmar Acción")
                .withMessage("¿Está seguro?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                            dataManager.remove(oc);
                        }),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();

    }

    @Subscribe(id = "ordenCobroesDl", target = Target.DATA_LOADER)
    public void onOrdenCobroesDlPostLoad(CollectionLoader.PostLoadEvent event) {

    }

    @Subscribe(id = "ordenCobroesDl", target = Target.DATA_LOADER)
    public void onOrdenCobroesDlPreLoad(CollectionLoader.PreLoadEvent event) {
        checkBoxes.clear();
    }
    
    
}