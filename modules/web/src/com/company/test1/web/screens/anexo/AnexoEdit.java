package com.company.test1.web.screens.anexo;

import com.company.test1.entity.contratosinquilinos.ParametroValorAnexo;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.service.PlantillaService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.contratosinquilinos.Anexo;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@UiController("test1_Anexo.edit")
@UiDescriptor("anexo-edit.xml")
@EditedEntityContainer("anexoDc")
@LoadDataBeforeShow
public class AnexoEdit extends StandardEditor<Anexo> {
    @Inject
    private InstanceContainer<Anexo> anexoDc;
    @Inject
    private CollectionPropertyContainer<ParametroValorAnexo> parametrosValoresAnexoDc;
    @Inject
    private PickerField<Plantilla> plantillaField;
    @Inject
    private RichTextArea rtaContenidoAnexo;
    @Inject
    private PlantillaService plantillaService;
    @Inject
    private DataContext dataContext;
    @Inject
    private DataManager dataManager;
    @Inject
    private Table<ParametroValorAnexo> tableParametrosValor;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Notifications notifications;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        if (anexoDc.getItem().getPlantilla()!=null){
            if (anexoDc.getItem().getContenido()==null) {
                rtaContenidoAnexo.setValue(anexoDc.getItem().getPlantilla().getContenidoPlantilla());
            }
        }
        if (anexoDc.getItem().getParametrosValores()!=null){
            int y = 2;
        }
    }

//    @Subscribe("plantillaField")
//    private void onPlantillaValueChange(HasValue.ValueChangeEvent<Plantilla> event) {
//        Plantilla p = event.getValue();
//        rtaContenidoAnexo.setValue(p.getContenidoPlantilla());
//        List<ParametroValorAnexo> currList = parametrosValoresAnexoDc.getMutableItems();
//        List<ParametroValorAnexo> defList = plantillaService.devuelveParametrosDePlantilla(p);
//
//        if (currList!=null){
//            for (ParametroValorAnexo parametroValorAnexo : currList) {
//                parametroValorAnexo.setAnexo(null);
//                dataContext.remove(parametroValorAnexo);
//            }
//            currList.clear();
//        }
//        for (ParametroValorAnexo parametroValorAnexo : defList) {
//            parametroValorAnexo.setAnexo(anexoDc.getItem());
//            currList.add(parametroValorAnexo);
//        }
//        int y = 2;
//
//
//    }

    
    


    @Subscribe("tableParametrosValor.edit")
    private void onTableParametrosValorEdit(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchEditEntityScreen(tableParametrosValor.getSingleSelected(), null, tableParametrosValor, screenBuilders,
//                this, OpenMode.DIALOG, dataContext, null);
        ParametroValorAnexo pva = tableParametrosValor.getSingleSelected();
        if (pva == null){
            notifications.create().withDescription("Seleccionar un parametro").show();
            return;
        }
        screenBuilders.editor(ParametroValorAnexo.class, this).editEntity(pva).withParentDataContext(dataContext)
                .withOpenMode(OpenMode.DIALOG).withListComponent(tableParametrosValor).build().show();
    }

    @Subscribe("plantillaField")
    public void onPlantillaFieldValueChange(HasValue.ValueChangeEvent<Plantilla> event) {
        if (!event.isUserOriginated()) return;
        Plantilla p = event.getValue();

        rtaContenidoAnexo.setValue(p.getContenidoPlantilla());
        List<ParametroValorAnexo> currList = parametrosValoresAnexoDc.getMutableItems();
        List<ParametroValorAnexo> defList = plantillaService.devuelveParametrosDePlantilla(p);

        if (currList!=null){
            for (ParametroValorAnexo parametroValorAnexo : currList) {
                parametroValorAnexo.setAnexo(null);
                dataContext.remove(parametroValorAnexo);
            }
            currList.clear();
        }
        for (ParametroValorAnexo parametroValorAnexo : defList) {
            //los creamos via dataContext
            ParametroValorAnexo pva = dataContext.create(ParametroValorAnexo.class);
            pva.setAnexo(anexoDc.getItem());
            pva.setNombreParametro(parametroValorAnexo.getNombreParametro());

            currList.add(pva);
        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        int y = 2;
        //de esta manera fuerzo un refresco de la entidad en el data context
        //no se porque los parametros valores anexo no se me guardaban en la edicion de nueva instancia, aunque sí en la de edicion

    }




    
    
    
    



}