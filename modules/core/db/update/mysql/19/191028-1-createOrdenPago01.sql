create table ORDEN_PAGO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    DTYPE varchar(100),
    --
    FECHA_VALOR date,
    IMPORTE double precision,
    IMPORTE_EFECTIVO double precision,
    DESCRIPCION varchar(255),
    REALIZACION_PAGO_ID varchar(32),
    --
    -- from test1_OrdenPagoContratoInquilino
    CONTRATO_INQUILINO_ID varchar(32),
    --
    -- from test1_OrdenPagoProveedor
    PROVEEDOR_ID varchar(32),
    ES_COMPENSACION_ABONO boolean,
    --
    primary key (ID)
);