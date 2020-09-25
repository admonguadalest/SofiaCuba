create table TEST1_NOTIFICACION_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_DESDE date,
    FECHA_HASTA date,
    CONTENIDO longtext,
    PROGRAMACION_RECIBO_ID varchar(32),
    CONCEPTO_RECIBO_ID varchar(32),
    --
    primary key (ID)
);