alter table SANGYO_WAREHOUSE add constraint FK_SANGYO_WAREHOUSE_ON_PROCEDURE foreign key (PROCEDURE_ID) references SANGYO_PROCEDURE(ID);
create index IDX_SANGYO_WAREHOUSE_ON_PROCEDURE on SANGYO_WAREHOUSE (PROCEDURE_ID);