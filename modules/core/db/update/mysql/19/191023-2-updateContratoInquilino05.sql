alter table CONTRATO_INQUILINO add constraint FK_CONTRATO_INQUILINO_ON_IMPLEMENTACION_MODELO foreign key (IMPLEMENTACION_MODELO_ID) references IMPLEMENTACION_MODELO(ID);
create index IDX_CONTRATO_INQUILINO_ON_IMPLEMENTACION_MODELO on CONTRATO_INQUILINO (IMPLEMENTACION_MODELO_ID);
