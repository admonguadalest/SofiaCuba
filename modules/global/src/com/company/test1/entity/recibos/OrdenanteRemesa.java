package com.company.test1.entity.recibos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ORDENANTE_REMESA")
@Entity(name = "test1_OrdenanteRemesa")
public class OrdenanteRemesa extends StandardEntity {
    private static final long serialVersionUID = 3625167209442731094L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFINICION_REMESA_ID")
    protected DefinicionRemesa definicionRemesa;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REMESA_ID")
    protected Remesa remesa;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ordenanteRemesa")
    protected List<Recibo> recibos = new ArrayList<Recibo>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public List<Recibo> getRecibos() {
        return recibos;
    }

    public void setRecibos(List<Recibo> recibos) {
        this.recibos = recibos;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Remesa getRemesa() {
        return remesa;
    }

    public void setRemesa(Remesa remesa) {
        this.remesa = remesa;
    }

    public DefinicionRemesa getDefinicionRemesa() {
        return definicionRemesa;
    }

    public void setDefinicionRemesa(DefinicionRemesa definicionRemesa) {
        this.definicionRemesa = definicionRemesa;
    }
}