create table TEST1_REPRESENTACION_LEGAL (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PERSONA_ID varchar(32),
    PERSONA_REPRESENTANTE_ID varchar(32) not null,
    EN_CALIDAD_DE varchar(50),
    --
    primary key (ID)
);