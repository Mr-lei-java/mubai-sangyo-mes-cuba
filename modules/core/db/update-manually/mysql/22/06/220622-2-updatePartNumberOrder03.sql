alter table SANGYO_PART_NUMBER_ORDER add constraint FK_SANGYO_PART_NUMBER_ORDER_ON_ORDER foreign key (ORDER_ID) references SANGYO_ORDER(ID);
create index IDX_SANGYO_PART_NUMBER_ORDER_ON_ORDER on SANGYO_PART_NUMBER_ORDER (ORDER_ID);
