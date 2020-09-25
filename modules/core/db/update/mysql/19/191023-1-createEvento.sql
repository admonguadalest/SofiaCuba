create table EVENTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CICLO_ID varchar(32),
    NOMBRE varchar(255),
    FECHA date,
    ARCHIVADO boolean,
    TIPO_EVENTO integer,
    --
    primary key (ID)
);