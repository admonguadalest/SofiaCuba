alter table ORDEN_TRABAJO add constraint FK_ORDEN_TRABAJO_ON_PROPUESTO_POR foreign key (PROPUESTO_POR_ID) references PERSONA(ID);
create index IDX_ORDEN_TRABAJO_ON_PROPUESTO_POR on ORDEN_TRABAJO (PROPUESTO_POR_ID);
