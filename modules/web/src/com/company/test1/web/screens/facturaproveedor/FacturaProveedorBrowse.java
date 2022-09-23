package com.company.test1.web.screens.facturaproveedor;

import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.service.ContabiService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.FacturaProveedor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_FacturaProveedor.browse")
@UiDescriptor("factura-proveedor-browse.xml")
@LookupComponent("facturaProveedorsTable")
@LoadDataBeforeShow
public class FacturaProveedorBrowse extends StandardLookup<FacturaProveedor> {

    @Inject
    private GroupTable<FacturaProveedor> facturaProveedorsTable;
    @Inject
    private Button btnPublicarContabilidad;
    @Inject
    private ContabiService contabiService;
    @Inject
    private Notifications notifications;
    @Inject
    private Filter filter;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        filter.setCollapsable(false);
    }



    private void doTableReport(){
        List<FacturaProveedor> ffpp = (List<FacturaProveedor>)facturaProveedorsTable.getItems().getItems();
        List<Table.Column<FacturaProveedor>> cols = facturaProveedorsTable.getColumns();
        ArrayList<String> colIds = new ArrayList<String>();
        ArrayList<Integer> ws = new ArrayList<Integer>();
        for (int i = 0; i < cols.size(); i++) {
            Table.Column<FacturaProveedor> fpc =  cols.get(i);
            String id = fpc.getIdString();
            colIds.add(id);
            try {
                int w = fpc.getWidth();
                ws.add(w);
            }catch(Exception exc){

            }

        }
        int y = 2;
    }

    public void onBtnReportClick() {
        doTableReport();
    }

    @Subscribe("btnPublicarContabilidad")
    public void onBtnPublicarContabilidadClick(Button.ClickEvent event) {
        DocumentoImputable fprov = facturaProveedorsTable.getSingleSelected();
        if (fprov==null){
            notifications.create().withCaption("Seleccionar un registro").show();
            return;
        }
        if (fprov instanceof FacturaProveedor){
            try {
                boolean res = contabiService.publicaContabilizacionFacturaProveedor((FacturaProveedor) fprov);
                if (res){
                    notifications.create().withCaption("Factura publicada corréctamente").show();
                }
            } catch (Exception e) {
                notifications.create().withCaption("Error al publicar").withDescription(e.getMessage()).show();
                return;
            }
        }else{
            notifications.create().withCaption("Seleccionar un registro tipo Factra Proveedor (FP)").show();
            return;
        }

    }


}