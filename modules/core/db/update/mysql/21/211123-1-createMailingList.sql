create table TEST1_MAILING_LIST (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE varchar(255),
    DESCRIPCION longtext,
    COLECCION_ARCHIVOS_ADJUNTOS_ID varchar(32) not null,
    COMMA_SEPARATED_MAILING_LIST longtext,
    --
    primary key (ID)
);