create table TEST1_NOTIFICACION (
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
    CONTENIDO_IMPLEMENTADO longtext,
    ENVIADO boolean,
    FECHA_REALIZACION date,
    IMPLEMENTADO boolean,
    PLANTILLA_ID varchar(32),
    VERSION_PDF longblob,
    FECHA_PROGRAMADA_ENVIO date,
    TITULO varchar(255),
    --
    -- from test1_NotificacionContratoInquilino
    CONTRATO_INQUILINO_ID varchar(32),
    --
    primary key (ID)
);