alter table CTA_DOCTOS_FOTOGRAFICOS add constraint FK_CTA_DOCTOS_FOTOGRAFICOS_ON_LIQUIDACION_EXTINCION foreign key (LIQUIDACION_EXTINCION_ID) references LIQUIDACION_EXTINCION(ID);
create index IDX_CTA_DOCTOS_FOTOGRAFICOS_ON_LIQUIDACION_EXTINCION on CTA_DOCTOS_FOTOGRAFICOS (LIQUIDACION_EXTINCION_ID);
