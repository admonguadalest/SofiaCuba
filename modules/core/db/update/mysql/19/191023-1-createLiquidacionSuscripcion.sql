create table TEST1_LIQUIDACION_SUSCRIPCION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ES_RENOVACION boolean,
    FIANZA_CONTRATO double precision,
    GARANTIAS_EN_EFECTIVO double precision,
    DESC_RECIBOS_A_CUENTA varchar(255),
    DESC_DEVOLUCION_FIANZA_CONTRATO_RENUNCIADO varchar(255),
    DEVOLUCION_FIANZA_CONTRATO_RENUNCIADO double precision,
    GASTOS_CONTRATO double precision,
    TOTAL_LIQUIDACION double precision,
    DETALLE longtext,
    ESCANEO_INGRESO_LIQUIDACION_ID varchar(32),
    CONTRATO_INQUILINO_ID varchar(32),
    OTROS_CONCEPTOS double precision,
    CANTIDADES_ENTREGADAS_EN_RESERVA double precision,
    FECHA_INGRESO_ITP date,
    ESCANEO_RESGUARDO_ITP_ID varchar(32),
    --
    primary key (ID)
);