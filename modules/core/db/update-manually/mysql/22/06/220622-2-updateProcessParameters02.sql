alter table SANGYO_PROCESS_PARAMETERS add constraint FK_SANGYO_PROCESS_PARAMETERS_ON_PARAMETER_NAME foreign key (PARAMETER_NAME_ID) references SANGYO_PART_NUMBER_PARAMETERS(ID);
create index IDX_SANGYO_PROCESS_PARAMETERS_ON_PARAMETER_NAME on SANGYO_PROCESS_PARAMETERS (PARAMETER_NAME_ID);