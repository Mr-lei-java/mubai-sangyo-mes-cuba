alter table SANGYO_STATION add constraint FK_SANGYO_STATION_ON_WORKSHOP_ID foreign key (WORKSHOP_ID_ID) references SANGYO_WORKSHOP(ID);
create index IDX_SANGYO_STATION_ON_WORKSHOP_ID on SANGYO_STATION (WORKSHOP_ID_ID);