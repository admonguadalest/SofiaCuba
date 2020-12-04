package com.company.test1.web.screens.archivoadjunto;

import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.ArchivoAdjunto;
import com.itextpdf.text.pdf.PdfReader;


import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@UiController("test1_ArchivoAdjunto.browse")
@UiDescriptor("archivo-adjunto-browse.xml")
@LookupComponent("archivoAdjuntoesTable")
@LoadDataBeforeShow
public class ArchivoAdjuntoBrowse extends StandardLookup<ArchivoAdjunto> {

    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private Table<ArchivoAdjunto> archivoAdjuntoesTable;
    @Inject
    private Notifications notifications;
    @Inject
    private ExportDisplay exportDisplay;

    public void OnVisualizeBtn1Decode(){
        try {
            ArchivoAdjunto aa = archivoAdjuntoesTable.getSingleSelected();
            if (aa == null) {
                notifications.create().withCaption("Seleccion").withDescription("Seleccionar un registro").show();
            }
            ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
            byte[] bb = null;
            bb = aaext.getRepresentacionSerial();
            if (bb == null) bb = aa.getRepresentacionSerial();
            bb = Base64.getMimeDecoder().decode(bb);

            exportDisplay.show(new ByteArrayDataProvider(bb), aa.getNombreArchivo());
        }catch(Exception exc){

        }
    }
    public void OnVisualizeBtn2Decode(){
        try {
            ArchivoAdjunto aa = archivoAdjuntoesTable.getSingleSelected();
            if (aa == null) {
                notifications.create().withCaption("Seleccion").withDescription("Seleccionar un registro").show();
            }
            ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
            byte[] bb = null;
            bb = aaext.getRepresentacionSerial();
            if (bb == null) bb = aa.getRepresentacionSerial();
            bb = Base64.getMimeDecoder().decode(bb);
            bb = Base64.getMimeDecoder().decode(bb);

            exportDisplay.show(new ByteArrayDataProvider(bb), aa.getNombreArchivo());
        }catch(Exception exc){

        }
    }

    public void doCheck1D(){
        String messages = "";
        ArchivoAdjunto aa = archivoAdjuntoesTable.getSingleSelected();
        if (aa == null) {
            notifications.create().withCaption("Seleccion").withDescription("Seleccionar un registro").show();
        }
        ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
        byte[] bb = null;
        bb = aaext.getRepresentacionSerial();
        if (bb == null) {
            bb = aa.getRepresentacionSerial();
            messages += "El archivo adjunto tiene la representacion serial en la bbdd main y no la de documentos";
        }
        bb = Base64.getMimeDecoder().decode(bb);
        String extension = aa.getExtension();
        if (extension.compareTo("pdf")==0){
            try {
                PdfReader pdfr = new PdfReader(bb);
                int n = pdfr.getNumberOfPages();
                messages += "INCORRECTO: El archivo adjunto solo esta codificado 1 vez";
            }catch(Exception exc){
                messages += "CORRECTO: El documento no se puede procesar como PDF con 1 Decode";
            }

        }
        notifications.create().withDescription(messages).show();

    }

    public void doCheck2D(){
        String messages = "El archivo adjunto esta en estado correcto";
        ArchivoAdjunto aa = archivoAdjuntoesTable.getSingleSelected();
        if (aa == null) {
            notifications.create().withCaption("Seleccion").withDescription("Seleccionar un registro").show();
        }
        ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
        byte[] bb = null;
        bb = aaext.getRepresentacionSerial();
        if (bb == null) {
            bb = aa.getRepresentacionSerial();
            messages = "El archivo adjunto tiene la representacion serial en la bbdd main y no la de documentos";
        }
        bb = Base64.getMimeDecoder().decode(bb);
        bb = Base64.getMimeDecoder().decode(bb);
        String extension = aa.getExtension();
        if (extension.compareTo("pdf")==0){
            try {
                PdfReader pdfr = new PdfReader(bb);
                int n = pdfr.getNumberOfPages();
            }catch(Exception exc){
                int y = 2;
                messages += "El documento no se puede procesar como PDF";
            }
            int y = 2;
            messages += "El archivo adjunto esta en estado correcto en cuanto a DecodificacionF";
        }
        notifications.create().withDescription(messages).show();

    }




}