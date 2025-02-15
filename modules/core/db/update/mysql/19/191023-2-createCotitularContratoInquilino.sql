alter table TEST1_COTITULAR_CONTRATO_INQUILINO add constraint FK_TEST1_COTITULAR_CONTRATO_INQUILINO_ON_CONTRATO_INQUILINO foreign key (CONTRATO_INQUILINO_ID) references TEST1_CONTRATO_INQUILINO(ID);
alter table TEST1_COTITULAR_CONTRATO_INQUILINO add constraint FK_TEST1_COTITULAR_CONTRATO_INQUILINO_ON_COTITULAR foreign key (COTITULAR_ID) references TEST1_PERSONA(ID);
create index IDX_TEST1_COTITULAR_CONTRATO_INQUILINO_ON_CONTRATO_INQUILINO on TEST1_COTITULAR_CONTRATO_INQUILINO (CONTRATO_INQUILINO_ID);
create index IDX_TEST1_COTITULAR_CONTRATO_INQUILINO_ON_COTITULAR on TEST1_COTITULAR_CONTRATO_INQUILINO (COTITULAR_ID);
