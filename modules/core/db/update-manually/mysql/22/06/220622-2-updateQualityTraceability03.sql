alter table SANGYO_QUALITY_TRACEABILITY add constraint FK_SANGYO_QUALITY_TRACEABILITY_ON_DISPATCH_LIST foreign key (DISPATCH_LIST_ID) references SANGYO_DISPATCH_LIST(ID);
create index IDX_SANGYO_QUALITY_TRACEABILITY_ON_DISPATCH_LIST on SANGYO_QUALITY_TRACEABILITY (DISPATCH_LIST_ID);
