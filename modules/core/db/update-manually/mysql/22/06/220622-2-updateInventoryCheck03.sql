alter table SANGYO_INVENTORY_CHECK add constraint FK_SANGYO_INVENTORY_CHECK_ON_INVENTORY_RECORDING foreign key (INVENTORY_RECORDING_ID) references SANGYO_INVENTORY_RECORDING(ID);
create index IDX_SANGYO_INVENTORY_CHECK_ON_INVENTORY_RECORDING on SANGYO_INVENTORY_CHECK (INVENTORY_RECORDING_ID);