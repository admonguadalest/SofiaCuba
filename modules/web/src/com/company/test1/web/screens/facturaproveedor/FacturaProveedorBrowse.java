package com.company.test1.web.screens.facturaproveedor;

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
}