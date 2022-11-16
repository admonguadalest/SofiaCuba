create table TEST1_CUENTA_DE_GASTO (
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
    TITULAR_ID varchar(32),
    AMPLIACION longtext,
    PERSONA_ID varchar(32),
    --
    primary key (ID)
);