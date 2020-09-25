create table TEST1_CONCEPTO_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONCEPTO_ID varchar(32),
    IMPORTE double precision,
    PROGRAMACION_RECIBO_ID varchar(32),
    VIGENCIA integer,
    ACTIVADO_DESACTIVADO boolean,
    FECHA_DESDE date,
    FECHA_HASTA date,
    NUM_EMISIONES integer,
    ES_MODIFICACION_AGREGADA boolean,
    FECHA_VALOR date,
    DESCRIPCION_CAUSA varchar(255),
    ESTADO_NOTIFICACION integer,
    ACTUALIZABLE_IPC boolean,
    INCREMENTO_ID varchar(32),
    OMITIR_EN_PRORRATEO boolean,
    --
    primary key (ID)
);