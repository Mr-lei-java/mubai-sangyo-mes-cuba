alter table SANGYO_FINISHED add constraint FK_SANGYO_FINISHED_ON_REL_WORK_ORDER foreign key (REL_WORK_ORDER_ID) references SANGYO_WORK_ORDER(ID);
create index IDX_SANGYO_FINISHED_ON_REL_WORK_ORDER on SANGYO_FINISHED (REL_WORK_ORDER_ID);