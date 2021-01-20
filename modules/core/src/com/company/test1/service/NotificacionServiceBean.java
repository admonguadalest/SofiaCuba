package com.company.test1.service;

import ch.qos.logback.classic.sift.AppenderFactoryUsingJoran;
import com.company.test1.NumberUtils;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.notificaciones.Notificacion;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.JRRenderable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(NotificacionService.NAME)
public class NotificacionServiceBean implements NotificacionService {

    @Inject
    PdfService pdfService;
    @Inject
    Persistence persistence;

    public byte[] getVersionPdf(Notificacion n){
        return n.getVersionPdf();
    }

    public byte[] getVersionPdfConcatenada(List<Notificacion> nn){
        List<InputStream> inputStreams = new ArrayList<InputStream>();
        for (int i = 0; i < nn.size(); i++) {
            Notificacion notificacion = nn.get(i);
            byte[] bb = notificacion.getVersionPdf();
            ByteArrayInputStream bais = new ByteArrayInputStream(bb);
            inputStreams.add(bais);
        }

        ByteArrayOutputStream concatenated = new ByteArrayOutputStream();

        pdfService.concatPdfs(inputStreams, concatenated, false);
        byte[] bb = concatenated.toByteArray();
        return bb;
    }

    private Hashtable getListaParametrosPlantilla(Notificacion n, Hashtable objetos) throws Exception{
        Hashtable ht = new Hashtable();
        int currPos0;
        if (n.getPlantilla() != null){
            currPos0 = n.getPlantilla().getContenidoPlantilla().indexOf("@[");
            while(currPos0!=-1){
                int pos1 = n.getPlantilla().getContenidoPlantilla().indexOf("]",currPos0);
                String nombrePam = n.getPlantilla().getContenidoPlantilla().substring(currPos0+2,pos1);
                Object valorPam = resuelveParametro(nombrePam, objetos);
                if (!(valorPam instanceof Date)){
                    valorPam = valorPam.toString();
                }
                ht.put(nombrePam, valorPam);
                currPos0 = n.getPlantilla().getContenidoPlantilla().indexOf("@[",currPos0+2);
            }
            return ht;
        }
        throw new Exception("La notificacion no tiene asignada una plantilla");


    }

    private Hashtable getListaParametrosPlantilla(String contenido, Hashtable objetos) throws Exception{
        Hashtable ht = new Hashtable();
        int currPos0;

        currPos0 = contenido.indexOf("@[");
        while(currPos0!=-1){
            int pos1 = contenido.indexOf("]",currPos0);
            String nombrePam = contenido.substring(currPos0+2,pos1);
            Object valorPam = resuelveParametro(nombrePam, objetos);
            if (!(valorPam instanceof Date)){
                valorPam = valorPam.toString();
            }
            ht.put(nombrePam, valorPam);
            currPos0 = contenido.indexOf("@[",currPos0+2);
        }
        return ht;




    }

    private Object resuelveParametro(String nombreParametro, Hashtable objetos)throws Exception{
        String[] elementosParametro = nombreParametro.split("\\.");
        Object currBase;
        if (elementosParametro.length>1){
            currBase = objetos.get(elementosParametro[0]);
            for (int a = 1; a < elementosParametro.length;a++){
                String elemento = elementosParametro[a];
                if (a < elementosParametro.length-1){
                    elemento = elemento.substring(0,1).toUpperCase() + elemento.substring(1);
                    String nombreMetodo = "get" + elemento;
                    Class c = currBase.getClass();
                    try{
                        Method m = c.getMethod(nombreMetodo);
                        currBase = m.invoke(currBase);
                    }catch(Exception exc){

                    }
                }else{
                    //resuelvo el valor
                    elemento = elemento.substring(0,1).toUpperCase() + elemento.substring(1);
                    String nombreMetodo = "get" + elemento;
                    Class c = currBase.getClass();
                    try{
                        Method m = c.getMethod(nombreMetodo);
                        currBase = m.invoke(currBase);
                    }catch(Exception exc){
                        exc.printStackTrace();
                    }
                    return currBase;
                }
            }
            return null;
        }else{
            Object o;
            try{
                o = objetos.get(nombreParametro);
                if (o==null){
                    return "";
                }
                return o;
            }catch(Exception exc){
                return "";
            }
        }
    }

