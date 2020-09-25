create table COTITULAR_CONTRATO_INQUILINO (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CONTRATO_INQUILINO_ID varchar(32),
    COTITULAR_ID varchar(32),
    --
    primary key (ID)
);