package com.company.test1.service.accessory.reportprevisualizacionrecibo;

import com.company.test1.entity.Direccion;
import com.company.test1.entity.Persona;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.NombreTipoDireccion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

import java.text.SimpleDateFormat;
import java.util.*;

public class HelperRecibo {

    Recibo r = null;
    Persistence persistence = null;
    List<ImplementacionConcepto> licagg = new ArrayList<ImplementacionConcepto>();

    public HelperRecibo(Recibo r){

        this.r = r;
        persistence = AppBeans.get(Persistence.class);
        Transaction t = persistence.createTransaction();
        ContratoInquilino ci = r.getUtilitarioContratoInquilino();

        ci = persistence.getEntityManager().reload(ci, "contratoInquilino-reportrecibo-view");
        t.close();
        this.r = r;
        this.r.setUtilitarioContratoInquilino(ci);
        int y = 2;
    }

    public String getNumRecibo(){
        return r.getNumRecibo();
    }

    public Double getTotalRecibo(){
        return r.getTotalRecibo();
    }

    public Date getFechaExpedicion(){
        return r.getFechaEmision();

    }

    public String getPoblacionExpedicion(){
        return r.getUtilitarioContratoInquilino().getLugarRealizacion();
    }

    public String getVencimiento(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.r.getFechaEmision());
    }

    public String getReferenciaCatastral(){
        Departamento d = r.getUtilitarioContratoInquilino().getDepartamento();
        if (d.getUbicacion().getEsPropiedadVertical()){
            return d.getUbicacion().getInformacionCatastral();
        }else{
            return d.getReferenciaCatastral();
        }
    }

    public String getNifDniInquilino(){
        return r.getUtilitarioInquilino().getNifDni();
    }

    public String getDirprop1(){
        try {
            Propietario prop = r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo();
            Direccion dir = Direccion.getDireccionDesdeEnum(prop.getPersona(), NombreTipoDireccion.DOMICILIO_ADMINISTRADOR);

            return dir.getVia() + " " + dir.getNombreVia() + " " + dir.getNumeroVia();
        }catch(Exception exc){
            return "Datos de direccion linea 1 invalidos";
        }
    }

    public String getDirprop2(){

        try {
            Propietario prop = r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo();
            Direccion dir = prop.getPersona().getDirecciones().get(0); //pendiente solventar
            return dir.getCodigoPostal() + " " + dir.getPoblacion() + " (" + dir.getProvincia() + ") " ;
        }catch(Exception exc){
            return "Datos de direccion linea 2 invalidos";
        }
    }

    public String getNifDniPropietario(){
        Propietario prop = r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo();
        return prop.getPersona().getNifDni();
    }

    public String getNombreCompleto(){
        return r.getUtilitarioInquilino().getNombreCompleto();
    }

    public String getNombreCompletoPropietario(){
        Propietario prop = r.getUtilitarioContratoInquilino().getDepartamento().getPropietarioEfectivo();
        return prop.getPersona().getNombreCompleto();
    }

    public String getRecibo(){
        //en Sofia anterior era repercusionFacturaProveedor.recibo
        //pero no se bien bien lo que ha de ser
        return "";
    }

    public String getNombre50(){
        return r.getUtilitarioContratoInquilino().getLugarRealizacion();
    }

    public String getDirinquilino1(){
        try {
            Persona pers = r.getUtilitarioContratoInquilino().getInquilino();
            Direccion dir = pers.getDirecciones().get(0); //pendiente solventar
            return dir.getVia() + " " + dir.getNombreVia() + " " + dir.getNumeroVia();
        }catch(Exception exc){
            return "Datos de direccion linea 1 invalidos";
        }
    }

    public String getDirinquilino2(){

        try {
            Persona pers = r.getUtilitarioContratoInquilino().getInquilino();
            Direccion dir = pers.getDirecciones().get(0); //pendiente solventar
            return dir.getCodigoPostal() + " " + dir.getPoblacion() + " (" + dir.getProvincia() + ") " ;
        }catch(Exception exc){
            return "Datos de direccion linea 2 invalidos";
        }
    }

    public double getTotalIRPF(){
        double total = 0.0;

        for (int i = 0; i < r.getImplementacionesConceptos().size(); i++) {
            ImplementacionConcepto ic = r.getImplementacionesConceptos().get(i);

            if (ic.getRegistroAplicacionesConceptosAdicionales() == null) continue;

            total += getImporteAplicadoConSignoSegunConceptoAdicional(getRegistroAplicacionConceptosAdicionalPorNombre("IRPF", ic.getRegistroAplicacionesConceptosAdicionales()));
        }

        return total;
    }

    public double getTotalIVA(){
        double total = 0.0;

        for (int i = 0; i < r.getImplementacionesConceptos().size(); i++) {
            ImplementacionConcepto ic = r.getImplementacionesConceptos().get(i);

            if (ic.getRegistroAplicacionesConceptosAdicionales() == null) continue;

            total += getImporteAplicadoConSignoSegunConceptoAdicional(getRegistroAplicacionConceptosAdicionalPorNombre("IVA", ic.getRegistroAplicacionesConceptosAdicionales()));
        }

        return total;
    }

    public RegistroAplicacionConceptoAdicional getRegistroAplicacionConceptosAdicionalPorNombre(String nombre, List<RegistroAplicacionConceptoAdicional> list_raca){

        List<RegistroAplicacionConceptoAdicional> rraaccaa = list_raca;

        if (rraaccaa == null) return null;

        for (int i = 0; i < rraaccaa.size(); i++) {
            RegistroAplicacionConceptoAdicional raca = rraaccaa.get(i);
            if (raca.getConceptoAdicional().getNombre().equals(nombre)){
                return raca;
            }
        }

        return null;
    }

    public Double getImporteAplicadoConSignoSegunConceptoAdicional(RegistroAplicacionConceptoAdicional raca){

        if (raca == null){
            return 0.0;
        }
        if (raca.getConceptoAdicional().getAdicionSustraccion()) {
            return raca.getImporteAplicado();
        } else {
            return -raca.getImporteAplicado();
        }
    }

    public Double getTotalReciboPostCCAA(){
        return r.getTotalReciboPostCCAA();
    }

    public String getCuentaDePago(){

        return r.getUtilitarioContratoInquilino().getProgramacionRecibo().getCuentaBancariaPagador().getVersionIBAN();

    }

    public void setImplementacionesConceptosAgregadas(List<ImplementacionConcepto> icagg){
        //nothing
        Integer y = 2;
    }

    public List<ImplementacionConcepto> getImplementacionesConceptosAgregadas(){
        if (licagg.size()>0) return licagg;
        Hashtable ht = new Hashtable();
        for (int i = 0; i < this.r.getImplementacionesConceptos().size(); i++) {
            ImplementacionConcepto ic = this.r.getImplementacionesConceptos().get(i);
            ImplementacionConcepto icagg = null;
            if (ht.contains(ic.getConcepto().getAbreviacion())){
                icagg = (ImplementacionConcepto)ht.get(ic.getConcepto().getAbreviacion());
            }else{
                icagg = new ImplementacionConcepto();
                ht.put(ic.getConcepto().getAbreviacion(), icagg);
            }
            if ((icagg.getOverrideConcepto()==null)&&(icagg.getConcepto()==null)){
                if (ic.getConcepto()!=null){
                    icagg.setOverrideConcepto(ic.getConcepto());
                }else{
                    icagg.setOverrideConcepto(ic.getOverrideConcepto());
                }
            }

            if (icagg.getImporte()==null){
                icagg.setImporte(0.0);
                icagg.setImportePostCCAA(0.0);
            }
            double importe = ic.getImporte();
            double importePCCAA = ic.getImportePostCCAA();
            icagg.setImporte(icagg.getImporte()+importe);
            icagg.setImportePostCCAA(icagg.getImportePostCCAA()+importePCCAA);
        }
        Iterator iter = ht.keySet().iterator();
        while(iter.hasNext()){
            String k = (String) iter.next();
            ImplementacionConcepto icagg = (ImplementacionConcepto) ht.get(k);
            licagg.add(icagg);
        }
        return licagg;
    }






}
