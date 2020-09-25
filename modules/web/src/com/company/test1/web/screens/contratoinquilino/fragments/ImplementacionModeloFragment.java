package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.contratosinquilinos.ImplementacionModelo;
import com.company.test1.entity.contratosinquilinos.ParametroValor;
import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.web.screens.contratoinquilino.clausulado.SelectorVersionClausula;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.EditAction;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_ImplementacionModeloFragment")
@UiDescriptor("implementacion-modelo-fragment.xml")
public class ImplementacionModeloFragment extends ScreenFragment {
    @Inject
    private CollectionLoader<ParametroValor> parametroValorsDl;
    @Inject
    private Table<ParametroValor> parametroValorsTable;
    @Inject
    private PickerField<ModeloContrato> pickerModeloContrato;
    @Inject
    private CollectionLoader<Clausula> clausulasDl;
    @Inject
    private InstanceContainer<ImplementacionModelo> implementacionModeloDc;
    @Inject
    private Notifications notifications;
    @Inject
    private DataContext dataContext;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<ModeloContrato> modeloContratoesDl;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        modeloContratoesDl.load();
        ImplementacionModelo im = implementacionModeloDc.getItem();
        clausulasDl.load();
        parametroValorsDl.load();


    }
    
    

    @Subscribe("pickerModeloContrato")
    public void onPickerModeloContratoValueChange(HasValue.ValueChangeEvent<ModeloContrato> event) {
        ImplementacionModelo im = implementacionModeloDc.getItem();
        im.setModeloContrato(pickerModeloContrato.getValue());
        try{
            ImplementacionModelo.inicializaParametrosValores(im);
        }catch(Exception exc){
            notifications.create().withCaption("No se pudo incializar los parametros para el modelo seleccionado").show();

        }
        clausulasDl.load();
        parametroValorsDl.load();
    }


    
    


    @Install(to = "parametroValorsDl", target = Target.DATA_LOADER)
    private List<ParametroValor> parametroValorsDlLoadDelegate(LoadContext<ParametroValor> loadContext) {
        ImplementacionModelo im = implementacionModeloDc.getItem();
        ModeloContrato mc = pickerModeloContrato.getValue();


        if (im.getParametrosValores().size()>0){
            return im.getParametrosValores();
        }else{
            if (im.getModeloContrato()!=null){


                return im.getParametrosValores();

            }else{
                return new ArrayList<ParametroValor>();
            }
        }
    }

    @Install(to = "clausulasDl", target = Target.DATA_LOADER)
    private List<Clausula> clausulasDlLoadDelegate(LoadContext<Clausula> loadContext) {
        if (pickerModeloContrato.getValue()!=null){
            ModeloContrato mc = pickerModeloContrato.getValue();
            mc = dataManager.reload(mc, "modeloContrato-view");
            List<Clausula> cc = Clausula.getTodasLasClausulasDeModeloContrato(mc);
            return cc;
        }
        return new ArrayList();
    }

    @Subscribe("pickerClausula")
    public void onPickerClausulaValueChange(HasValue.ValueChangeEvent<Clausula> event) {
        Clausula c = event.getValue();
        Screen s = screenBuilders.screen(this).withScreenClass(SelectorVersionClausula.class).withOpenMode(OpenMode.DIALOG).build();
        SelectorVersionClausula svc = (SelectorVersionClausula) s;
        svc.implementacionModelo = implementacionModeloDc.getItem();
        svc.clausula = c;
        svc.clausulaDc.setItem(c);
        svc.show();
        svc.addAfterCloseListener(se->{
            parametroValorsDl.load();
        });
    }

    public void editParametroValor(){
        if (parametroValorsTable.getSingleSelected()==null){
            notifications.create().withCaption("Seleccionar parametro a editoar").show();
            return;
        }
        Screen s = screenBuilders.editor(ParametroValor.class, this).editEntity(parametroValorsTable.getSingleSelected()).withParentDataContext(dataContext).withOpenMode(OpenMode.DIALOG).withListComponent(parametroValorsTable)
                .build();
        s.show();

    }

}