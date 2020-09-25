package com.company.test1.service.accessory.reportprevisualizacionrecibo;

import com.company.test1.entity.Direccion;
import com.company.test1.entity.Persona;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

import java.util.Date;
import java.util.List;

public class HelperRecibo {

    Recibo r = null;
    Persistence persistence = null;

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

    public Date getVencimiento(){
        return r.getUtilitarioContratoInquilino().getFechaVencimientoPrevisto();
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
            Direccion dir = prop.getPersona().getDirecciones().get(0); //pendiente solventar
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






}
