package com.company.test1.web.screens.contratoinquilino.fragments;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.company.test1.service.JasperReportService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

@UiController("test1_RenunciaFragment")
@UiDescriptor("renuncia-fragment.xml")
public class RenunciaFragment extends ScreenFragment {

    @Inject
    private JasperReportService jasperReportService;
    @Inject
    private Notifications notifications;

    InstanceContainer<ContratoInquilino> contratoInquilinoDc;
    @Inject
    private ExportDisplay exportDisplay;

    public void setContratoInquilinoDataContainer(InstanceContainer<ContratoInquilino> o){
        this.contratoInquilinoDc = o;
    }




    public void OnBtnVerModeloRenuncia(){
        try{
            byte[] bb = jasperReportService.generaReportModeloRenunciaContratoInquilino(contratoInquilinoDc.getItem());
            exportDisplay.show(new ByteArrayDataProvider(bb), "modelo_renuncia.pdf");
        }catch(Exception exc){
            notifications.create().withDescription(exc.getMessage()).withCaption("Error").show();
        }

    }

}