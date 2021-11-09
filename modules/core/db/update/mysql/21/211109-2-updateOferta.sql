alter table OFERTA add column ESTADO_OFERTA varchar(50) ^
update OFERTA set ESTADO_OFERTA = 'ComercializaciÃ³n' where ESTADO_OFERTA is null ;
alter table OFERTA modify column ESTADO_OFERTA varchar(50) not null ;
