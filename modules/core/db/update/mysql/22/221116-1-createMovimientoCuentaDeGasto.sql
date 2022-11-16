create table TEST1_MOVIMIENTO_CUENTA_DE_GASTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CUENTA_DE_GASTO_ID varchar(32),
    TIPO_DE_GASTO varchar(255),
    FECHA date,
    IMPORTE_BASE double precision,
    IMPORTE_POST_CCAA double precision,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    FACTURA_PROVEEDOR_ASOCIADO_ID varchar(32),
    --
    primary key (ID)
);