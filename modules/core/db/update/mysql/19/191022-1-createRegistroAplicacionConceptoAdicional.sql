create table TEST1_REGISTRO_APLICACION_CONCEPTO_ADICIONAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONCEPTO_ADICIONAL_ID varchar(32),
    DOCUMENTO_IMPUTABLE_ID varchar(32),
    NIF_DNI varchar(50),
    FECHA_VALOR date,
    NUM_DOCUMENTO varchar(100),
    BASE_ double precision,
    PORCENTAJE double precision,
    IMPORTE_APLICADO double precision,
    --
    primary key (ID)
);