alter table CONTRATO_INQUILINO add constraint FK_CONTRATO_INQUILINO_ON_ARCHIVO_ADJUNTO_RENUNCIA foreign key (ARCHIVO_ADJUNTO_RENUNCIA_ID) references ARCHIVO_ADJUNTO(ID);
create index IDX_CONTRATO_INQUILINO_ON_ARCHIVO_ADJUNTO_RENUNCIA on CONTRATO_INQUILINO (ARCHIVO_ADJUNTO_RENUNCIA_ID);
