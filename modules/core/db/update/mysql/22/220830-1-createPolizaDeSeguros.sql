create table TEST1_POLIZA_DE_SEGUROS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IDENTIFICADOR_POLIZA varchar(255),
    DESCRIPCION_ABREVIADA_RIESGO varchar(255),
    DESCRIPCION_AMPLIADA_RIESGO longtext,
    PRIMERA_FECHA_INICIAL date,
    FECHA_RESCISION date,
    ESCANEO_POLIZA_ID varchar(32),
    COMPANIA_ASEGURADORA varchar(255),
    BROKER varchar(255),
    --
    primary key (ID)
);