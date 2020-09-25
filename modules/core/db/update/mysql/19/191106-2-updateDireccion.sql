alter table DIRECCION change column UBICACION_ID UBICACION_ID__U75694 varchar(32)^
alter table DIRECCION drop foreign key FK_DIRECCION_ON_UBICACION;
drop index IDX_DIRECCION_ON_UBICACION on DIRECCION ;
alter table DIRECCION change column NUMERO NUMERO__U06547 varchar(50)^
alter table DIRECCION modify column NUMERO__U06547 varchar(50) null ;
alter table DIRECCION change column NOMBRE_DIRECCION NOMBRE_DIRECCION__U49062 varchar(50)^
alter table DIRECCION add column NOMBRE_VIA varchar(255) ;
alter table DIRECCION add column ESCALERA varchar(10) ;
alter table DIRECCION add column PROVINCIA varchar(255) ;
alter table DIRECCION add column REGION varchar(255) ;
alter table DIRECCION add column VIA varchar(255) ;
alter table DIRECCION add column PISO varchar(50) ;
alter table DIRECCION add column OBSERVACIONES varchar(255) ;
alter table DIRECCION add column ENVIAR_CORRESPONDENCIA_A_ESTA_DIRECCION boolean ;
alter table DIRECCION add column DIRECCION_COMPLETA varchar(255) ;
alter table DIRECCION add column PAIS varchar(255) ;
alter table DIRECCION add column POBLACION varchar(255) ;
alter table DIRECCION add column NUMERO_VIA varchar(25) ;
alter table DIRECCION add column PUERTA varchar(25) ;
