alter table COMERCIAL_OFERTAS add constraint FK_COMERCIAL_OFERTAS_ON_ESCANEO_ACEPTACION foreign key (ESCANEO_ACEPTACION_ID) references ARCHIVO_ADJUNTO(ID);
create index IDX_COMERCIAL_OFERTAS_ON_ESCANEO_ACEPTACION on COMERCIAL_OFERTAS (ESCANEO_ACEPTACION_ID);
