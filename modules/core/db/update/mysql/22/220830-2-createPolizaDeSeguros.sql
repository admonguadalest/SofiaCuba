alter table TEST1_POLIZA_DE_SEGUROS add constraint FK_TEST1_PDS_ON_ESCANEO_POLIZA foreign key (ESCANEO_POLIZA_ID) references ARCHIVO_ADJUNTO(ID);
create index IDX_TEST1_PDS_ON_ESCANEO_POLIZA on TEST1_POLIZA_DE_SEGUROS (ESCANEO_POLIZA_ID);