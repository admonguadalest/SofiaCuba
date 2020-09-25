create table COMERCIAL_OFERTAS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DETALLE_CORREOS_ELECTRONICOS longtext,
    DETALLE_NOMBRES longtext,
    EXCLUIR_ENVIOS boolean,
    PUEDE_DAR_ALTA_O_BAJA_AGENTES boolean,
    XML_COMERCIAL_VISITAS longtext,
    --
    primary key (ID)
);