    public String implementaContenido(String contenido, Hashtable ht, boolean verCamposVacios) throws Exception{

        Hashtable objetos = getListaParametrosPlantilla(contenido, ht);
        Iterator iter = objetos.keySet().iterator();
        while(iter.hasNext()){
            String k = (String) iter.next();
            ht.put(k, objetos.get(k));
        }
        Enumeration e = ht.keys();
        String contenidoImplementado = contenido;
        while(e.hasMoreElements()){
            String s = (String) e.nextElement();
            String np = "@[" + s + "]";
            String v = null;
            try{
                Object ov = ht.get(s);
                if (ov instanceof Date){
                    v = new SimpleDateFormat("dd-MM-yyyy").format(ov);
                }else{
                    v = (String) ov;
                }

                if (verCamposVacios) {
                    if (v.trim().length() > 0) {
                        contenidoImplementado = contenidoImplementado.replace(np, v);
                    }
                }else{
                    contenidoImplementado = contenidoImplementado.replace(np, v);
                }

            }catch(Exception exc){

            }

        }
        return contenidoImplementado;
    }

    public Notificacion implementaContenido(Notificacion n, Hashtable ht, boolean verCamposVacios) throws Exception{

        Hashtable objetos = getListaParametrosPlantilla(n, ht);
        Iterator iter = objetos.keySet().iterator();
        while(iter.hasNext()){
            String k = (String) iter.next();
            ht.put(k, objetos.get(k));
        }
        Enumeration e = ht.keys();
        String contenidoImplementado = n.getPlantilla().getContenidoPlantilla();
        while(e.hasMoreElements()){
            String s = (String) e.nextElement();
            String np = "@[" + s + "]";
            String v = null;
            try{
                Object ov = ht.get(s);
                if (ov instanceof Date){
                    v = new SimpleDateFormat("dd-MM-yyyy").format(ov);
                }else{
                    v = (String) ov;
                }

                if (verCamposVacios) {
                    if (v.trim().length() > 0) {
                        contenidoImplementado = contenidoImplementado.replace(np, v);
                    }
                }else{
                    contenidoImplementado = contenidoImplementado.replace(np, v);
                }

            }catch(Exception exc){

            }

        }
        n.setContenidoImplementado(contenidoImplementado);
        n.setImplementado(true);
        return n;
    }

    public Notificacion implementaContenidoManual(Notificacion n, String contenido, Hashtable ht, boolean verCamposVacios) throws Exception{

        Hashtable objetos = getListaParametrosPlantilla(n, ht);
        Iterator iter = objetos.keySet().iterator();
        while(iter.hasNext()){
            String k = (String) iter.next();
            ht.put(k, objetos.get(k));
        }
        Enumeration e = ht.keys();
        String contenidoImplementado = contenido;
        while(e.hasMoreElements()){
            String s = (String) e.nextElement();
            String np = "@[" + s + "]";
            String v = null;
            try{
                Object ov = ht.get(s);
                if (ov instanceof Date){
                    v = new SimpleDateFormat("dd-MM-yyyy").format(ov);
                }else{
                    v = (String) ov;
                }
                if (verCamposVacios) {
                    if (v.trim().length() > 0) {
                        contenidoImplementado = contenidoImplementado.replace(np, v);
                    }
                }else{
                    contenidoImplementado = contenidoImplementado.replace(np, v);
                }

            }catch(Exception exc){

            }

        }
        n.setContenidoImplementado(contenidoImplementado);
        n.setImplementado(true);
        return n;
    }

    public byte[] implementaVersionPdfVersionFlexReport(String contenido)
            throws Exception{




            JRRenderable jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("carta.svg");

            Hashtable ht = new Hashtable();
//            ht.put("propietarioId", p.getId());
            ht.put("contenidoNotificacion", contenido);
            ht.put("CARTA", jrr);

            //este metodo ha dejado de funcionar
            //byte[] bb = com.sofia.model.reports.flexreports.Productor.produceReport(llp, ht, sl, slExtDocs);

            //probamos esta variante

            FlexReport fr = AppBeans.get(JasperReportService.class).getFlexReportDesdeNombre("CARTA");
            ArrayList al = new ArrayList();
            if (fr.getForzarReportDeUnSoloRegistroVacio()){
                al.add(" ");
            }
            byte[] bb = AppBeans.get(JasperReportService.class).produceReport(fr, ht, al);

            return bb;


    }

