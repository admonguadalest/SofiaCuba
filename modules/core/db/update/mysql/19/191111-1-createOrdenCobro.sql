create table TEST1_ORDEN_COBRO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FECHA_VALOR date,
    IMPORTE double precision,
    DESCRIPCION varchar(255),
    REALIZACION_COBRO_ID varchar(32),
    RECIBO_ID varchar(32),
    ENT_TO_ENT_ID varchar(255),
    --
    primary key (ID)
);