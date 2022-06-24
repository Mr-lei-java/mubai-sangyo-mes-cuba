alter table SANGYO_WORK_ORDER add constraint FK_SANGYO_WORK_ORDER_ON_CUSTOMER_PART_NUMBER foreign key (CUSTOMER_PART_NUMBER_ID) references SANGYO_CUSTOMER_PART_NUMBER(ID);
create index IDX_SANGYO_WORK_ORDER_ON_CUSTOMER_PART_NUMBER on SANGYO_WORK_ORDER (CUSTOMER_PART_NUMBER_ID);
