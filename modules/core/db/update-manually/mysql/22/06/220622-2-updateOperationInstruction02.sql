alter table SANGYO_OPERATION_INSTRUCTION add constraint FK_SANGYO_OPERATION_INSTRUCTION_ON_ISSUE_PARAMETERS foreign key (ISSUE_PARAMETERS_ID) references SANGYO_PART_NUMBER_PARAMETERS(ID);
create index IDX_SANGYO_OPERATION_INSTRUCTION_ON_ISSUE_PARAMETERS on SANGYO_OPERATION_INSTRUCTION (ISSUE_PARAMETERS_ID);