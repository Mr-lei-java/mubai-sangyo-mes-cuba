alter table SANGYO_SCREEN add constraint FK_SANGYO_SCREEN_ON_PART_NUMBER foreign key (PART_NUMBER_ID) references SANGYO_IN_PLANT_PART_NUMBER(ID);
create index IDX_SANGYO_SCREEN_ON_PART_NUMBER on SANGYO_SCREEN (PART_NUMBER_ID);
