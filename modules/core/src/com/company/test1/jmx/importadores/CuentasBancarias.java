package com.company.test1.jmx.importadores;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class CuentasBancarias {

    Persona persona;
    Proveedor proveedor;


    public CuentaBancaria realizaImportacion(Integer cbid, Persistence persistence) throws Exception{
        CuentaBancaria cb = new CuentaBancaria();
        String sql = "SELECT * FROM cuentas_bancarias WHERE id = " + cbid;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){
            cb.setRm2id(r.getInt("id"));
            cb.setDigitosControl(r.getString("digitosControl"));
            cb.setEntidad(r.getString("entidad"));
            cb.setInfoContactoOficina(r.getString("infoContactoOficina"));
            cb.setNumeroCuenta(r.getString("numeroCuenta"));
            cb.setOficina(r.getString("oficina"));
            cb.setPersona(persona);
            cb.setProveedor(proveedor);
            cb.setDomicilioEntidadBancaria(r.getString("domicilioEntidadBancaria"));
            cb.setNombreEntidadBancaria(r.getString("nombreEntidadBancaria"));
            cb.setPais(r.getString("pais"));
            cb.setCodigoBIC(r.getString("codigoBic"));
            cb.setDigigosControlIBAN(r.getString("digitosControlIBAN"));
        }
        r.close();
        return cb;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public CuentaBancaria devuelveCuentaBancariaDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        if (id ==null) return null;
        Transaction t = persistence.createTransaction();
        String hql = "SELECT cb FROM test1_CuentaBancaria cb WHERE cb.rm2id = " + id;
        CuentaBancaria cb = (CuentaBancaria) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return cb;
    }


}
