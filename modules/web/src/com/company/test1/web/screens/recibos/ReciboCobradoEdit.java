package com.company.test1.web.screens.recibos;

import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.ReciboCobrado;

import javax.inject.Inject;
import java.util.Date;

@UiController("test1_ReciboCobrado.edit")
@UiDescriptor("recibo-cobrado-edit.xml")
@EditedEntityContainer("reciboCobradoDc")
@LoadDataBeforeShow
public class ReciboCobradoEdit extends StandardEditor<ReciboCobrado> {


    @Inject
    private LookupField<ReciboCobradoModoIngreso> modoIngresoField;
    @Inject
    private InstanceContainer<ReciboCobrado> reciboCobradoDc;
    @Inject
    private DateField<Date> fechaCobroField;
    @Inject
    private Notifications notifications;

    @Subscribe("modoIngresoField")
    private void onModoIngresoFieldValueChange(HasValue.ValueChangeEvent<ReciboCobradoModoIngreso> event) {
        if(event.isUserOriginated()) {
            reciboCobradoDc.getItem().setTotalIngreso(this.getEditedEntity().getRecibo().getTotalReciboPostCCAA());
        }
        //ELIMINADO A PETICION DE ISABEL
//        if (PersistenceHelper.isNew(reciboCobradoDc.getItem())){
//            fechaCobroField.setValue(new Date());
//        }

    }

    @Subscribe("fechaCobroField")
    public void onFechaCobroFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (event.isUserOriginated()) {
            Date fechaMovto = reciboCobradoDc.getItem().getFechaCobro();
            ReciboCobradoModoIngreso rcmi = reciboCobradoDc.getItem().getModoIngreso();
            if ((fechaMovto!=null)&&(rcmi!=null)){
                if (!validaFechaParaMovto()){
                    notifications.create()
                            .withCaption("La fecha de movimiento no puede ser anterior a la fecha de emision del recibo para una devolucion")
                            .show();
                    fechaCobroField.setValue(null);
                }
            }

        }
    }

    private boolean validaFechaParaMovto(){
        Date fechaMovto = reciboCobradoDc.getItem().getFechaCobro();
        Date fechaEmisionRecibo = reciboCobradoDc.getItem().getRecibo().getFechaEmision();
        if (reciboCobradoDc.getItem().getModoIngreso() == ReciboCobradoModoIngreso.DEVUELTO) {
            if (fechaMovto.getTime() < fechaEmisionRecibo.getTime()) {
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }


    
    
    
}