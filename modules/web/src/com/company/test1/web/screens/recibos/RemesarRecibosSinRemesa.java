package com.company.test1.web.screens.recibos;

import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_RemesarRecibosSinRemesa")
@UiDescriptor("remesar-recibos-sin-remesa.xml")
public class RemesarRecibosSinRemesa extends Screen {
    @Inject
    private DateField<Date> dteFechaAdeudo;
    @Inject
    private DateField<Date> dteFechaDesde;
    @Inject
    private DateField<Date> dteFechaHasta;
    @Inject
    private DateField<Date> dteFechaValor;
    @Inject
    private LookupField lkpDefinicionRemesa;
    @Inject
    private LookupField lkpTipoGiro;
    @Inject
    private CollectionLoader<Recibo> reciboesDl;
    @Inject
    private TextField<String> txtCodigoRemesa;
    @Inject
    private CollectionContainer<Recibo> reciboesDc;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionContainer<DefinicionRemesa> definicionRemesasDc;
    @Inject
    private CollectionLoader<DefinicionRemesa> definicionRemesasDl;
    @Inject
    private Table<Recibo> tblRecibos;
    @Inject
    private ExportDisplay exportDisplay;

    @Subscribe
    private void onAfterInit(AfterInitEvent event) {
        definicionRemesasDl.load();
    }

    

    public void onBtnActualizarClick() {
        reciboesDl.load();
    }



    @Install(to = "reciboesDl", target = Target.DATA_LOADER)
    private List<Recibo> reciboesDlLoadDelegate(LoadContext<Recibo> loadContext) {
        if ((dteFechaDesde.getValue()==null) && (dteFechaHasta.getValue()==null)){
            notifications.create().withCaption("Aportar rango de fechas para seleccion de recibos");
            return new ArrayList<Recibo>();
        }

        DefinicionRemesaTipoGiroEnum tipoGiroEnum = (DefinicionRemesaTipoGiroEnum) lkpTipoGiro.getValue();



        String codigoRemesa = txtCodigoRemesa.getValue();

        String hql = "select r from test1_Recibo r LEFT JOIN r.ordenanteRemesa orem WHERE orem is null AND " +
                "r.fechaEmision >= :fd and r.fechaEmision <= :fh";
        List<Recibo> rr = dataManager.loadValue(hql, Recibo.class).parameter("fd", dteFechaDesde.getValue()).parameter("fh", dteFechaHasta.getValue()).list();


        return rr;
    }

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Recibos", Recibo.class, tblRecibos);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Recibos.pdf");
    }

    public void onBtnCerrarClick() {
        this.closeWithDefaultAction();
    }

    public void onBtnRealizarClick() {
        int y = 2;
    }
}