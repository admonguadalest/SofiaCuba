package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@PublishEntityChangedEvents
@Table(name = "ANEXO")
@Entity(name = "test1_Anexo")
public class Anexo extends StandardEntity {
    private static final long serialVersionUID = -954224455234106917L;

    @Column(name = "NOMBRE_ANEXO")
    protected String nombreAnexo;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "anexo", orphanRemoval = true)
    protected List<ParametroValorAnexo> parametrosValores = new ArrayList<ParametroValorAnexo>();

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANTILLA_ID")
    protected Plantilla plantilla;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    public List<ParametroValorAnexo> getParametrosValores() {
        return parametrosValores;
    }

    public void setParametrosValores(List<ParametroValorAnexo> parametrosValores) {
        this.parametrosValores = parametrosValores;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public String getNombreAnexo() {
        return nombreAnexo;
    }

    public void setNombreAnexo(String nombreAnexo) {
        this.nombreAnexo = nombreAnexo;
    }
}