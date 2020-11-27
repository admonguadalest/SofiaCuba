/*
Comento porque da fallo ya uqe el campo ya existe. Y pese  acomentar y ejecutar, el script aun esta marcado como
no ejecutado PENDIENTE
alter table TEST1_REALIZACION_COBRO add constraint FK_TEST1_REALIZACION_COBRO_ON_CUENTA_BANCARIA foreign key (CUENTA_BANCARIA_ID) references CUENTA_BANCARIA(ID);
create index IDX_TEST1_REALIZACION_COBRO_ON_CUENTA_BANCARIA on TEST1_REALIZACION_COBRO (CUENTA_BANCARIA_ID);
*/