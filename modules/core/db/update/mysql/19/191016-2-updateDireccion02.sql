alter table TEST1_DIRECCION add column DELETED_BY varchar(50) ;
alter table TEST1_DIRECCION add column UPDATE_TS datetime(3) ;
alter table TEST1_DIRECCION add column DELETE_TS datetime(3) ;
alter table TEST1_DIRECCION add column UPDATED_BY varchar(50) ;
alter table TEST1_DIRECCION add column CREATED_BY varchar(50) ;
alter table TEST1_DIRECCION add column CREATE_TS datetime(3) ;
alter table TEST1_DIRECCION add column VERSION integer ^
update TEST1_DIRECCION set VERSION = 0 where VERSION is null ;
alter table TEST1_DIRECCION modify column VERSION integer not null ;
