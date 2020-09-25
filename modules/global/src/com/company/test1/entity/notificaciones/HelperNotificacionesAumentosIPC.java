package com.company.test1.entity.notificaciones;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.company.test1.NumberUtils;
import com.company.test1.StringUtils;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.incrementos.Incremento;
import com.company.test1.entity.incrementos.IncrementoIndice;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.haulmont.cuba.core.global.DataManager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


/**
 *
 * @author Carlos Conti
 */
public class HelperNotificacionesAumentosIPC implements Serializable {

    List<ConceptoRecibo> ccrr = null;
    ConceptoRecibo conceptoRecibo = null;

    DataManager dataManager;


    Boolean aplicarIPCNegativo;


    /**
     * Aprovecho la funcion para el caso en que se apunten más conceptos. En este caso de usuario solo se pondrá el concepto de incremento de renta, pero
     * la funcion con varios conceptos en ContratoInquilino, es valida tambien:
     * ContratoInquilino.getContratosVigentesAsociadosAUbicacionesYConceptosRecibosCreadosDesdeFechaParaConceptos
     * Asi que la aprovecho, y devolvera un hashtable <concepto,list<conceptorecibo>>, pero en realidad albergará tan solo un conceptorecibo correspondiente
     * a un incremento de renta.
     * @param cr
     */
    public HelperNotificacionesAumentosIPC(List<ConceptoRecibo> cr) {
        this.ccrr = cr;
        this.conceptoRecibo = ccrr.get(0);





        this.aplicarIPCNegativo = this.conceptoRecibo.getProgramacionRecibo().getAplicarIPCNegativo();
    }



    public String getTextoMesAnyoAplicacion(){
        Date fechaValor = conceptoRecibo.getFechaValor();
        Calendar c = Calendar.getInstance();
        c.setTime(fechaValor);

        int mes = c.get(Calendar.MONTH);
        int anno = c.get(Calendar.YEAR);

        String txt = StringUtils.getMesDeNumero(mes + 1) + " de " + anno;
        return txt;
    }

    public String getRentaAnterior() throws Exception{
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        double ra = getRentaAnteriorNumero();

        return nf.format(ra);
    }

    public String getIndiceInicial() throws Exception{
        Incremento i = conceptoRecibo.getIncremento();
        double ra = 0.0;
        if (i instanceof IncrementoIndice){
            IncrementoIndice ii = (IncrementoIndice) i;
            ra = ii.getIndiceAnterior();
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(ra);
    }

    public String getIndiceFinal() throws Exception{
        Incremento i = conceptoRecibo.getIncremento();
        double ra = 0.0;
        if (i instanceof IncrementoIndice){
            IncrementoIndice ii = (IncrementoIndice) i;
            ra = ii.getValorIndice();
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(ra);

    }

    public String getPorcentajeAumento() throws Exception{
        double ra = getPorcentajeAumentoNumero();

        NumberFormat nf = new DecimalFormat("#,##0.000");
        return nf.format(ra) + "%";
    }

    public String getImporteAumentoRenta() throws Exception{
        double aum = getImporteAumentoRentaNumero();

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return nf.format(aum);
    }

    public String getImporteAtrasos(){
        Incremento i = conceptoRecibo.getIncremento();
        double ia = 0.0;
        if (i instanceof IncrementoIndice){
            IncrementoIndice ii = (IncrementoIndice) i;
            ia = ii.getImporteAtrasos();
            ia = NumberUtils.roundToNDecimals(ia, 2.0);
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return nf.format(ia);
    }

    public String getRentaFinal() throws Exception{
        double rf = getRentaAnteriorNumero() + getImporteAumentoRentaNumero();

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return nf.format(rf);
    }

    public String getFechaRealizacionContrato(){
        Date d = conceptoRecibo.getProgramacionRecibo().getContratoInquilino().getFechaRealizacion();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int anno = c.get(Calendar.YEAR);

        return dia + " de " + StringUtils.getMesDeNumero(mes + 1) + " de " + anno;
    }

    public String getDireccionDepartamento(){
        Departamento d = conceptoRecibo.getProgramacionRecibo().getContratoInquilino().getDepartamento();
        Ubicacion u = d.getUbicacion();
        String direccionNombre = u.getDireccion().getDireccionParaDocumento() + d.getPiso() + " " + d.getPuerta();
        return direccionNombre;
    }

    public String getTablaHtmlDesgloseAumento() throws Exception{

        ArrayList keys = new ArrayList();
        keys.add("Renta que satisface(n)");
        keys.add("Indice inicial");
        keys.add("Indice final");
        keys.add("Porcentaje aumento");
        keys.add("Importe aumento renta");
        keys.add("Importe atrasos aumento renta");
        keys.add("Renta final");

        ArrayList vals = new ArrayList();
        vals.add(this.getRentaAnterior());
        vals.add(this.getIndiceInicial());
        vals.add(this.getIndiceFinal());
        vals.add(this.getPorcentajeAumento());
        vals.add(this.getImporteAumentoRenta());
        vals.add(this.getImporteAtrasos());
        vals.add(this.getRentaFinal());

        String html = "<p><table>";

        for (int i = 0; i < vals.size(); i++) {
            String v  = (String) vals.get(i);
            String l = (String) keys.get(i);
            html += "<tr><td>"  + l + "</td><td>" + v + "</td></tr>";
        }
        html += "</table></p>";
        return html;
    }

    private double getRentaAnteriorNumero() throws Exception{
        Incremento i = conceptoRecibo.getIncremento();
        double ra = 0.0;
        if (i instanceof IncrementoIndice){
            IncrementoIndice ii = (IncrementoIndice) i;
            ra = ii.getValorBase();
        }
        return ra;
    }

    private double getPorcentajeAumentoNumero(){

        Incremento i = conceptoRecibo.getIncremento();
        double ra = 0.0;
        if (i instanceof IncrementoIndice) {
            IncrementoIndice ii = (IncrementoIndice) i;
            ra = ii.getValorIndicePorcentual() * 100;
            ra = NumberUtils.roundToNDecimals(ra, 3.0);

            if ((ii.getValorIndicePorcentual() < 0) && (!aplicarIPCNegativo)) {
                ra = 0.0;
            }
        }

        return ra;
    }

    private double getImporteAumentoRentaNumero() throws Exception{
        Incremento i = conceptoRecibo.getIncremento();
        double aum = 0.0;
        if (i instanceof IncrementoIndice){
            aum = i.getImporte();
            aum = NumberUtils.roundToNDecimals(aum, 2.0);

            if ((aum < 0) && (!aplicarIPCNegativo)){
                aum = 0.0;
            }
        }

        return aum;
    }
}