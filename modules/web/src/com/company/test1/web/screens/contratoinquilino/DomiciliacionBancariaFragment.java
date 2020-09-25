package com.company.test1.web.screens.contratoinquilino;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstancePropertyContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_DomiciliacionBancariaFragment")
@UiDescriptor("domiciliacion-bancaria-fragment.xml")
public class DomiciliacionBancariaFragment extends ScreenFragment {
    @Inject
    private InstanceContainer<ContratoInquilino> contratoInquilinoDc;
    @Inject
    private InstancePropertyContainer<ProgramacionRecibo> programacionReciboDc;
    @Inject
    private Table<CuentaBancaria> tableCuentasBancariasPagador;
    @Inject
    private LookupPickerField<Persona> pkfPagador;
    @Inject
    private CollectionLoader<CuentaBancaria> cuentasBancariasDl;

    @Subscribe(target = Target.PARENT_CONTROLLER)
    private void onAfterShow(Screen.AfterShowEvent event) {
        cuentasBancariasDl.load();
    }



    
    

    @Subscribe(id = "contratoInquilinoDc", target = Target.DATA_CONTAINER)
    private void onContratoInquilinoDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<ContratoInquilino> event) {
        if (event.getProperty().compareTo("pagador")==0){
            cuentasBancariasDl.load();
        }
    }

    

    @Install(to = "cuentasBancariasDl", target = Target.DATA_LOADER)
    private List<CuentaBancaria> cuentasBancariasDlLoadDelegate(LoadContext<CuentaBancaria> loadContext) {
        if (contratoInquilinoDc.getItem().getPagador()!=null){
            return contratoInquilinoDc.getItem().getPagador().getCuentasBancarias();
        }
        if (contratoInquilinoDc.getItem().getElPagadorEsElTitular()!=null) {
            if (contratoInquilinoDc.getItem().getElPagadorEsElTitular()) {
                if (contratoInquilinoDc.getItem().getInquilino()==null){
                    return new ArrayList<CuentaBancaria>();
                }
                return contratoInquilinoDc.getItem().getInquilino().getCuentasBancarias();
            }
        }
        return new ArrayList<CuentaBancaria>();
    }
    
    
    
}