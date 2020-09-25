create table TEST1_LIQUIDACION_EXTINCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TOTALES_GARANTIAS double precision,
    TOTALES_RECIBOS_PENDIENTES double precision,
    TOTALES_INDEMNIZACIONES double precision,
    POR_LIQUIDAR double precision,
    ESCANEO_LIQUIDACION_ID varchar(32),
    CONTRATO_INQUILINO_ID varchar(32),
    DETALLE longtext,
    IMPORTE_AVAL_EJECUTADO double precision,
    CANTIDADES_ENTREGADAS_EN_RESERVA double precision,
    CONFORMIDAD_ADMINISTRADOR boolean,
    RETENCION_CARPETA_DOCUMENTO_FOTOGRAFICO_ID varchar(32),
    CANTIDADES_A_CUENTA_LIQUIDACION double precision,
    FECHA_CANTIDADES_A_CUENTA_LIQUIDACION date,
    FECHA_LIQUIDACION date,
    --
    primary key (ID)
);