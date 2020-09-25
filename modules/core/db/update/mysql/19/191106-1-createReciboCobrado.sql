create table RECIBO_COBRADO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    AMPLIACION_INGRESO varchar(255),
    DESCRIPCION varchar(255),
    FECHA_COBRO date,
    MODO_INGRESO integer,
    TOTAL_INGRESO double precision,
    COBRANZAS double precision,
    RECIBO_ID varchar(32),
    --
    primary key (ID)
);