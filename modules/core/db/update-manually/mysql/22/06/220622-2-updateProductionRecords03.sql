alter table SANGYO_PRODUCTION_RECORDS add constraint FK_SANGYO_PRODUCTION_RECORDS_ON_QUALITY_TRACEABILITY foreign key (QUALITY_TRACEABILITY_ID) references SANGYO_QUALITY_TRACEABILITY(ID);
create index IDX_SANGYO_PRODUCTION_RECORDS_ON_QUALITY_TRACEABILITY on SANGYO_PRODUCTION_RECORDS (QUALITY_TRACEABILITY_ID);
