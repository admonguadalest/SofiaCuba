alter table TEST1_MAILING_LIST add constraint FK_TEST1_MAILING_LIST_ON_COLECCION_ARCHIVOS_ADJUNTOS foreign key (COLECCION_ARCHIVOS_ADJUNTOS_ID) references COLECCION_ARCHIVOS_ADJUNTOS(ID);
create index IDX_TEST1_MAILING_LIST_ON_COLECCION_ARCHIVOS_ADJUNTOS on TEST1_MAILING_LIST (COLECCION_ARCHIVOS_ADJUNTOS_ID);
