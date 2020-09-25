create table TEST1_DOCUMENTACION_INQUILINO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DNI varchar(255),
    NOMBRE_COMPLETO varchar(255),
    OBJETO_CANIDADO varchar(255),
    INFORMACION_DE_CONTACTO varchar(255),
    PRESENTACION longtext,
    FECHA_REGISTRO varchar(255),
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32),
    --
    primary key (ID)
);