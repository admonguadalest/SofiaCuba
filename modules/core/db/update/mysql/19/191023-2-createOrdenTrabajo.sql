alter table ORDEN_TRABAJO add constraint FK_ORDEN_TRABAJO_ON_ENTRADA foreign key (ENTRADA_ID) references ENTRADA(ID);
alter table ORDEN_TRABAJO add constraint FK_ORDEN_TRABAJO_ON_CARPETA_DOCUMENTOS_FOTOGRAFICOS foreign key (CARPETA_DOCUMENTOS_FOTOGRAFICOS_ID) references CARPETA_DOCUMENTOS_FOTOGRAFICOS(ID);
alter table ORDEN_TRABAJO add constraint FK_ORDEN_TRABAJO_ON_PROPUESTO_POR foreign key (PROPUESTO_POR_ID) references PERSONA(ID);
alter table ORDEN_TRABAJO add constraint FK_ORDEN_TRABAJO_ON_ADMINISTRADOR foreign key (ADMINISTRADOR_ID) references SEC_USER(ID);
create index IDX_ORDEN_TRABAJO_ON_ENTRADA on ORDEN_TRABAJO (ENTRADA_ID);
create index IDX_ORDEN_TRABAJO_ON_CARPETA_DOCUMENTOS_FOTOGRAFICOS on ORDEN_TRABAJO (CARPETA_DOCUMENTOS_FOTOGRAFICOS_ID);
create index IDX_ORDEN_TRABAJO_ON_PROPUESTO_POR on ORDEN_TRABAJO (PROPUESTO_POR_ID);
create index IDX_ORDEN_TRABAJO_ON_ADMINISTRADOR on ORDEN_TRABAJO (ADMINISTRADOR_ID);
