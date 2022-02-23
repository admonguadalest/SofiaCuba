package com.company.test1.service;

import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.documentosImputables.DocumentoProveedor;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.enums.*;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.validaciones.Validacion;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(ValidacionesService.NAME)
public class ValidacionesServiceBean implements ValidacionesService {

    @Inject
    Persistence persistence;
    @Inject
    private DataManager dataManager;

    public void guardaCambiosEnValidacion(Validacion v){

        Transaction t = persistence.createTransaction();
        persistence.getEntityManager().merge(v);
        t.commit();

    }

    public List<ValidacionImputacionDocumentoImputable> devuelveValidacionesAcordeADatos(DocumentoImputableTipoEnum tdi, ValidacionEstado estado,
                                                                                         Date fechaDesde, Date fechaHasta, String direccion, String nombreProveedorNoDocto,
                                                                                         DepartamentoEstadoEnum estadoDepto, DepartamentoTipoEnum tipoDepartamento,
                                                                                         TipoCiclo tipoCiclo
                                                                                         ) throws Exception{
        Transaction t =
                persistence.createTransaction();
        Query q = persistence.getEntityManager().createQuery();
//        String hql = "select vidi, prov from test1_ValidacionImputacionDocumentoImputable vidi, test1_Proveedor prov " +
//                "JOIN vidi.imputacionDocumentoImputable idi JOIN idi.documentoImputable di " +
//                "JOIN prov.persona pers JOIN idi.ciclo c JOIN c.departamento d JOIN d.ubicacion u " +
//                "WHERE  type(di)=test1_DocumentoProveedor and treat(di as test1_DocumentoProveedor).proveedor.id= prov.id";
        String hql = "select vidi FROM test1_DocumentoProveedor di " +
                "JOIN di.imputacionesDocumentoImputable idi " +
                "JOIN idi.validacionImputacion vidi " +
                "JOIN idi.ciclo c " +
                "JOIN c.departamento d " +
                "JOIN d.ubicacion u " +
                "JOIN di.proveedor prov " +
                "JOIN prov.persona pers WHERE 1=1 ";
        if (estado != null){
            hql += " AND vidi.estadoValidacion = :ev";
            q.setParameter("ev", estado);
        }
        if (fechaDesde!=null){
            hql += " AND di.fechaEmision >= :fd";
            q.setParameter("fd", fechaDesde);
        }
        if (fechaHasta!=null){
            hql += " AND di.fechaEmision <= :fh";
            q.setParameter("fh", fechaHasta);
        }
        if (direccion!=null){
            hql += " AND CONCAT(u.nombre,' ',d.piso,' ', d.puerta) like :d";
            q.setParameter("d", "%" + direccion + "%");
        }
        if (nombreProveedorNoDocto!=null){
            hql += " AND CONCAT(pers.nombreCompleto, ' ',di.numDocumento) like :npnd";
            nombreProveedorNoDocto = nombreProveedorNoDocto.replace(" ", "%");
            q.setParameter("npnd", "%" + nombreProveedorNoDocto + "%");
        }
        if(tipoCiclo!=null){
            hql += " AND c.tipoCiclo = :tc";
            q.setParameter("tc", tipoCiclo);
        }
        if (tdi != null){
            if (tdi == DocumentoImputableTipoEnum.FACTURA){
                hql += " AND type(di) = test1_FacturaProveedor";
            }
            if (tdi == DocumentoImputableTipoEnum.PRESUPUESTO){
                hql += " AND type(di) = test1_Presupuesto";
            }
        }
        q.setQueryString(hql);
        List<ValidacionImputacionDocumentoImputable> lvidis = q.setMaxResults(500).getResultList();
        t.close();
        return lvidis;

    }

    public DocumentoImputable gestionaValidacionesImputacionesDocumentoImputableDesdeDocumentoImputable(DocumentoImputable di, boolean resetearEstadosValidacion) throws Exception{

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.MONTH, 6);
        cal.set(Calendar.YEAR, 2014);
        Date fechaInicio = cal.getTime();

        if (di.getFechaEmision().before(fechaInicio)) return null; // Para no crear validaciones de Documentos Imputables antiguos!

        List<ImputacionDocumentoImputable> listaIDI = di.getImputacionesDocumentoImputable();

        //se realizan gestiones solo si es un documento imputable de tipo factura o presupuesto
        if ((di instanceof FacturaProveedor) || (di instanceof Presupuesto)){
            Proveedor prov = ((DocumentoProveedor) di).getProveedor();
            if (!prov.getModoDePagoTelematico()){
                return di;
            }
        }

        for(int i = 0;i < listaIDI.size();i++){

            ImputacionDocumentoImputable idi = listaIDI.get(i);
            Departamento dep = idi.getCiclo().getDepartamento();
            Propietario prop = dep.getPropietarioEfectivo();


            ValidacionImputacionDocumentoImputable vidi = idi.getValidacionImputacion();

            if (vidi == null){
                vidi = new ValidacionImputacionDocumentoImputable();
                vidi.setPropietario(prop);
                vidi.setImputacionDocumentoImputable(idi);
                vidi.setEstadoValidacion(ValidacionEstado.PENDIENTE);
                idi.setValidacionImputacion(vidi);
            }else{
                if (resetearEstadosValidacion){
                    vidi.setEstadoValidacion(ValidacionEstado.PENDIENTE);
                }
            }


        }
        return di;
    }

    public ValidacionEstado devuelveEstadoValidacionDocumentoImputable(DocumentoImputable di)
            throws Exception{
        List<ImputacionDocumentoImputable> l = di.getImputacionesDocumentoImputable();
        ArrayList<ValidacionEstado> estados = new ArrayList<ValidacionEstado>();
        for (int i = 0; i < l.size(); i++) {
            ImputacionDocumentoImputable idi = l.get(i);
            idi = dataManager.reload(idi, "imputacionDocumentoImputable-comprobacionValidaciones-view");
            ValidacionImputacionDocumentoImputable vidi = idi.getValidacionImputacion();
            if (estados.indexOf(vidi.getEstadoValidacion())==-1){
                estados.add(vidi.getEstadoValidacion());
            }
        }
        if (estados.isEmpty()){
            throw new Exception("No se hallaron registros de validacion: (id " + di.getId() + ")");
        }else{
            if (estados.size()==1){
                return estados.get(0);
            }else{
                return ValidacionEstado.PARCIALMENTE_VALIDADO;
            }
        }


    }



}