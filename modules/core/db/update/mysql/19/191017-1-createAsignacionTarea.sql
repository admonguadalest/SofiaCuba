create table TEST1_ASIGNACION_TAREA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROVEEDOR_ID varchar(32),
    ORDEN_TRABAJO_ID varchar(32),
    FECHA_PREVISTA date,
    FECHA_FINALIZACION date,
    DESCRIPCION longtext,
    CANCELADO boolean,
    FECHA_PREVISTO_INICIO date,
    ACORDADO_CON_INDUSTRIAL boolean,
    COMENTARIOS_INDUSTRIAL longtext,
    --
    primary key (ID)
);