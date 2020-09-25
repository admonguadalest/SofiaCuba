alter table PERSONA add constraint FK_PERSONA_ON_USUARIO foreign key (USUARIO_ID) references SEC_USER(ID);
create index IDX_PERSONA_ON_USUARIO on PERSONA (USUARIO_ID);
