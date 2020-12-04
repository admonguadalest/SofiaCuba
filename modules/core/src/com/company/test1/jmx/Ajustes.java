package com.company.test1.jmx;

import com.company.test1.entity.*;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.ordenespago.*;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.UUID;
import java.util.List;

@Component("test1_Ajustes")
public class Ajustes implements AjustesMBean {

    public void realizaAjustePoblarTitularDocumentosProveedor() throws Exception{
        String hql = "SELECT dp.id FROM test1_FacturaProveedor dp";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<UUID> ids = AppBeans.get(Persistence.class).getEntityManager().createQuery(hql).getResultList();
        t.close();
        for (int i = 0; i < ids.size(); i++) {
            UUID id = ids.get(i);
            FacturaProveedor fp = null;
            try {
                t = AppBeans.get(Persistence.class).createTransaction();
                fp = AppBeans.get(Persistence.class).getEntityManager().find(FacturaProveedor.class, id, "facturaProveedor-view");
                Persona titular = fp.getImputacionesDocumentoImputable().get(0).getCiclo().getDepartamento().getPropietarioEfectivo().getPersona();
                fp.setTitular(titular);
                t.close();
                AppBeans.get(DataManager.class).commit(fp);
            }catch(Exception exc){
                t.close();
            }

        }

    }

    public void realizaAjustePoblarTitularDocumentosProveedorAsociadosCicloR216Guadalest() throws Exception{
        String hql = "SELECT idi.id FROM test1_Ciclo c join c.imputacionesDocumentoImputable idi where c.id = '19065baeb5a3e63ce84b8cbe13f098a9'";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<UUID> ids = AppBeans.get(Persistence.class).getEntityManager().createQuery(hql).getResultList();
        Persona personaGuadalest = AppBeans.get(Persistence.class).getEntityManager().find(PersonaJuridica.class, UUID.fromString("65f91587-3af6-3aad-0eae-53e04a6a0adb"), "personaJuridica-view");
        t.close();
        for (int i = 0; i < ids.size(); i++) {
            UUID id = ids.get(i);
            ImputacionDocumentoImputable idi = null;
            try {
                t = AppBeans.get(Persistence.class).createTransaction();
                idi = AppBeans.get(Persistence.class).getEntityManager().find(ImputacionDocumentoImputable.class, id, "imputacionDocumentoImputable-view");
                DocumentoProveedor dp = (DocumentoProveedor) idi.getDocumentoImputable();
                dp.setTitular(personaGuadalest);
                t.close();
                AppBeans.get(DataManager.class).commit(dp);
            }catch(Exception exc){
                t.close();
            }

        }

    }

    public void realizaAjustePoblarTitularDocumentosProveedorAsociadosCicloR216Marvallos() throws Exception{
        String hql = "SELECT idi.id FROM test1_Ciclo c join c.imputacionesDocumentoImputable idi where c.id = '7e3354928b9709c86b2eb7f0864cdef0'";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<UUID> ids = AppBeans.get(Persistence.class).getEntityManager().createQuery(hql).getResultList();
        Persona personaMarvallos = AppBeans.get(Persistence.class).getEntityManager().find(PersonaJuridica.class, UUID.fromString("186876e1-3e1f-c30a-b0f6-c2b126a2fb31"), "personaJuridica-view");
        t.close();
        for (int i = 0; i < ids.size(); i++) {
            UUID id = ids.get(i);
            ImputacionDocumentoImputable idi = null;
            try {
                t = AppBeans.get(Persistence.class).createTransaction();
                idi = AppBeans.get(Persistence.class).getEntityManager().find(ImputacionDocumentoImputable.class, id, "imputacionDocumentoImputable-view");
                DocumentoProveedor dp = (DocumentoProveedor) idi.getDocumentoImputable();
                dp.setTitular(personaMarvallos);
                t.close();
                AppBeans.get(DataManager.class).commit(dp);
            }catch(Exception exc){
                t.close();
            }

        }

    }

    public void realizaAjustePoblarEmisorBeneficiarioOrdenesPago() throws Exception{
        String hql = "SELECT op.id FROM test1_OrdenPago op";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<UUID> ids = AppBeans.get(Persistence.class).getEntityManager().createQuery(hql).getResultList();
        t.close();
        for (int i = 0; i < ids.size(); i++) {
            UUID id = ids.get(i);
            OrdenPago op = null;
            try {
                t = AppBeans.get(Persistence.class).createTransaction();
                op = AppBeans.get(Persistence.class).getEntityManager().find(OrdenPago.class, id, "ordenPago-view");
                if (op instanceof OrdenPagoProveedor){
                    OrdenPagoProveedor opp = AppBeans.get(Persistence.class).getEntityManager().find(OrdenPagoProveedor.class, id, "ordenPagoProveedor-view");
                    //Emisor->Propietario de la cuenta bancaria del realizacion pago
                    if (opp.getRealizacionPago()!=null){
                        RealizacionPago rp = opp.getRealizacionPago();
                        CuentaBancaria cb = rp.getCuentaBancaria();
                        Persona emisor = cb.getPersona();
                        op.setEmisor(emisor);
                        op.setBeneficiario(opp.getProveedor().getPersona());
                    }
                }
                if (op instanceof OrdenPagoFacturaProveedor){
                    OrdenPagoFacturaProveedor opfp = AppBeans.get(Persistence.class).getEntityManager().find(OrdenPagoFacturaProveedor.class, id, "ordenPagoFacturaProveedor-view");
                    if (opfp.getRealizacionPago()!=null){
                        RealizacionPago rp = opfp.getRealizacionPago();
                        CuentaBancaria cb = rp.getCuentaBancaria();
                        Persona emisor = cb.getPersona();
                        op.setEmisor(emisor);
                        op.setBeneficiario(opfp.getFacturaProveedor().getProveedor().getPersona());
                    }
                }
                if (op instanceof OrdenPagoContratoInquilino){
                    OrdenPagoContratoInquilino opci = AppBeans.get(Persistence.class).getEntityManager().find(OrdenPagoContratoInquilino.class, id, "ordenPagoContratoInquilino-view");
                    if (opci.getRealizacionPago()!=null){
                        RealizacionPago rp = opci.getRealizacionPago();
                        CuentaBancaria cb = rp.getCuentaBancaria();
                        Persona emisor = cb.getPersona();
                        op.setEmisor(emisor);
                        op.setBeneficiario(opci.getContratoInquilino().getInquilino());
                    }
                }
                if (op instanceof OrdenPagoAbono){
                    OrdenPagoAbono opa = AppBeans.get(Persistence.class).getEntityManager().find(OrdenPagoAbono.class, id, "ordenPagoAbono-view");
                    if (opa.getFacturaProveedor()!=null){
                        FacturaProveedor fp = opa.getFacturaProveedor();
                        Persona emisor = fp.getTitular();
                        op.setEmisor(emisor);
                        op.setBeneficiario(fp.getProveedor().getPersona());
                    }
                }

                t.close();
                AppBeans.get(DataManager.class).commit(op);
            }catch(Exception exc){
                t.close();
            }

        }
    }

