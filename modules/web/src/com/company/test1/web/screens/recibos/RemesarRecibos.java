package com.company.test1.web.screens.recibos;

import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.Remesa;
import com.company.test1.service.OrdenCobroService;
import com.company.test1.service.RecibosService;
import com.company.test1.web.screens.ScreenLaunchUtil;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_RemesarRecibos")
@UiDescriptor("remesar-recibos.xml")
@EditedEntityContainer("remesaDc")
@LoadDataBeforeShow
public class RemesarRecibos extends Screen {


    @Inject
    private CollectionContainer<Recibo> recibosDc;
    

    @Inject
    private Button btnBuscar;

    @Inject
    private Button btnVerReport;
    @Inject
    private Button cerrar;
    @Inject
    private TextField<String> codigoRemesa;
    @Inject
    private LookupField definicionesRemesaField;
    @Inject
    private DateField<Date> fechaAdeudo;
    @Inject
    private DateField<Date> fechaDesde;
    @Inject
    private DateField<Date> fechaHasta;
    @Inject
    private DateField<Date> fechaValor;
    @Inject
    private LookupField tipoGiro;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataContext dataContext;
    @Inject
    private Table<Recibo> tableRecibos;
    @Inject
    private CollectionLoader<DefinicionRemesa> definicionRemesasDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private RecibosService recibosService;
    @Inject
    private OrdenCobroService ordenCobroService;

    @Inject
    private Notifications notifications;



    
    
    @Subscribe
    private void onAfterShow(AfterShowEvent event) {
        definicionRemesasDl.load();
    }


    @Install(to = "reciboesDl", target = Target.DATA_LOADER)
    private List<Recibo> reciboesDlLoadDelegate(LoadContext<Recibo> loadContext) {
        return new ArrayList<>();
    }
    

    @Subscribe("tableRecibos.create")
    private void onTableRecibosCreate(Action.ActionPerformedEvent event) {
        ScreenLaunchUtil.launchNewEntityStreen(Recibo.class, "test1_ReciboIndividualizado.edit", tableRecibos,screenBuilders,this, OpenMode.NEW_TAB,null, null);
    }

    @Subscribe("tableRecibos.edit")
    private void onTableRecibosEdit(Action.ActionPerformedEvent event) {
        Recibo r = tableRecibos.getSingleSelected();
        r = dataManager.reload(r, "recibo-view");
        ScreenLaunchUtil.launchEditEntityScreen(r, "test1_ReciboIndividualizado.edit", tableRecibos,screenBuilders,this, OpenMode.NEW_TAB,null, null);
    }


    public void onBtnBuscarClick() {
        ArrayList<Recibo> al = new ArrayList<Recibo>();
        List<Recibo> rr = recibosService.devuelveRecibosSinRemesasParaDatos(fechaDesde.getValue(), fechaHasta.getValue(), null);
        for (int i = 0; i < rr.size(); i++) {
            Recibo recibo =  rr.get(i);
            recibo = dataManager.reload(recibo, "recibo-view");
            al.add(recibo);

        }
        recibosDc.getMutableItems().clear();
        recibosDc.getMutableItems().addAll(al);

    }

    public void onBtnGenerarRemesaYOOCCClick() {

        if (recibosDc.getItems().size()==0){
            notifications.create().withCaption("Seleccionar recibos a incluir en la remesa").show();
            return;
        }
        if (definicionesRemesaField.getValue()==null){
            notifications.create().withCaption("Seleccionar la Definicion de Remesa a aplicar").show();
            return;
        }
        if ((fechaAdeudo.getValue()==null)||(fechaValor.getValue()==null)){
            notifications.create().withCaption("Indicar fecha de valor y adeudo").show();
            return;
        }
        if (codigoRemesa.getValue()==null){
            notifications.create().withCaption("Indicar codigo de remesa").show();
            return;
        }

        List<OrdenCobro> oocc = ordenCobroService.generaOrdenesCobroParaRecibos(recibosDc.getItems(), fechaAdeudo.getValue());
        Remesa r = recibosService.generaRemesaParaRecibos(recibosDc.getItems(), (DefinicionRemesa) definicionesRemesaField.getValue(), fechaValor.getValue(), fechaAdeudo.getValue(), fechaValor.getValue(), codigoRemesa.getValue());

        ArrayList al = new ArrayList(oocc);
        al.add(r);
        //anadiendo el ordenante remesa para evitar los errores de sincronizacion
        al.add(r.getOrdenantesRemesa().get(0));

        /* PENDIENTE SOLUCIONAR MEJOR: ESTA EN 3 TRANSACCIONES Y NO PARECE CORRECTO*/
        dataManager.commit(new CommitContext(al));
        al.clear();
        //actualizando los recibos con el OrdenanteRemesa
        double totalesRemesa = 0.0;
        for (int i = 0; i < recibosDc.getItems().size(); i++) {
            recibosDc.getItems().get(i).setOrdenanteRemesa(r.getOrdenantesRemesa().get(0));
            al.add(recibosDc.getItems().get(i));
            totalesRemesa += recibosDc.getItems().get(i).getTotalReciboPostCCAA();
        }
        r.setTotalRemesa(totalesRemesa);
        dataManager.commit(new CommitContext(al));



        notifications.create().withCaption("Remesa generada y guardada correctamente").show();

        onBtnBuscarClick();
    }


    public void onCerrarClick() {
        this.closeWithDefaultAction();
    }

    @Subscribe("definicionesRemesaField")
    public void onDefinicionesRemesaFieldValueChange(HasValue.ValueChangeEvent<DefinicionRemesa> event) {
        DefinicionRemesa dr = event.getValue();
        if ((dr!=null)&&(fechaValor.getValue()!=null)){
            actualizaCodigoRemesa();
        }
    }

    @Subscribe("fechaValor")
    public void onFechaValorValueChange(HasValue.ValueChangeEvent<Date> event) {
        if ((event.getValue()!=null)&&(definicionesRemesaField.getValue()!=null)){
            actualizaCodigoRemesa();
        }
    }

    private void actualizaCodigoRemesa(){
        try{
            String identificadorRemesa = recibosService.calculaIdentificadorRemesa((DefinicionRemesa) definicionesRemesaField.getValue(), fechaValor.getValue());
            codigoRemesa.setValue(identificadorRemesa);
        }catch(Exception exc){
            notifications.create().withCaption("No se pudo generar el Codigo de Remesa").show();
        }

    }
    
    
    
    
}