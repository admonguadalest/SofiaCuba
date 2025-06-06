package com.company.test1.service;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.ordenespago.*;
import com.haulmont.cuba.core.global.PersistenceHelper;

import javax.inject.Inject;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public interface OrdenPagoService {
    String NAME = "test1_OrdenPagoService";



    List<OrdenPago> devuelveOrdenesPagoPendientesDeCompensacion(Proveedor prov, boolean incluirCompensadas);
    Double getTotalImporteCompensadoDeOrdenPago(OrdenPago op);
    void guardaOrdenPagoFacturaProveedor(OrdenPagoFacturaProveedor opfp);
    List<CuentaBancaria> devuelveCuentasBancariasPropietariosRegistrados();
    RealizacionPago crearRealizacionPagoDesdeListaOrdenesPago(List<OrdenPago> oopp, CuentaBancaria cb) throws Exception ;
    void guardaRealizacionPago(RealizacionPago rp);
    String crearIdentificadorParaRealizacionPago(Date fecha);
    OrdenPagoFacturaProveedor devuelveOrdenPagoFacturaProveedor(FacturaProveedor fp);
    OrdenPagoAbono devuelveOrdenPagoAbono(FacturaProveedor fp);
//    String getNombreEmisor(OrdenPago op);

}