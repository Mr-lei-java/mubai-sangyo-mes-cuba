alter table SANGYO_QUALITY_TRACEABILITY add constraint FK_SANGYO_QUALITY_TRACEABILITY_ON_OPERATING_PERSONNEL foreign key (OPERATING_PERSONNEL_ID) references SEC_USER(ID);
create index IDX_SANGYO_QUALITY_TRACEABILITY_ON_OPERATING_PERSONNEL on SANGYO_QUALITY_TRACEABILITY (OPERATING_PERSONNEL_ID);
