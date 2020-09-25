create table ENTRADA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONTENIDO_ENTRADA longtext,
    FECHA_ENTRADA date,
    CICLO_ID varchar(32),
    EVENTO_ID varchar(32),
    ENTERO_RECORDATORIO integer,
    --
    primary key (ID)
);