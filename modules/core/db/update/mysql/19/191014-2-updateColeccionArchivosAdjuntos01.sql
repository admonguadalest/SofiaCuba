alter table TEST1_COLECCION_ARCHIVOS_ADJUNTOS add constraint FK_TEST1_COLECCION_ARCHIVOS_ADJUNTOS_ON_COLECCION_PARENT foreign key (COLECCION_PARENT_ID) references TEST1_COLECCION_ARCHIVOS_ADJUNTOS(ID);
create index IDX_TEST1_COLECCION_ARCHIVOS_ADJUNTOS_ON_COLECCION_PARENT on TEST1_COLECCION_ARCHIVOS_ADJUNTOS (COLECCION_PARENT_ID);
