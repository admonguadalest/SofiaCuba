package com.company.test1.web.screens.notificaciones;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.DatoDeContacto;
import com.company.test1.entity.Persona;
import com.company.test1.entity.PersonaFisica;
import com.company.test1.entity.enums.TipoDeDatoDeContactoEnum;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.service.NotificacionService;
import com.company.test1.service.PdfService;
import com.company.test1.web.screens.DynamicReportHelper;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EmailAttachment;
import com.haulmont.cuba.core.global.EmailException;
import com.haulmont.cuba.core.global.EmailInfo;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.notificaciones.Notificacion;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@UiController("test1_Notificacion.browse")
@UiDescriptor("notificacion-browse.xml")
@LookupComponent("notificacionsTable")
@LoadDataBeforeShow
public class NotificacionBrowse extends StandardLookup<Notificacion> {

    @Inject
    private Table<NotificacionContratoInquilino> notificacionsTable;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private PdfService pdfService;
    @Inject
    private NotificacionService notificacionService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private EmailService emailService;
    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;

    public void onBtnVerReportClick() {
        byte[] bb = DynamicReportHelper.getReportDinamico("Notificaciones", NotificacionContratoInquilino.class, notificacionsTable);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Notificaciones.pdf");
    }

    public void onBtnEnvioMailClick(){
        dialogs.createOptionDialog(Dialogs.MessageType.CONFIRMATION).withCaption("Confirmar Acción")
                .withMessage("¿Está seguro que desea enviar esta notificación a su destinatario?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                            doSendNotification();
                        }),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();
    }

    private void doSendNotification(){
        Notificacion n = notificacionsTable.getSingleSelected();
        if (n==null){
            notifications.create().withCaption("Seleccionar una notificación a enviar.").show();
            return;
        }
        String emails = null;
        if (n instanceof NotificacionContratoInquilino){
            NotificacionContratoInquilino nci = (NotificacionContratoInquilino)n;
            Persona inquilino = nci.getContratoInquilino().getInquilino();
            if (inquilino instanceof PersonaFisica){
                inquilino = dataManager.reload(inquilino, "personaFisica-view");
            }else{
                inquilino = dataManager.reload(inquilino, "personaJuridica-view");
            }

            List<DatoDeContacto> ddc = inquilino.getDatosDeContacto();
            for (int i = 0; i < ddc.size(); i++) {
                DatoDeContacto d = ddc.get(i);
                if (d.getTipoDeDato()== TipoDeDatoDeContactoEnum.CORREO_ELECTRONICO){
                    if (emails == null){
                        emails = d.getDato().replace(";","").toLowerCase().trim();
                    }else{
                        emails += "," + d.getDato().replace(";","").toLowerCase().trim();
                    }
                }
            }
        }
        EmailInfo emailInfo = new EmailInfo("","","");
        emailInfo.setAddresses(emails);
        emailInfo.setCaption("NOTIFICACION IMPORTATE SOBRE SU VIVIENDA ARRENDADA");
        emailInfo.setBody("Ponemos a su disposición la notificación adjunta.\nAténtamente,\nLa Administración.");
        emailInfo.setBodyContentType(EmailInfo.HTML_CONTENT_TYPE);
        emailInfo.setFrom("noreply@cgc-guadalest.com");

        //email attachments
        List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
        List<ArchivoAdjunto> aas = null;
        EmailAttachment ea = new EmailAttachment(n.getVersionPdf(), "NOTIFICACION.pdf");

        emailInfo.setAttachments(new EmailAttachment[]{ea});
        try {
            emailService.sendEmail(emailInfo);
        } catch (EmailException e) {
            e.printStackTrace();
        }

        notifications.create().withCaption("Mailing programado exitósamente").show();

    }

    public void onBtnPdfClick() {


        byte[] bb = notificacionService.getVersionPdfConcatenada(new ArrayList(notificacionsTable.getSelected()));
        exportDisplay.show(new ByteArrayDataProvider(bb), "Notificaciones.pdf");
    }
}