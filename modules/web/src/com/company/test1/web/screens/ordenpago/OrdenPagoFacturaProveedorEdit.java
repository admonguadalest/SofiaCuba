package com.company.test1.web.screens.ordenpago;

import com.company.test1.entity.Persona;
import com.company.test1.entity.ordenespago.CompensacionOrdenPagoProveedor;
import com.company.test1.entity.ordenespago.OrdenPagoAbono;
import com.company.test1.entity.ordenespago.OrdenPagoProveedor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ordenespago.OrdenPagoFacturaProveedor;
import freemarker.template.SimpleDate;

import javax.inject.Inject;
import java.text.SimpleDateFormat;

@UiController("test1_OrdenPagoFacturaProveedor.edit")
@UiDescriptor("orden-pago-factura-proveedor-edit.xml")
@EditedEntityContainer("ordenPagoFacturaProveedorDc")
@LoadDataBeforeShow
public class OrdenPagoFacturaProveedorEdit extends StandardEditor<OrdenPagoFacturaProveedor> {


    @Inject
    private UiComponents uiComponents;
    @Inject
    private InstanceContainer<OrdenPagoFacturaProveedor> ordenPagoFacturaProveedorDc;
    @Inject
    private DataManager dataManager;
    @Inject
    private DataContext dataContext;

    //PENDIENTE: Eliminar cuando hayamos resuelto el problema de los emisores/beneficiarios de las ordenes de pago
    @Subscribe("beneficiario")
    public void onBeneficiarioValueChange(HasValue.ValueChangeEvent<Persona> event) {
        ordenPagoFacturaProveedorDc.getItem().setBeneficiario(event.getValue());
        dataManager.commit(ordenPagoFacturaProveedorDc.getItem());
        OrdenPagoFacturaProveedor opfp = dataManager.reload(ordenPagoFacturaProveedorDc.getItem(), "ordenPagoFacturaProveedor-view");
        dataContext.merge(opfp);

    }
    //PENDIENTE: Eliminar cuando hayamos resuelto el problema de los emisores/beneficiarios de las ordenes de pago
    @Subscribe("emisor")
    public void onEmisorValueChange(HasValue.ValueChangeEvent<Persona> event) {
        ordenPagoFacturaProveedorDc.getItem().setEmisor(event.getValue());
        dataManager.commit(ordenPagoFacturaProveedorDc.getItem());
        OrdenPagoFacturaProveedor opfp = dataManager.reload(ordenPagoFacturaProveedorDc.getItem(), "ordenPagoFacturaProveedor-view");
        dataContext.merge(opfp);

    }
    @Inject
    private PickerField<Persona> emisor;
    @Inject
    private PickerField<Persona> beneficiario;

    public Component getColumnInfoFraAbono(CompensacionOrdenPagoProveedor copp){
        Label l = uiComponents.create(Label.NAME);
        String t = "";
        if (copp.getOrdenPagoAbono()!=null){
            OrdenPagoAbono opa = dataManager.reload(copp.getOrdenPagoAbono(),  "ordenPagoAbono-viewext");
            t = opa.getFacturaProveedor().getNumDocumento();
        }else{
            t = "";
        }

        l.setValue(t);
        return l;
    }

    public Component getColumnInfoRealizacionPago(CompensacionOrdenPagoProveedor copp){
        Label l = uiComponents.create(Label.NAME);
        String t = "";
        if (copp.getOrdenPagoProveedor()!=null){
            OrdenPagoProveedor opp = dataManager.reload(copp.getOrdenPagoProveedor(),  "ordenPagoProveedor-view");
            t = opp.getRealizacionPago().getIdentificador();
        }else{
            t = "";
        }

        l.setValue(t);
        return l;
    }

    public Component getColumnOrdenPagoCompensacion(CompensacionOrdenPagoProveedor copp){
        Label l = uiComponents.create(Label.NAME);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String t = "";
        if (copp.getOrdenPagoProveedor()!=null){
            t = "OPP " + sdf.format(copp.getOrdenPagoProveedor().getFechaValor()) + " " + copp.getImporte();
        }
        if (copp.getOrdenPagoAbono()!=null){
            t = "OPA " + sdf.format(copp.getOrdenPagoAbono().getFechaValor()) + " " + copp.getImporte();
        }
        l.setValue(t);
        return l;
    }


}