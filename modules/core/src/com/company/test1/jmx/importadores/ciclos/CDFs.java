package com.company.test1.jmx.importadores.ciclos;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.contratosinquilinos.LiquidacionExtincion;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;

import java.sql.ResultSet;

public class CDFs {

    Ciclo ciclo = null;
    Evento evento = null;
    LiquidacionExtincion liquidacionExtincion;

    public CarpetaDocumentosFotograficos devuelveCarpetaDesdeRm2Id(Integer rm2id) throws Exception{
        String hql = "SELECT cdf FROM test1_CarpetaDocumentosFotograficos cdf WHERE cdf.rm2id = :rm2id";
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        CarpetaDocumentosFotograficos cdf = (CarpetaDocumentosFotograficos) AppBeans.get(Persistence.class).getEntityManager().createQuery(hql).setParameter("rm2id", rm2id).getFirstResult();
        t.close();
        return cdf;
    }

    public CarpetaDocumentosFotograficos realizaImportacion(Integer id, Persistence persistence) throws Exception{
        CarpetaDocumentosFotograficos cdf = new CarpetaDocumentosFotograficos();
        String sql = "SELECT * FROM carpetas_documentos_fotograficos WHERE id = " + id;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){
            cdf.setNombreCarpeta(r.getString("nombreCarpeta"));
            cdf.setAportadasPor(r.getString("aportadasPor"));
            cdf.setCiclo(getCiclo());
            cdf.setEvento(getEvento());
            cdf.setDescripcion(r.getString("descripcion"));
            cdf.setNumeroDeFotografias(r.getInt("numeroDeFotografias"));
            cdf.setAccesibleParaComerciales(r.getBoolean("accesibleParaComerciales"));
            cdf.setLiquidacionExtincion(liquidacionExtincion);


        }
        r.close();
        return cdf;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LiquidacionExtincion getLiquidacionExtincion() {
        return liquidacionExtincion;
    }

    public void setLiquidacionExtincion(LiquidacionExtincion liquidacionExtincion) {
        this.liquidacionExtincion = liquidacionExtincion;
    }
}
