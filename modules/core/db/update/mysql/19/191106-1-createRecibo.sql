create table RECIBO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    AMPLIACION varchar(255),
    FECHA_EMISION date,
    TOTAL_RECIBO double precision,
    TOTAL_RECIBO_POST_CCAA double precision,
    PROGRAMACION_RECIBO_ID varchar(32),
    NUM_RECIBO varchar(100),
    FECHA_VALOR date,
    ORDENANTE_REMESA_ID varchar(32),
    SERIE_ID varchar(32),
    UTILITARIO_CONTRATO_INQUILINO_ID varchar(32),
    GRADO_ESTADO_IMPAGO integer,
    FECHA_ESTADO_PENDIENTE date,
    FECHA_ESTADO_INCOBRABLE date,
    UTILITARIO_INQUILINO_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
);