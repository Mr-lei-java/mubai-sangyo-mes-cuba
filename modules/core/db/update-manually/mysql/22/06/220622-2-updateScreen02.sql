alter table SANGYO_SCREEN add constraint FK_SANGYO_SCREEN_ON_CURRENT_PROCEDURE foreign key (CURRENT_PROCEDURE_ID) references SANGYO_PROCEDURE(ID);
create index IDX_SANGYO_SCREEN_ON_CURRENT_PROCEDURE on SANGYO_SCREEN (CURRENT_PROCEDURE_ID);
