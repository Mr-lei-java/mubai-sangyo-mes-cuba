alter table SANGYO_PROCESS_PARAMETERS add constraint FK_SANGYO_PROCESS_PARAMETERS_ON_STANDARD_VALUE foreign key (STANDARD_VALUE_ID) references SANGYO_PART_NUMBER_PARAMETERS(ID);
create index IDX_SANGYO_PROCESS_PARAMETERS_ON_STANDARD_VALUE on SANGYO_PROCESS_PARAMETERS (STANDARD_VALUE_ID);
