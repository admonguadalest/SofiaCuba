create table VALIDACION (
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
    PROPIETARIO_ID varchar(32),
    ESTADO_VALIDACION integer,
    FECHA_APROBACION_RECHAZO date,
    RM2ID integer,
    --
    -- from test1_ValidacionImputacionDocumentoImputable
    IMPUTACION_DOCUMENTO_IMPUTABLE_ID varchar(32),
    --
    primary key (ID)
);