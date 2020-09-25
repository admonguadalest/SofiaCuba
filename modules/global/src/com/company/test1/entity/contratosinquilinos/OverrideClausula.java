package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "OVERRIDE_CLAUSULA")
@Entity(name = "test1_OverrideClausula")
public class OverrideClausula extends StandardEntity {
    private static final long serialVersionUID = -305413079982559588L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLAUSULA_ID")
    protected Clausula clausula;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSION_APLICADA_ID")
    protected VersionClausula versionAplicada;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPLEMENTACION_MODELO_ID")
    protected ImplementacionModelo implementacionModelo;

    public ImplementacionModelo getImplementacionModelo() {
        return implementacionModelo;
    }

    public void setImplementacionModelo(ImplementacionModelo implementacionModelo) {
        this.implementacionModelo = implementacionModelo;
    }

    public VersionClausula getVersionAplicada() {
        return versionAplicada;
    }

    public void setVersionAplicada(VersionClausula versionAplicada) {
        this.versionAplicada = versionAplicada;
    }

    public Clausula getClausula() {
        return clausula;
    }

    public void setClausula(Clausula clausula) {
        this.clausula = clausula;
    }
    
    
    public static OverrideClausula getOverrideClausula(ImplementacionModelo im, Clausula c){
        for (int i = 0; i < im.getOverrideClausulas().size(); i++) {
            OverrideClausula oc = im.getOverrideClausulas().get(i);
            if (oc.getClausula().getId().compareTo(c.getId())==0){
                return oc;
            }
        }
        return null;
    }
}