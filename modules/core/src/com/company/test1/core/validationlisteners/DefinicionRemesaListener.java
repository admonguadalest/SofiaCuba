package com.company.test1.core.validationlisteners;

import com.company.test1.entity.enums.recibos.DefinicionRemesaModoPresentacionEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.listener.BeforeDeleteEntityListener;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;


@Component(DefinicionRemesaListener.NAME)
public class DefinicionRemesaListener implements BeforeDeleteEntityListener<DefinicionRemesa>, BeforeUpdateEntityListener<DefinicionRemesa>, BeforeInsertEntityListener<DefinicionRemesa> {
    public static final String NAME = "test1_DefinicionRemesaListener";


    @Override
    public void onBeforeDelete(DefinicionRemesa entity, EntityManager entityManager) {

    }

    @Override
    public void onBeforeInsert(DefinicionRemesa entity, EntityManager entityManager) {
        validate(entity, entityManager);
    }

    @Override
    public void onBeforeUpdate(DefinicionRemesa entity, EntityManager entityManager) {
        validate(entity, entityManager);

    }

    private void validate(DefinicionRemesa entity, EntityManager entityManager) throws ValidationException{
        if (entity.getTipoGiro() == DefinicionRemesaTipoGiroEnum.ADMINISTRACION){
            if (entity.getCuentaBancaria()!=null){
                throw new ValidationException("La cuenta bancaria debe ser nula en las definiciones de tipo Administracion");
            }
        }
        if (entity.getTipoGiro() == DefinicionRemesaTipoGiroEnum.BANCARIA){
            if (entity.getCuentaBancaria()==null){
                throw new ValidationException("Debe asignar un valor a cuenta bancaria para las definiciones de tipo bancaria");
            }
        }
        if (entity.getModoPresentacion()== DefinicionRemesaModoPresentacionEnum.PROPIETARIO){
            if (entity.getDelegado()!=null){
                throw new ValidationException("En modo de presentacion PROPIETARIO no puede asignarse un Delegado");
            }
        }

        if (entity.getModoPresentacion()== DefinicionRemesaModoPresentacionEnum.DELEGADO){
            if (entity.getDelegado()==null){
                throw new ValidationException("En modo de presentacion DELEGADO debe asignar un valor al campo Delegado");
            }
        }
    }
}