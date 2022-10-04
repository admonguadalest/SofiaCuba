package com.company.test1.web.screens;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.recibos.FlujoMonetarioContrato;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.ReciboCobrado;
import com.company.test1.service.JasperReportService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.*;

@UiController("test1_HistoricoCobrosInquilino")
@UiDescriptor("historico-cobros-inquilino.xml")
public class HistoricoCobrosInquilino extends Screen {
    @Inject
    private PickerField<ContratoInquilino> pckContrato;
    @Inject
    private DateField<Date> fechaHasta;
    @Inject
    private CollectionLoader<FlujoMonetarioContrato> flujosDl;

    @Inject
    private DateField<Date> fechaDesde;
    @Inject
    private Label<String> lblInfo;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionContainer<FlujoMonetarioContrato> flujosDc;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Notifications notifications;


    private boolean puedeBuscar(){
        return ((pckContrato.getValue()!=null) && (fechaDesde.getValue()!=null) && (fechaHasta.getValue()!=null));
    }

    @Install(to = "flujosDl", target = Target.DATA_LOADER)
    private List<FlujoMonetarioContrato> flujosDlLoadDelegate(LoadContext<FlujoMonetarioContrato> loadContext) {
        if (!puedeBuscar()){
            lblInfo.setValue("Completar todos los campos para proceder");
            return new ArrayList<>();
        }else{
            lblInfo.setValue("");

        }
        ContratoInquilino ci = pckContrato.getValue();
        Date fechaDesde = this.fechaDesde.getValue();
        Date fechaHasta = this.fechaHasta.getValue();

        String q = "SELECT r FROM test1_Recibo r join r.utilitarioContratoInquilino ci WHERE ci.id = :nc " +
                "and r.fechaEmision >= :fd and r.fechaEmision <= :fh";
        List<Recibo> rr = dataManager.load(Recibo.class).query(q)
                .parameter("nc", ci.getId())
                .parameter("fd", fechaDesde)
                .parameter("fh", fechaHasta)
                .list();

        HashMap<Date, FlujoMonetarioContrato> importes = new HashMap<>();
        for (int i = 0; i < rr.size(); i++) {
            Recibo r = rr.get(i);
            r = dataManager.reload(r, "recibo-view");

            //si es bancario que se obvie pues el movimiento ya vendra por el movimiento de recibo cobrado
            DefinicionRemesaTipoGiroEnum drtge = null;
            try{
                drtge = r.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro();
            }catch(Exception exc){
                //no hacemos nada
            }

            Date fe = r.getFechaEmision();
            if (!importes.containsKey(fe)){
                FlujoMonetarioContrato fmc = new FlujoMonetarioContrato();
                fmc.setFechaMovimiento(fe);
                importes.put(fe, fmc);
                fmc.setInformacionRecibo(r.getNumRecibo());
                fmc.setNominalRecibo(r.getTotalReciboPostCCAA());
                fmc.setConcepto("EMISIÓN RECIBO");
                if (r.getOrdenanteRemesa()!=null){
                    if (r.getOrdenanteRemesa().getRemesa()!=null){
                        if (r.getOrdenanteRemesa().getRemesa().getDefinicionRemesa()!=null){
                            fmc.setInformacionRemesa(r.getOrdenanteRemesa().getRemesa().getIdentificadorRemesa());
                        }
                    }
                }
            }
            FlujoMonetarioContrato fmcr = importes.get(fe);
            if (r.getTotalReciboPostCCAA()>=0.0) {
                fmcr.setImporteDebe(fmcr.getImporteDebe () + (double) r.getTotalReciboPostCCAA());
            }else{
                fmcr.setImporteHaber(fmcr.getImporteHaber () + (double) r.getTotalReciboPostCCAA());
            }




            List<ReciboCobrado> rrcc = r.getRecibosCobrados();
            Collections.sort(rrcc, new Comparator<ReciboCobrado>() {
                @Override
                public int compare(ReciboCobrado o1, ReciboCobrado o2) {
                    return o1.getFechaCobro().compareTo(o1.getFechaCobro());
                }
            });

            for (int j = 0; j < rrcc.size(); j++) {
                ReciboCobrado rc = r.getRecibosCobrados().get(j);
                Date frc = rc.getFechaCobro();
                if (!importes.containsKey(frc)){
                    FlujoMonetarioContrato fmc = new FlujoMonetarioContrato();
                    fmc.setFechaMovimiento(frc);
                    importes.put(frc, fmc);
                    fmc.setInformacionRecibo(r.getNumRecibo());

                }
                if (rc.getModoIngreso() == ReciboCobradoModoIngreso.BANCARIO) {
                    FlujoMonetarioContrato fmc = importes.get(frc);
                    if (rc.getTotalIngreso() >= 0.0) {
                        fmc.setImporteHaber(fmc.getImporteHaber () + (double) rc.getTotalIngreso());
                    } else {
                        fmc.setImporteDebe(fmc.getImporteDebe () + (double) rc.getTotalIngreso());
                    }
                    fmc.setConcepto(fmc.getConcepto() + " " + "BANCARIO");

                }
                if (rc.getModoIngreso() == ReciboCobradoModoIngreso.ADMINISTRACION) {
                    FlujoMonetarioContrato fmc = importes.get(frc);
                    if (rc.getTotalIngreso() >= 0.0) {
                        fmc.setImporteHaber(fmc.getImporteHaber () + (double) rc.getTotalIngreso());
                    } else {
                        fmc.setImporteDebe(fmc.getImporteDebe () + (double) rc.getTotalIngreso());
                    }
                    fmc.setConcepto(fmc.getConcepto() + " " + "ADMINISTRACION");
                }
                if (rc.getModoIngreso() == ReciboCobradoModoIngreso.COMPENSACION_ABONO_RECIBO) {
                    FlujoMonetarioContrato fmc = importes.get(frc);
                    if (rc.getTotalIngreso() >= 0.0) {
                        fmc.setImporteHaber(fmc.getImporteHaber () + (double) rc.getTotalIngreso());
                    } else {
                        fmc.setImporteDebe(fmc.getImporteDebe () + (double) rc.getTotalIngreso());
                    }
                    fmc.setConcepto(fmc.getConcepto() + " " + "COMPENSACION");
                }
                if (rc.getModoIngreso() == ReciboCobradoModoIngreso.DEVUELTO) {
                    FlujoMonetarioContrato fmc = importes.get(frc);
                    if (rc.getTotalIngreso() >= 0.0) {

                        fmc.setImporteDebe(fmc.getImporteDebe () + (double) rc.getTotalIngreso());
                    } else {
                        fmc.setImporteHaber(fmc.getImporteHaber () + (double) rc.getTotalIngreso());
                    }
                    fmc.setConcepto(fmc.getConcepto() + " " + "DEVUELTO");
                }

            }
        }
        //convierto a instancias de flujomonetario para poblar tabla
        Set<Date> s = importes.keySet();
        ArrayList<Date> al2 = new ArrayList(s);
        ArrayList<FlujoMonetarioContrato> results = new ArrayList();
        for (int i = 0; i < al2.size(); i++) {
            Date f_ = al2.get(i);
            FlujoMonetarioContrato fmc = importes.get(f_);
            results.add(fmc);
        }
        Collections.sort(results, new Comparator<FlujoMonetarioContrato>() {
            @Override
            public int compare(FlujoMonetarioContrato o1, FlujoMonetarioContrato o2) {
                return o1.getFechaMovimiento().compareTo(o2.getFechaMovimiento());
            }
        });
        //rellenando la columna saldo acumulado con los datos ordenados por fecha
        FlujoMonetarioContrato fmcAnterior = null;
        for (int i = 0; i < results.size(); i++) {
            double saldoAnterior = 0.0;
            if (fmcAnterior!=null){
                saldoAnterior = fmcAnterior.getSaldoAcumulado();
            }
            FlujoMonetarioContrato fmc = results.get(i);
            fmc.setSaldoAcumulado(saldoAnterior + fmc.getImporteDebe() - fmc.getImporteHaber());
            fmcAnterior = fmc;
        }
        flujosDc.setItems(results);
        return results;
    }

