alter table FIANZA add constraint FK_FIANZA_ON_ESCANEO_SEGURO_ARCHIVO_ADJUNTO foreign key (ESCANEO_SEGURO_ARCHIVO_ADJUNTO_ID) references ARCHIVO_ADJUNTO(ID);
create index IDX_FIANZA_ON_ESCANEO_SEGURO_ARCHIVO_ADJUNTO on FIANZA (ESCANEO_SEGURO_ARCHIVO_ADJUNTO_ID);
