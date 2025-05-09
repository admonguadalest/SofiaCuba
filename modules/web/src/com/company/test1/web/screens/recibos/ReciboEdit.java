package com.company.test1.web.screens.recibos;

import com.company.test1.entity.recibos.ReciboCobrado;
import com.company.test1.entity.recibos.Serie;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.recibos.Recibo;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@UiController("test1_Recibo.edit")
@UiDescriptor("recibo-edit.xml")
@EditedEntityContainer("reciboDc")
@LoadDataBeforeShow
public class ReciboEdit extends StandardEditor<Recibo> {


    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<ReciboCobrado> tableMovimientos;
    @Inject
    private TextField<Double> acumuladoField;
    @Inject
    private RecibosService recibosService;
    @Inject
    private InstanceContainer<Recibo> reciboDc;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Notifications notifications;
    @Inject
    private Button btnRetrocederReciboEnZHelper;
    @Inject
    private ExportDisplay exportDisplay;

    @Inject
    private Button btnRegistrarReciboEnZHelper;
    @Inject
    private CollectionLoader<Serie> seriesDl;
    @Inject
    private DataManager dataManager;

    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        acumuladoField.setValue(recibosService.getTotalIngresadoDeRecibo(reciboDc.getItem()));
    }
    
    

    @Subscribe("tableMovimientos.create")
    private void onTableMovimientosCreate(Action.ActionPerformedEvent event) {
//        ScreenLaunchUtil.launchNewEntityStreen(ReciboCobrado.class, null, tableMovimientos, screenBuilders, this, OpenMode.DIALOG, dataContext,
//                s->{
//                    acumuladoField.setValue(recibosService.getTotalIngresadoDeRecibo(reciboDc.getItem()).toString());
//                });
        Screen s = screenBuilders.editor(ReciboCobrado.class, this)
                .withListComponent(tableMovimientos)
                .withParentDataContext(dataContext)
                .withLaunchMode(OpenMode.DIALOG)
                .withInitializer(rc->{rc.setRecibo(reciboDc.getItem());})
                .newEntity().build();
        s.addAfterCloseListener(e->{updateAcumuladoField();});
        s.show();
    }

    @Subscribe("tableMovimientos.edit")
    private void onTableMovimientosEdit(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchEditEntityScreen(tableMovimientos.getSingleSelected(), null, tableMovimientos, screenBuilders, this, OpenMode.DIALOG, dataContext,
                s->{
                    updateAcumuladoField();
                });
    }

    @Subscribe(id = "recibosCobradosDc", target = Target.DATA_CONTAINER)
    private void onRecibosCobradosDcCollectionChange(CollectionContainer.CollectionChangeEvent<ReciboCobrado> event) {
        updateAcumuladoField();
    }
    
    


    public void onBtnImprimirReciboClick() {
        Recibo r = reciboDc.getItem();
        try {
            byte[] bb = jasperReportService.getReportRecibo(r);
            exportDisplay.show(new ByteArrayDataProvider(bb), "Recibo " + r.getNumRecibo(), ExportFormat.getByExtension("pdf"));
        } catch (Exception e) {
            notifications.create().withCaption("Error").withCaption("Error al producir report de recibo");
        }
    }

    private void updateAcumuladoField(){
        acumuladoField.setValue(reciboDc.getItem().getTotalCobrado());
    }

    @Subscribe("btnRetrocederReciboEnZHelper")
    public void onBtnRetrocederReciboEnZHelperClick(Button.ClickEvent event) {
        Recibo r = reciboDc.getItem();
        try {
            recibosService.retrocedeRecibosEnZHelper(Arrays.asList(new Recibo[]{r}));
            notifications.create().withCaption("Recibo Retrocedido").withCaption("Recibo retrocedido con éxito").show();
        } catch (Exception e) {
            notifications.create().withCaption("Error").withCaption("Error al retroceder recibo de registro de iva").show();
        }

    }

    @Subscribe("btnRegistrarReciboEnZHelper")
    public void onBtnRegistrarReciboEnZHelperClick(Button.ClickEvent event) {
        Recibo r = reciboDc.getItem();
        r = dataManager.reload(r, "recibo-detalle-view");
        try {
            recibosService.registraReciboEnTablaZHelper(r);
            notifications.create().withCaption("Recibo Registrado").withCaption("Recibo registrado con éxito").show();
        } catch (Exception e) {
            notifications.create().withCaption("Error").withCaption("Error al registrar recibo de registro de iva").show();
        }
    }

    @Install(to = "seriesDl", target = Target.DATA_LOADER)
    private List<Serie> seriesDlLoadDelegate(LoadContext<Serie> loadContext) {
        List<Serie> ss = dataManager.load(Serie.class).query("select s from test1_Serie s").list();
        return ss;
    }


}