    public Notificacion implementaVersionPdfVersionFlexReport(Notificacion n)
            throws Exception{
        if (n instanceof NotificacionContratoInquilino){
            NotificacionContratoInquilino nci = (NotificacionContratoInquilino) n;

            ContratoInquilino ci = nci.getContratoInquilino();
            Propietario p = ci.getDepartamento().getPropietarioEfectivo();

            JRRenderable jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("carta.svg");

            Hashtable ht = new Hashtable();
            ht.put("propietarioId", p.getId());
            ht.put("contenidoNotificacion", nci.getContenidoImplementado());
            ht.put("CARTA", jrr);

            //este metodo ha dejado de funcionar
            //byte[] bb = com.sofia.model.reports.flexreports.Productor.produceReport(llp, ht, sl, slExtDocs);

            //probamos esta variante

            FlexReport fr = AppBeans.get(JasperReportService.class).getFlexReportDesdeNombre("CARTA");
            ArrayList al = new ArrayList();
            if (fr.getForzarReportDeUnSoloRegistroVacio()){
                al.add(" ");
            }
            byte[] bb = AppBeans.get(JasperReportService.class).produceReport(fr, ht, al);

            nci.setVersionPdf(bb);
            return nci;
        }else{
            throw new Exception("Se esperaba una notificacion tipo NotificacionContratoInquilino");
        }

    }

    public List<NotificacionContratoInquilino> devuelveNotificacionesAsociadasAContrato(ContratoInquilino ci) throws Exception{
        Transaction t = persistence.createTransaction();
        List<NotificacionContratoInquilino> nnccii = persistence.getEntityManager().createQuery("select nci FROM test1_NotificacionContratoInquilino nci WHERE nci.contratoInquilino.id = :cid").setParameter("cid", ci.getId()).getResultList();
        t.close();
        return nnccii;
    }


    /**
     * Metodo accesorio a la notificacion de recibos pendientes. Deberia ubicarse en recibosService
     * Pendiente dirimir
     * @param ci
     * @return
     * @throws Exception
     */
    public List<Recibo> getRecibosPendientes(ContratoInquilino ci) throws Exception{

        List res = new ArrayList();
        String sql = "SELECT r.id, "
                + " r.total_recibo_post_ccaa AS TR, "
                + " SUM(CASE rc.modo_ingreso WHEN 1 THEN rc.total_ingreso ELSE 0 END) as IB, "
                + " SUM(CASE rc.modo_ingreso WHEN 2 THEN rc.total_ingreso ELSE 0 END) as IA, "
                + " SUM(CASE rc.modo_ingreso WHEN 3 THEN rc.total_ingreso ELSE 0 END) as D, "
                + " SUM(CASE rc.modo_ingreso WHEN 4 THEN rc.total_ingreso ELSE 0 END) as P "
                + " FROM recibo r LEFT JOIN recibo_cobrado rc on rc.recibo_id = r.id JOIN contrato_inquilino ci on r.utilitario_contrato_inquilino_id = ci.id " +
                " WHERE ci.id = '" + ci.getId().toString().replace("-","") + "' GROUP BY r.id, r.total_recibo_post_ccaa";
        Hashtable ht = new Hashtable();
        ht.put("uci_id", ci.getId().toString().replace("-",""));
        Transaction t = persistence.createTransaction();
        List<Object[]> rrcc = persistence.getEntityManager().createNativeQuery(sql).getResultList();
        t.close();
        for (int i = 0; i < rrcc.size(); i++) {
            Object[] oarr = (Object[]) rrcc.get(i);
            double d = (Double) oarr[2] + (Double) oarr[3] - (Double) oarr[4] - (Double) oarr[5];
            //pendiente eliminar despues de testeos
            if (oarr[1]==null){
                oarr[1] = 0.0;
            }
            double rci = (Double) oarr[1];

            d = NumberUtils.roundToNDecimals(d, 2.0);

            if (rci <= d){
                continue;
            }
            t = persistence.createTransaction();
//            Recibo recibo = (Recibo) sl.getInstance(Recibo.class, (Integer) oarr[0]);
            String uuid = (String) oarr[0];
            uuid = uuid.substring(0,8)+"-"+uuid.substring(8,12)+"-"+uuid.substring(12,16)+"-"+uuid.substring(16,20)+"-"+uuid.substring(20);
            Recibo recibo = (Recibo) persistence.getEntityManager().find(Recibo.class, UUID.fromString(uuid), "recibo-view");
            t.close();

           res.add(recibo);

        }
        return res;
    }







}