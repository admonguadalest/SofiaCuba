create table SERIE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NOMBRE_SERIE varchar(100),
    RM2ID integer,
    DESCRIPCION varchar(255),
    --
    primary key (ID)
);