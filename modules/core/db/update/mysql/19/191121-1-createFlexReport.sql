create table TEST1_FLEX_REPORT (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    FIELDS varchar(255),
    FORZAR_REPORT_DE_UN_SOLO_REGISTRO_VACIO boolean,
    CONTENIDO_JRXML longtext,
    MODO_PRODUCTOR varchar(255),
    NOMBRE varchar(255),
    PARAMETROS_MANUALES longtext,
    PRODUCTOR longtext,
    TIPO varchar(255),
    PARAMETROS_PRODUCTOR longtext,
    RUTA varchar(255),
    USER_DATA_SOURCE_CONNECTION boolean,
    --
    primary key (ID)
);