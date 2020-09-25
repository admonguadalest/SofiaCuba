update TEST1_CONCEPTO set ORDENACION = 0 where ORDENACION is null ;
alter table TEST1_CONCEPTO modify column ORDENACION integer not null ;
