package com.company.test1.web.screens.presupuesto;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.service.PdfService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import com.company.test1.entity.documentosImputables.Presupuesto;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@UiController("test1_Presupuesto.browse")
@UiDescriptor("presupuesto-browse.xml")
@LookupComponent("presupuestoesTable")
@LoadDataBeforeShow
public class PresupuestoBrowse extends StandardLookup<Presupuesto> {

    @Inject
    private DataManager dataManager;
    @Inject
    private ColeccionArchivosAdjuntosService coleccionArchivosAdjuntosService;
    @Inject
    private PdfService pdfService;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Table<Presupuesto> presupuestoesTable;

    public void onBtnVerEscaneosClick() {
        List<DocumentoProveedor> ffpp = new ArrayList(presupuestoesTable.getSelected());
        List<byte[]> documentos = new ArrayList<byte[]>();
        for (int i = 0; i < ffpp.size(); i++) {
            DocumentoProveedor dp = ffpp.get(i);
            if (dp instanceof FacturaProveedor){
                dp = dataManager.reload(dp, "facturaProveedor-view");
            }
            if (dp instanceof Presupuesto){
                dp = dataManager.reload(dp, "presupuesto-view");
            }
            ArchivoAdjunto aa = dp.getColeccionArchivosAdjuntos().getArchivos().get(0);
            ArchivoAdjuntoExt aaext = coleccionArchivosAdjuntosService.getArchivoAdjuntoExt(aa);
            /* PENDIENTE IMPLEMENTAR OTRAS EXTENSIONES */
            if (aaext.getExtension().toLowerCase().compareTo("pdf")==0){
                byte[] bb = aaext.getRepresentacionSerial();
                bb = Base64.getMimeDecoder().decode(bb);
                bb = Base64.getMimeDecoder().decode(bb);
                documentos.add(bb);

            }
        }
        byte[] bb = pdfService.concatPdfs(documentos, false);
        exportDisplay.show(new ByteArrayDataProvider(bb), "Escaneos Presupuestos.pdf");
    }

}