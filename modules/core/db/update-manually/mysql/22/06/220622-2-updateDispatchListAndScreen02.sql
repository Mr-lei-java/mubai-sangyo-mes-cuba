alter table SANGYO_DISPATCH_LIST_AND_SCREEN add constraint FK_SANGYO_DISPATCH_LIST_AND_SCREEN_ON_SCREEN foreign key (SCREEN_ID) references SANGYO_SCREEN(ID);
create index IDX_SANGYO_DISPATCH_LIST_AND_SCREEN_ON_SCREEN on SANGYO_DISPATCH_LIST_AND_SCREEN (SCREEN_ID);
