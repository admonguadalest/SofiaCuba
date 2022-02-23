package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Service(NoEmitidosOAnomalosService.NAME)
public class NoEmitidosOAnomalosServiceBean implements NoEmitidosOAnomalosService {

    @Inject
    private DataManager dataManager;
    @Inject
    private ContratosService contratoService;

    @Override
    public List<Departamento> getDepartamentosMonitorizados() throws Exception {
        String eql = "select d from test1_Departamento d where d.excluirDeMonitorizacionNoEmitidosOAnomalos is null or " +
                "d.excluirDeMonitorizacionNoEmitidosOAnomalos = false";
        List<Departamento> dd = dataManager.load(Departamento.class).query(eql).view("departamento-view").list();
        return dd;
    }

    /**
     * Si no hay contrato vigente se reporta. Si lo hay, si las cantidades vigentes son inferiores al nominal del contrato también se reporta
     * @param d
     * @return
     * @throws Exception
     */
    @Override
    public Object[] reportarDepartamento(Departamento d, Date fechaDesde, Date fechaHasta) throws Exception {
        ContratoInquilino ci = contratoService.devuelveContratoVigenteParaDepartamento(d);
        if (ci==null){
            return new Object[]{true, "NO EXISTE CONTRATO VIGENTE"};
        }else{
            double q = devuelveCantidadesEmitidasEntreFechas(ci, fechaDesde, fechaHasta);
            if (q < ci.getRentaContractual()){
                return new Object[]{true, "IMPORTES EMITIDOS INFERIORES A RENTA NOMINA."};
            }
            return new Object[]{false, "nada a reportar"};
        }

    }

    @Override
    public double devuelveCantidadesEmitidasEntreFechas(ContratoInquilino ci, Date fechaDesde, Date fechaHasta) throws Exception {
        String eql = "select r from test1_Recibo r where r.utilitarioContratoInquilino.id = :ciid and r.fechaEmision >= :fd " +
                "and r.fechaEmision <= :fh";
        List<Recibo> rr = dataManager.load(Recibo.class).query(eql)
                .parameter("ciid", ci.getId()).parameter("fd", fechaDesde).parameter("fh", fechaHasta)
                .list();
        double sum = 0.0;
        for (int i = 0; i < rr.size(); i++) {
            Recibo r = rr.get(i);
            sum += r.getTotalRecibo();
        }
        return sum;

    }
}