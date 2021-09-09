package com.company.test1.entity.recibos;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.TipoCobroProgramacionReciboEnum;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Table(name = "PROGRAMACION_RECIBO")
@Entity(name = "test1_ProgramacionRecibo")
public class ProgramacionRecibo extends StandardEntity {
    private static final long serialVersionUID = -3370325819467938033L;

    @Column(name = "PROPIETARIO_ES_EMISOR")
    protected Boolean propietarioEsEmisor;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "programacionRecibo")
    protected List<ConceptoRecibo> conceptosRecibo = new ArrayList<ConceptoRecibo>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "programacionRecibo")
    protected List<ProgramacionConceptoAdicional> programacionConceptosAdicionales = new ArrayList<ProgramacionConceptoAdicional>();

    @Column(name = "TIPO_COBRO")
    protected Integer tipoCobro;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.DENY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @JoinColumn(name = "CUENTA_BANCARIA_INQUILINO_ID")
    protected CuentaBancaria cuentaBancariaInquilino;

    @NotNull(message = "NO PUEDE SER NULA")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFINICION_REMESA_ID")
    protected DefinicionRemesa definicionRemesa;

    @Column(name = "DESACTIVAR_PROGRAMACION")
    protected Boolean desactivarProgramacion = false;

    @Column(name = "APLICAR_IPC_NEGATIVO")
    protected Boolean aplicarIPCNegativo = false;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_PAGADOR_ID")
    protected CuentaBancaria cuentaBancariaPagador;

    @Column(name = "PRORRATEO_PROXIMA_EMISION")
    protected Double prorrateoProximaEmision;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public ProgramacionRecibo(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public ProgramacionRecibo() {
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<ConceptoRecibo> getConceptosRecibo() {
        return conceptosRecibo;
    }

    public void setConceptosRecibo(List<ConceptoRecibo> conceptosRecibo) {
        this.conceptosRecibo = conceptosRecibo;
    }

    public List<ProgramacionConceptoAdicional> getProgramacionConceptosAdicionales() {
        return programacionConceptosAdicionales;
    }

    public void setProgramacionConceptosAdicionales(List<ProgramacionConceptoAdicional> programacionConceptosAdicionales) {
        this.programacionConceptosAdicionales = programacionConceptosAdicionales;
    }

    public Double getProrrateoProximaEmision() {
        return prorrateoProximaEmision;
    }

    public void setProrrateoProximaEmision(Double prorrateoProximaEmision) {
        this.prorrateoProximaEmision = prorrateoProximaEmision;
    }

    public Boolean getAplicarIPCNegativo() {
        return aplicarIPCNegativo;
    }

    public void setAplicarIPCNegativo(Boolean aplicarIPCNegativo) {
        this.aplicarIPCNegativo = aplicarIPCNegativo;
    }

    public CuentaBancaria getCuentaBancariaPagador() {
        return cuentaBancariaPagador;
    }

    public void setCuentaBancariaPagador(CuentaBancaria cuentaBancariaPagador) {
        this.cuentaBancariaPagador = cuentaBancariaPagador;
    }

    public Boolean getDesactivarProgramacion() {
        return desactivarProgramacion;
    }

    public void setDesactivarProgramacion(Boolean desactivarProgramacion) {
        this.desactivarProgramacion = desactivarProgramacion;
    }

    public DefinicionRemesa getDefinicionRemesa() {
        return definicionRemesa;
    }

    public void setDefinicionRemesa(DefinicionRemesa definicionRemesa) {
        this.definicionRemesa = definicionRemesa;
    }

    public CuentaBancaria getCuentaBancariaInquilino() {
        return cuentaBancariaInquilino;
    }

    public void setCuentaBancariaInquilino(CuentaBancaria cuentaBancariaInquilino) {
        this.cuentaBancariaInquilino = cuentaBancariaInquilino;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public TipoCobroProgramacionReciboEnum getTipoCobro() {
        return tipoCobro == null ? null : TipoCobroProgramacionReciboEnum.fromId(tipoCobro);
    }

    public void setTipoCobro(TipoCobroProgramacionReciboEnum tipoCobro) {
        this.tipoCobro = tipoCobro == null ? null : tipoCobro.getId();
    }

    public Boolean getPropietarioEsEmisor() {
        return propietarioEsEmisor;
    }

    public void setPropietarioEsEmisor(Boolean propietarioEsEmisor) {
        this.propietarioEsEmisor = propietarioEsEmisor;
    }
}