alter table TEST1_USUARIO add constraint FK_TEST1_USUARIO_ON_ROL foreign key (ROL_ID) references SEC_ROLE(ID);
create index IDX_TEST1_USUARIO_ON_ROL on TEST1_USUARIO (ROL_ID);
