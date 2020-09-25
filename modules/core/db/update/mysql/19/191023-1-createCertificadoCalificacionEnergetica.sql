create table CERTIFICADO_CALIFICACION_ENERGETICA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NUMERO_REGISTRO varchar(255),
    ARCHIVO_ADJUNTO_ID varchar(32),
    FECHA_VENCIMIENTO date,
    OBSERVACIONES longtext,
    DEPARTAMENTO_ID varchar(32),
    CALIFICACION_EMISIONES varchar(5),
    CALIFICACION_CONSUMO_ENERGETICO varchar(5),
    --
    primary key (ID)
);