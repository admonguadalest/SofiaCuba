alter table NOTA_INTERVENCION add constraint FK_NOTA_INTERVENCION_ON_ORDEN_TRABAJO foreign key (ORDEN_TRABAJO_ID) references ORDEN_TRABAJO(ID);
create unique index IDX_NOTA_INTERVENCION_UNIQ_ORDEN_TRABAJO_ID on NOTA_INTERVENCION (ORDEN_TRABAJO_ID) ;
create index IDX_NOTA_INTERVENCION_ON_ORDEN_TRABAJO on NOTA_INTERVENCION (ORDEN_TRABAJO_ID);
