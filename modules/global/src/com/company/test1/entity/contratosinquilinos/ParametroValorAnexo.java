package com.company.test1.entity.contratosinquilinos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "PARAMETRO_VALOR_ANEXO")
@Entity(name = "test1_ParametroValorAnexo")
public class ParametroValorAnexo extends StandardEntity {
    private static final long serialVersionUID = 705275779314201233L;

    @Column(name = "NOMBRE_PARAMETRO")
    protected String nombreParametro;

    @Column(name = "VALOR")
    protected String valor;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANEXO_ID")
    protected Anexo anexo;

    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }
}