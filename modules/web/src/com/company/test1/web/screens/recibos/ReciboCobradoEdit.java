package com.company.test1.web.screens.recibos;

import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.haulmont.cuba.core.global.PersistenceHelper;
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

    @Subscribe("modoIngresoField")
    private void onModoIngresoFieldValueChange(HasValue.ValueChangeEvent<ReciboCobradoModoIngreso> event) {
        reciboCobradoDc.getItem().setTotalIngreso(this.getEditedEntity().getRecibo().getTotalReciboPostCCAA());
        if (PersistenceHelper.isNew(reciboCobradoDc.getItem())){
            fechaCobroField.setValue(new Date());
        }

    }
    
    
    
}