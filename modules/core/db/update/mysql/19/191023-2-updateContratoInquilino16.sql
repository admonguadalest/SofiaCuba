alter table TEST1_CONTRATO_INQUILINO add constraint FK_TEST1_CONTRATO_INQUILINO_ON_REPRESENTANTE_ARRENDATARIO foreign key (REPRESENTANTE_ARRENDATARIO_ID) references TEST1_PERSONA(ID);
create index IDX_TEST1_CONTRATO_INQUILINO_ON_REPRESENTANTE_ARRENDATARIO on TEST1_CONTRATO_INQUILINO (REPRESENTANTE_ARRENDATARIO_ID);
