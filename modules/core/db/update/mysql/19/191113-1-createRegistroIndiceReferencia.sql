create table TEST1_REGISTRO_INDICE_REFERENCIA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    MES integer,
    ANNO integer,
    VALOR double precision,
    NOMBRE_TIPO varchar(50),
    REALIZADO_POR_PERSONA_ID varchar(32),
    --
    primary key (ID)
);