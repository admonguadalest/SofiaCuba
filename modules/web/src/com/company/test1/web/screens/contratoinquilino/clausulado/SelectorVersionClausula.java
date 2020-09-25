package com.company.test1.web.screens.contratoinquilino.clausulado;

import com.company.test1.entity.contratosinquilinos.ImplementacionModelo;
import com.company.test1.entity.contratosinquilinos.OverrideClausula;
import com.company.test1.entity.contratosinquilinos.ParametroValor;
import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;


import javax.inject.Inject;
import java.util.List;

@UiController("test1_SelectorVersionClausula")
@UiDescriptor("selector-version-clausula.xml")
public class SelectorVersionClausula extends Screen {
    
    public ImplementacionModelo implementacionModelo = null;
    public Clausula clausula = null;
    public OverrideClausula overrideClausula = null;
    @Inject
    private Table<VersionClausula> versionClausulasTable;
    @Inject
    private CollectionLoader<VersionClausula> versionClausulasDl;
    @Inject
    public InstanceContainer<Clausula> clausulaDc;

    @Inject
    private TextArea<String> txtPreviewVersionClausula;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

        versionClausulasDl.load();
        if (implementacionModelo!=null){

            try {
                overrideClausula = OverrideClausula.getOverrideClausula(implementacionModelo, clausula);
                if (overrideClausula==null){
                    overrideClausula = new OverrideClausula();
                    overrideClausula.setClausula(clausula);
                    overrideClausula.setVersionAplicada(Clausula.getVersionPredeterminada(clausula));
                    versionClausulasTable.setSelected(overrideClausula.getVersionAplicada());
                    implementacionModelo.getOverrideClausulas().add(overrideClausula);
                }else{
                    versionClausulasTable.setSelected(overrideClausula.getVersionAplicada());
                }
            }catch(Exception exc){

            }
        }
    }

    @Subscribe("versionClausulasTable")
    public void onVersionClausulasTableSelection(Table.SelectionEvent<VersionClausula> event) {
        VersionClausula vc = versionClausulasTable.getSingleSelected();
        overrideClausula.setVersionAplicada(vc);

        txtPreviewVersionClausula.setValue(vc.getTextoVersion());
    }

    @Subscribe
    public void onBeforeClose(BeforeCloseEvent event) {
        try {
            if (overrideClausula.getVersionAplicada().getId().compareTo(Clausula.getVersionPredeterminada(clausula).getId()) == 0) {
                overrideClausula.setImplementacionModelo(null);
                //quitar override de la lista de overrides de implementacionModelo
                implementacionModelo.getOverrideClausulas().remove(overrideClausula);
            }
            ImplementacionModelo.inicializaParametrosValores(implementacionModelo);

        }catch(Exception exc){

        }
    }

    @Install(to = "versionClausulasDl", target = Target.DATA_LOADER)
    private List<VersionClausula> versionClausulasDlLoadDelegate(LoadContext<VersionClausula> loadContext) {
        return clausulaDc.getItem().getVersiones();
    }


    
    

    
    
    
    
}