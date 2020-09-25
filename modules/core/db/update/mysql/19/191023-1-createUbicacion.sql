create table UBICACION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255) not null,
    LATITUD double precision,
    LONGITUD double precision,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    --
    primary key (ID)
);