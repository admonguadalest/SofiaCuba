rename table TEST1_CICLO to TEST1_CICLO__U11434 ;
alter table TEST1_ENTRADA drop foreign key FK_TEST1_ENTRADA_ON_CICLO;
alter table TEST1_EVENTO drop foreign key FK_TEST1_EVENTO_ON_CICLO;
alter table TEST1_IMPUTACION_DOCUMENTO_IMPUTABLE drop foreign key FK_TEST1_IMPUTACION_DOCUMENTO_IMPUTABLE_ON_CICLO;