    @Subscribe("fechaDesde")
    public void onFechaDesdeValueChange(HasValue.ValueChangeEvent<Date> event) {
        flujosDl.load();
    }

    @Subscribe("fechaHasta")
    public void onFechaHastaValueChange(HasValue.ValueChangeEvent<Date> event) {
        flujosDl.load();
    }

    @Subscribe("pckContrato")
    public void onPckContratoValueChange(HasValue.ValueChangeEvent<ContratoInquilino> event) {
        fechaDesde.setValue(pckContrato.getValue().getFechaOcupacion());
        Date d = new Date();
        if (pckContrato.getValue().getFechaDesocupacion()!=null){
            d = pckContrato.getValue().getFechaDesocupacion();
        }
        fechaHasta.setValue(d);
        flujosDl.load();
    }


    public void imprimirReport(){
        List<Entity> entities = new ArrayList(flujosDc.getItems());
        try {
            ContratoInquilino ci = dataManager.reload(pckContrato.getValue(), "contratoInquilino-view");
            String title = "MOVIMIENTOS DE CONTRATO " + pckContrato.getValue().getNumeroContrato() + " " +
                    pckContrato.getValue().getInquilino().getNombreCompleto() + " " +
                    pckContrato.getValue().getDepartamento().getNombreDescriptivoCompleto();
            byte[] bb = jasperReportService
                    .getReportDinamico(title, FlujoMonetarioContrato.class, entities,
                    Arrays.asList(new String[]{"informacionRecibo", "nominalRecibo", "fechaMovimiento", "importeDebe", "importeHaber", "saldoAcumulado", "concepto"}),
                            Arrays.asList(new String[]{"Num.Rbo", "Total Rbo.", "Fecha Rbo/Movto", "D","H","Saldo", "Concepto"}), new Hashtable<>());
            exportDisplay.show(new ByteArrayDataProvider(bb), title, ExportFormat.getByExtension("pdf") );

        }catch(Exception exc){
            notifications.create().withCaption("Error").withDescription(exc.getMessage()).show();
        }
    }
    



}