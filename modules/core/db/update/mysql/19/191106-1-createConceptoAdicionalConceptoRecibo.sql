create table CONCEPTO_ADICIONAL_CONCEPTO_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CA_ID varchar(32),
    CONCEPTO_RECIBO_ID varchar(32),
    PORCENTAJE double precision,
    --
    primary key (ID)
);