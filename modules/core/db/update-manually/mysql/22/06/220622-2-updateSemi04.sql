alter table SANGYO_SEMI add constraint FK_SANGYO_SEMI_ON_WORK_ORDER foreign key (WORK_ORDER_ID) references SANGYO_WORK_ORDER(ID);
create index IDX_SANGYO_SEMI_ON_WORK_ORDER on SANGYO_SEMI (WORK_ORDER_ID);
