alter table COLECCION_ARCHIVOS_ADJUNTOS add constraint FK_COLECCION_ARCHIVOS_ADJUNTOS_ON_COLECCION_PARENT foreign key (COLECCION_PARENT_ID) references COLECCION_ARCHIVOS_ADJUNTOS(ID);
create index IDX_COLECCION_ARCHIVOS_ADJUNTOS_ON_COLECCION_PARENT on COLECCION_ARCHIVOS_ADJUNTOS (COLECCION_PARENT_ID);
