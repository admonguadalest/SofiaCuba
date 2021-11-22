alter table TEST1_INVERSION add column FECHA_ENTRADA date ^
update TEST1_INVERSION set FECHA_ENTRADA = current_date where FECHA_ENTRADA is null ;
alter table TEST1_INVERSION modify column FECHA_ENTRADA date not null ;
