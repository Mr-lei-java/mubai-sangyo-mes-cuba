alter table SANGYO_STOCK_ORDER add constraint FK_SANGYO_STOCK_ORDER_ON_IN_PLANT_PART_NUMBER foreign key (IN_PLANT_PART_NUMBER_ID) references SANGYO_IN_PLANT_PART_NUMBER(ID);
create index IDX_SANGYO_STOCK_ORDER_ON_IN_PLANT_PART_NUMBER on SANGYO_STOCK_ORDER (IN_PLANT_PART_NUMBER_ID);
