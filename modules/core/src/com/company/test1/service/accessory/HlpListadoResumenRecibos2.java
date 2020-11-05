package com.company.test1.service.accessory;

import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.Remesa;
import com.company.test1.entity.recibos.accesorios.ConceptoOrdenacionComparator;
import com.company.test1.service.NumberUtilsService;
import com.company.test1.service.RecibosService;
import com.company.test1.service.RecibosServiceBean;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PersistenceHelper;

import java.text.SimpleDateFormat;
import java.util.*;



/**
 *
 * @author Carlos Conti
 */
public class HlpListadoResumenRecibos2 {

    /**
     * Metodo que devuelve dos hashtables con fincas como keys, para
     * @param rr
     * @return
     */

    public  MapsReport construyeEstructurasReportsDesdeListadoRemesas(List<Remesa> rr){

        try {
            TreeMap<Ubicacion, TreeMap> pams = new TreeMap();
            TreeMap<Ubicacion, ArrayList> recibosFincas = new TreeMap();
            TreeMap<String, Number> totalesAgregado = new TreeMap();
            totalesAgregado.put("P_NUMRECIBOS", 0);
            totalesAgregado.put("P_TOTALESBASE", 0.0);
            totalesAgregado.put("P_TOTALESIVA", 0.0);
            totalesAgregado.put("P_TOTALESIRPF", 0.0);
            totalesAgregado.put("P_TOTALES", 0.0);

            for (int i = 0; i < rr.size(); i++) {
                Remesa remesa = rr.get(i);

                List<Recibo> rbos = AppBeans.get(RecibosService.class).getTodosLosRecibosAsociadosParaRemesas(Arrays.asList(remesa));

                for (int k = 0; k < rbos.size(); k++) {
                    Recibo recibo = rbos.get(k);
                    if (recibo == null)  continue;
                    Transaction t = AppBeans.get(Persistence.class).createTransaction();
                    if (!PersistenceHelper.isNew(recibo)){
                        recibo = AppBeans.get(Persistence.class).getEntityManager().reload(recibo, "recibo-listado-recibos-view");
                    }

                    t.close();
                    Ubicacion u = recibo.getUtilitarioContratoInquilino().getDepartamento().getUbicacion();
                    TreeMap ht = null;
                    if (pams.containsKey(u)){
                        ht = pams.get(u);
                    }
                    if (ht == null) {
                        ht = new TreeMap();
                        ht.put("P_TOTALBASE", 0.0);
                        ht.put("P_TOTALES", 0.0);
                        ht.put("P_TOTALIVA", 0.0);
                        ht.put("P_TOTALIRPF", 0.0);
                        ht.put("P_NUMRECIBOS", 0);
                        ht.put("P_FECHA2", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                        ht.put("P_UBICACION", u.getNombre());
                        pams.put(u, ht);
                    }
                    HlpListadoResumenRecibos hlp = new HlpListadoResumenRecibos(recibo);

                    Hashtable<Concepto, Double> htListaTotalesConceptos = (Hashtable<Concepto, Double>) ht.get("P_LISTATOTALESCONCEPTOSUBICACION");
                    if (htListaTotalesConceptos == null) {
                        htListaTotalesConceptos = new Hashtable<Concepto, Double>();
                        ht.put("P_LISTATOTALESCONCEPTOSUBICACION", htListaTotalesConceptos);
                    }

                    anadirReciboAConceptoATotales(htListaTotalesConceptos, recibo);


                    ArrayList al = recibosFincas.get(u);
                    if (al == null) {
                        al = new ArrayList();
                        recibosFincas.put(u, al);
                    }
                    al.add(hlp);
                    double totalbase = (Double) ht.get("P_TOTALBASE");
                    totalbase += recibo.getTotalRecibo();
                    ht.put("P_TOTALBASE", totalbase);

                    double totales = (Double) ht.get("P_TOTALES");
                    totales += recibo.getTotalReciboPostCCAA();
                    ht.put("P_TOTALES", totales);

                    double totaliva = (Double) ht.get("P_TOTALIVA");
                    totaliva += getTotalIvaRecibo(recibo);
                    ht.put("P_TOTALIVA", totaliva);

                    double totalirpf = (Double) ht.get("P_TOTALIRPF");
                    totalirpf += getTotalIrpfRecibo(recibo);
                    ht.put("P_TOTALIRPF", totalirpf);

                    int numrecibos = (Integer) ht.get("P_NUMRECIBOS");
                    numrecibos += 1;
                    ht.put("P_NUMRECIBOS", numrecibos);

                    Integer nr = (Integer) totalesAgregado.get("P_NUMRECIBOS");
                    totalesAgregado.put("P_NUMRECIBOS", ++nr);

                    Double tb = (Double) totalesAgregado.get("P_TOTALESBASE");
                    tb += recibo.getTotalRecibo();
                    totalesAgregado.put("P_TOTALESBASE", tb);

                    Double tiva = (Double) totalesAgregado.get("P_TOTALESIVA");
                    tiva += getTotalIvaRecibo(recibo);
                    totalesAgregado.put("P_TOTALESIVA", tiva);

                    Double tirpf = (Double) totalesAgregado.get("P_TOTALESIRPF");
                    tirpf += getTotalIrpfRecibo(recibo);
                    totalesAgregado.put("P_TOTALESIRPF", tirpf);

                    Double tt = (Double) totalesAgregado.get("P_TOTALES");
                    tt += recibo.getTotalReciboPostCCAA();
                    totalesAgregado.put("P_TOTALES", tt);
                }
            }

            //ordenando los recibos en recibosFincas
            List<Ubicacion> su = new ArrayList(recibosFincas.keySet());
            for (int i = 0; i < su.size(); i++) {
                Ubicacion u = su.get(i);
                ArrayList al = recibosFincas.get(u);
                Collections.sort(al, HlpListadoResumenRecibos.getComparadorHlpListadoResumenRecibos());

                TreeMap ht = pams.get(u);
                anadirTotalesConceptosRecibo(ht, (Hashtable<Concepto, Double>) ht.get("P_LISTATOTALESCONCEPTOSUBICACION"));
            }
            return new MapsReport(pams, recibosFincas, totalesAgregado);
        } catch (Exception exc) {
            int y = 2;
            return null;
        }
    }

    public static Double getTotalIvaRecibo(Recibo r){

        double totales = AppBeans.get(RecibosService.class).getTotalConceptoAdicionalAplicadoARecibo("IVA", r);
        return totales;
    }

    public static Double getTotalIrpfRecibo(Recibo r){


        double totales = AppBeans.get(RecibosService.class).getTotalConceptoAdicionalAplicadoARecibo("IRPF", r);
        return totales;
    }


    public class MapsReport{
        public TreeMap pams;
        public TreeMap recibosFincas;
        public TreeMap totalesAgregado;

        public MapsReport(TreeMap pams, TreeMap recibosFincas, TreeMap totalesAgregado) {
            this.pams = pams;
            this.recibosFincas = recibosFincas;
            this.totalesAgregado = totalesAgregado;
        }

        public TreeMap getPams() {
            return pams;
        }

        public void setPams(TreeMap pams) {
            this.pams = pams;
        }

        public TreeMap getRecibosFincas() {
            return recibosFincas;
        }

        public void setRecibosFincas(TreeMap recibosFincas) {
            this.recibosFincas = recibosFincas;
        }

        public TreeMap getTotalesAgregado() {
            return totalesAgregado;
        }

        public void setTotalesAgregado(TreeMap totalesAgregado) {
            this.totalesAgregado = totalesAgregado;
        }
    }

    /*
     * Parte totales conceptos
     */


    private void anadirTotalesConceptosRecibo(TreeMap ht, Hashtable<Concepto, Double> htTotalesConceptos) {

        List<Concepto> cc = new ArrayList(htTotalesConceptos.keySet());
        Collections.sort(cc, new ConceptoOrdenacionComparator());


        for (int i = 0; i < cc.size(); i++) {
            Concepto c = cc.get(i);
            ht.put("P_COD" + (i + 1), c.getOrdenacion().toString());
            ht.put("P_DESC" + (i + 1), c.getTituloConcepto());

            Double importe = htTotalesConceptos.get(c);

            if (!c.getAdicionSustraccion()) {
                importe = importe * (-1);
            }


            importe = AppBeans.get(NumberUtilsService.class).roundTo2Decimals(importe); // Redondeamos la suma por si acaso

            ht.put("P_IMP" + (i + 1), importe.toString());
        }
    }


    private void anadirReciboAConceptoATotales(Hashtable<Concepto, Double> ht, Recibo r) {

        List<ImplementacionConcepto> iicc = r.getImplementacionesConceptos();

        iicc = AppBeans.get(RecibosService.class).getVersionAgregadaPorConceptos(iicc);
        for (int i = 0; i < iicc.size(); i++) {
            ImplementacionConcepto ic = iicc.get(i);
            Concepto conc = ic.getConcepto();

            double importe = ic.getImporte();

            importe = AppBeans.get(NumberUtilsService.class).roundTo2Decimals(importe);
            anadirConceptoATotales(ht, conc, importe);
        }
    }

    private void anadirConceptoATotales(Hashtable<Concepto, Double> ht, Concepto concepto, Double cantidad) {

        Double total = ht.get(concepto);
        if (total == null) {
            ht.put(concepto, cantidad);

        } else {
            ht.put(concepto, total + cantidad);

        }
    }
}