create table CUENTA_BANCARIA (
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
    ENTIDAD varchar(5),
    OFICINA varchar(5),
    DIGITOS_CONTROL varchar(5),
    NUMERO_CUENTA varchar(15),
    INFO_CONTACTO_OFICINA varchar(255),
    DOMICILIO_ENTIDAD_BANCARIA varchar(255),
    NOMBRE_ENTIDAD_BANCARIA varchar(255),
    PAIS varchar(50),
    CODIGO_BIC varchar(11),
    DIGIGOS_CONTROL_IBAN varchar(5),
    --
    primary key (ID)
);