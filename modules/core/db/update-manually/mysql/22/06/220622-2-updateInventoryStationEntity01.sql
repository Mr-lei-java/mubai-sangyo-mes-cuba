alter table SANGYO_INVENTORY_STATION_ENTITY add constraint FK_SANGYO_INVENTORY_STATION_ENTITY_ON_SCREEN_CODE foreign key (SCREEN_CODE_ID) references SANGYO_SCREEN(ID);
create index IDX_SANGYO_INVENTORY_STATION_ENTITY_ON_SCREEN_CODE on SANGYO_INVENTORY_STATION_ENTITY (SCREEN_CODE_ID);