    private boolean isPdfFile(byte[] bb){
        try{
            PdfReader pdfr = new PdfReader(bb);
            int n = pdfr.getNumberOfPages();
            return true;
        }catch(Exception exc){
            return false;
        }

    }

    private boolean isImage(byte[] bb){
        try{
            BufferedImage im = ImageIO.read(new ByteArrayInputStream(bb));
            if ((im.getWidth()>0) && (im.getHeight()>0)){
                return true;
            }else{
                return false;
            }

        }catch(Exception exc){
            return false;
        }

    }

    public void rellenaTablaArchivoAdjuntoCorreccionHelper() throws Exception{

        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<UUID> aa = AppBeans.get(Persistence.class).getEntityManager().createQuery("select aa.id from test1_ArchivoAdjunto aa").getResultList();
        t.close();
        for (int i = 0; i < aa.size(); i++) {
            int doTransfer = 0;
            int numberDecodeLeves = 0;
            UUID id = aa.get(i);
            t = AppBeans.get(Persistence.class).createTransaction();
            ArchivoAdjunto a = AppBeans.get(Persistence.class).getEntityManager().find(ArchivoAdjunto.class, id, "_base");
            t.close();
            t = AppBeans.get(Persistence.class).createTransaction("rentamasterdocs");
            ArchivoAdjuntoExt aaext = (ArchivoAdjuntoExt) AppBeans.get(Persistence.class).getEntityManager("rentamasterdocs").createQuery("select aa FROM test1_ArchivosAdjuntos aa WHERE aa.id = :extid")
                    .setParameter("extid", a.getExtId()).getFirstResult();
            t.close();
            if (aaext == null){
                Connection c = Cubatest1DB.getConnection();
                String sql = "insert into archivos_adjuntos_correction_helper (archivo_adjunto_id, ext_id, decodelevels, dbtransfer) values ('" + a.getId().toString().replace("-","") + "'," + a.getExtId().toString() + ",NULL,NULL)";
                PreparedStatement s = c.prepareStatement(sql);
                s.execute();
                continue;
            }
            byte[] bb = aaext.getRepresentacionSerial();
            if (bb==null){
                bb = a.getRepresentacionSerial();
                doTransfer = 1;
            }
            String extension = a.getExtension();
            if (bb==null){
                Connection c = Cubatest1DB.getConnection();
                String sql = "insert into archivos_adjuntos_correction_helper (archivo_adjunto_id, ext_id, decodelevels, dbtransfer) values ('" + a.getId().toString().replace("-","") + "'," + a.getExtId().toString() + ",NULL,NULL)";
                PreparedStatement s = c.prepareStatement(sql);
                s.execute();
                continue;
            }
            if (extension.toLowerCase().compareTo("pdf")==0){
                bb = Base64.getMimeDecoder().decode(bb);
                if (isPdfFile(bb)){
                    numberDecodeLeves = 1;
                }else{
                    bb = Base64.getMimeDecoder().decode(bb);
                    if (isPdfFile(bb)){
                        numberDecodeLeves = 2;
                    }else{
                        numberDecodeLeves = -1;
                    }
                }
            }
            if ("jpg jpeg png bmp gif ".indexOf(extension.toLowerCase())!=-1){
                bb = Base64.getMimeDecoder().decode(bb);
                if (isImage(bb)){
                    numberDecodeLeves = 1;
                }else{
                    bb = Base64.getMimeDecoder().decode(bb);
                    if (isImage(bb)){
                        numberDecodeLeves = 2;
                    }else{
                        numberDecodeLeves = -1;
                    }
                }
            }
            Connection c = Cubatest1DB.getConnection();
            String sql = "insert into archivos_adjuntos_correction_helper (archivo_adjunto_id, ext_id, decodelevels, dbtransfer) values ('" + a.getId().toString().replace("-","") + "'," + a.getExtId().toString() + "," + numberDecodeLeves + "," + doTransfer + ")";
            PreparedStatement s = c.prepareStatement(sql);
            s.execute();
        }
    }

}