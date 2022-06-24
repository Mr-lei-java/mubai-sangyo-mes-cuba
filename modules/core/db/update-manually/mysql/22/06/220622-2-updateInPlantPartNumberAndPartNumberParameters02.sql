alter table SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS add constraint FK_SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS_ON_PART_NUMBER_PARAMETER_ID foreign key (PART_NUMBER_PARAMETER_ID_ID) references SANGYO_PART_NUMBER_PARAMETERS(ID);
create index IDX_SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS_ON_PART_NUMBER_PARAMETER_ID on SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS (PART_NUMBER_PARAMETER_ID_ID);
