alter table SANGYO_DISPATCH_LIST add constraint FK_SANGYO_DISPATCH_LIST_ON_WORK_ORDER foreign key (WORK_ORDER_ID) references SANGYO_WORK_ORDER(ID);
create index IDX_SANGYO_DISPATCH_LIST_ON_WORK_ORDER on SANGYO_DISPATCH_LIST (WORK_ORDER_ID);
