package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TEST1_POLIZA_DE_SEGUROS")
@Entity(name = "test1_PolizaDeSeguros")
public class PolizaDeSeguros extends StandardEntity {
    private static final long serialVersionUID = 4010060840003333771L;
}