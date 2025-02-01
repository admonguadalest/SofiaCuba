package com.company.test1.web.screens.dispositivo;

import com.company.test1.entity.departamentos.Departamento;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.Dispositivo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_Dispositivo.browse")
@UiDescriptor("dispositivo-browse.xml")
@LookupComponent("table")
@LoadDataBeforeShow
public class DispositivoBrowse extends MasterDetailScreen<Dispositivo> {


    @Inject
    private InstanceContainer<Dispositivo> dispositivoDc;
    @Inject
    private CollectionContainer<Dispositivo> dispositivoesDc;
    @Inject
    private TextArea<String> txaInfoRecursiva;
    @Inject
    private DataManager dataManager;

    @Subscribe("table")
    public void onTableSelection(Table.SelectionEvent<Dispositivo> event) {
        fillInfoDispositivosRelacionados();
    }

    @Subscribe("instaladoEnField")
    public void onInstaladoEnFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        fillInfoDispositivosRelacionados();
    }

    @Subscribe("searchDepto")
    public void onSearchDeptoValueChange(HasValue.ValueChangeEvent<Departamento> event) {
        fillInfoDispositivosRelacionados();
    }

    @Subscribe("createBtn")
    public void onCreateBtnClick(Button.ClickEvent event) {

    }

    @Subscribe(id = "dispositivoDc", target = Target.DATA_CONTAINER)
    public void onDispositivoDcItemChange(InstanceContainer.ItemChangeEvent<Dispositivo> event) {
        fillInfoDispositivosRelacionados();
    }
    
    



    List traversed = new ArrayList();
    String txt = "";
    private void fillInfoDispositivosRelacionados(){

        traversed.clear();
        txt = "";
        Dispositivo d = dispositivoDc.getItem();
        if ((d.getIdentificador()==null)||(d.getTipoDispositivo()==null)){
            return;
            
            
        }
        List<Dispositivo> explained = new ArrayList();
        this.fillInfoDispositivosRelacionados(d, true, false, false);
        txaInfoRecursiva.setValue(txt);

    }

    private void fillInfoDispositivosRelacionados(Dispositivo d, boolean doChildren, boolean onParent, boolean onChildren){
        if (traversed.indexOf(d.getId().toString())==-1){
            traversed.add(d.getId().toString());
        }else{
            return;
        }
        String instaladoEn = "(No Instalado)";
        if (d.getDepartamento()!=null){
            instaladoEn = d.getDepartamento().getNombreDescriptivoCompleto();
        }
        String q = "D";

        if (onParent) q = "P";
        if (onChildren) q = "CH";
        txt = txt + q + ": Identificador:"+d.getIdentificador() + " Tipo:"+d.getTipoDispositivo().getId()+" Instalado en:" + instaladoEn+"\n";
        Dispositivo parent = d.getDispositivoPadre();

        if (parent!=null){
            parent = dataManager.reload(parent, "dispositivo-view");
            fillInfoDispositivosRelacionados(parent, false, true, false);
        }

        List<Dispositivo> children = new ArrayList();
        for (int i = 0; i < dispositivoesDc.getItems().size(); i++) {
            Dispositivo d_ = dispositivoesDc.getItems().get(i);
            if (d_.getDispositivoPadre()!=null){
                Dispositivo dpadre = d_.getDispositivoPadre();
                dpadre = dataManager.reload(dpadre, "dispositivo-view");
                if (dpadre.getId().compareTo(d.getId())==0){
                    children.add(d_);
                }
            }

        }
        for (int i = 0; i < children.size(); i++) {
            Dispositivo dch = children.get(i);
            fillInfoDispositivosRelacionados(dch, false, false, true);
        }

    }



}