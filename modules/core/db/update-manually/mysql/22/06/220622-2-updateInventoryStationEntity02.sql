alter table SANGYO_INVENTORY_STATION_ENTITY add constraint FK_SANGYO_INVENTORY_STATION_ENTITY_ON_PROCEDURE_NAME foreign key (PROCEDURE_NAME_ID) references SANGYO_PROCEDURE(ID);
create index IDX_SANGYO_INVENTORY_STATION_ENTITY_ON_PROCEDURE_NAME on SANGYO_INVENTORY_STATION_ENTITY (PROCEDURE_NAME_ID);
