alter table SANGYO_STATION_AND_PROCEDURE add constraint FK_SANGYO_STATION_AND_PROCEDURE_ON_PROCEDURE foreign key (PROCEDURE_ID) references SANGYO_PROCEDURE(ID);
create index IDX_SANGYO_STATION_AND_PROCEDURE_ON_PROCEDURE on SANGYO_STATION_AND_PROCEDURE (PROCEDURE_ID);