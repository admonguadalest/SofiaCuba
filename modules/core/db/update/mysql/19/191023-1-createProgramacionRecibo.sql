create table TEST1_PROGRAMACION_RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROPIETARIO_ES_EMISOR boolean,
    TIPO_COBRO integer,
    CONTRATO_INQUILINO_ID varchar(32),
    CUENTA_BANCARIA_INQUILINO_ID varchar(32),
    DEFINICION_REMESA_ID varchar(32) not null,
    DESACTIVAR_PROGRAMACION boolean,
    APLICAR_IPC_NEGATIVO boolean,
    CUENTA_BANCARIA_PAGADOR_ID varchar(32),
    PRORRATEO_PROXIMA_EMISION double precision,
    --
    primary key (ID)
);