create table TEST1_DATO_DE_CONTACTO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DATO varchar(255),
    DESCRIPCION_DATO varchar(255),
    TIPO_DE_DATO varchar(50),
    PERSONA_ID varchar(32),
    CORREO_DE_RECUPERACION_DE_CONSTRASENYA boolean,
    --
    primary key (ID)
);