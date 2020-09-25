create table ORDENANTE_REMESA (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DEFINICION_REMESA_ID varchar(32),
    REMESA_ID varchar(32),
    RM2ID integer,
    --
    primary key (ID)
);