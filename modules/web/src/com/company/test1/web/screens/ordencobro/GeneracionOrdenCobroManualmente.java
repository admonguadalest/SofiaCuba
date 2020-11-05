package com.company.test1.web.screens.ordencobro;

import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.recibos.OrdenanteRemesa;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.Remesa;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UiController("test1_GeneracionOrdenCobroManualmente")
@UiDescriptor("generacion-orden-cobro-manualmente.xml")


public class GeneracionOrdenCobroManualmente extends Screen {
    @Inject
    private Notifications notifications;
    @Inject
    private Table<Recibo> tableRecibos;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionContainer<Recibo> recibosDc;
    @Inject
    private DateField<Date> fechaValorField;
    @Inject
    private DataContext dataContext;

    @Subscribe("remesaField")
    public void onRemesaFieldValueChange(HasValue.ValueChangeEvent<Remesa> event) {
        Remesa r = event.getValue();
        r = dataManager.reload(r, "remesa-view-detalle");
        if (r==null){
            notifications.create().withCaption("Seleccionar una remesa").show();
            return;
        }
        List<Recibo> rr = new ArrayList<Recibo>();
        for (int i = 0; i < r.getOrdenantesRemesa().size(); i++) {
            OrdenanteRemesa or = r.getOrdenantesRemesa().get(i);
            for (int j = 0; j < or.getRecibos().size(); j++) {
                Recibo rbo = or.getRecibos().get(j);
                rr.add(rbo);
            }
        }
        recibosDc.setItems(rr);
    }


    @Inject
    private LookupPickerField<Remesa> remesaField;

    public void onBtnRealizarClick(){
        Date fechaValor = fechaValorField.getValue();
        List<Recibo> rr = Arrays.asList(tableRecibos.getSelected().toArray(new Recibo[0]));
        List recibosCobradosTotalmente = new ArrayList();
        if ((fechaValor == null ) || (rr.size()==0)){
            notifications.create().withCaption("Aportar Fecha de Valor y selección de recibos para creación de Orden Pago").show();
            return;
        }


        try{


            for (int i = 0; i < rr.size(); i++) {
                Recibo recibo = rr.get(i);

                recibo = dataManager.reload(recibo, "recibo-view");

                if (recibo.getTotalReciboPostCCAA() <= recibo.getTotalCobrado()){
                    recibosCobradosTotalmente.add(recibo);
                }

                OrdenCobro ocr = dataContext.create(OrdenCobro.class);
                ocr.setRecibo(recibo);


                recibo.getOrdenesCobro().add(ocr);


                ocr.setImporte(recibo.getTotalReciboPostCCAA());
                ocr.setFechaValor(fechaValor);

                dataManager.commit(ocr);

            }

            if (recibosCobradosTotalmente.size()>0){
                throw new Exception(recibosCobradosTotalmente.size() + " constan como cobrados totalmente");
            }


            notifications.create().withCaption("Las ordenes de cobro se han realizado correctamente").show();

        }catch(Exception ex){
            try{

                notifications.create().withCaption("No se generaron las ordenes de cobro: " + ex.getMessage()).show();
            }catch(Exception ex2){
                int x = 2;
            }
        }
    }
}