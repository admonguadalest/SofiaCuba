alter table UBICACION change column PROPIETARIO_ID PROPIETARIO_ID__U85408 varchar(32)^
alter table UBICACION drop foreign key FK_UBICACION_ON_PROPIETARIO;
drop index IDX_UBICACION_ON_PROPIETARIO on UBICACION ;
alter table UBICACION add column PROPIETARIO_ID varchar(32) ;
