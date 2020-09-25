create table PARAMETRO_VALOR (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_PARAMETRO varchar(255),
    VALOR longtext,
    DESCRIPCION_PARAMETRO varchar(255),
    IMPLEMENTACION_MODELO_ID varchar(32),
    --
    primary key (ID)
);