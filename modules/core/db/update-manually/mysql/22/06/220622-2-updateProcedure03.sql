alter table SANGYO_PROCEDURE add constraint FK_SANGYO_PROCEDURE_ON_LABEL_TEMPLATE foreign key (LABEL_TEMPLATE_ID) references SANGYO_LABEL_TEMPLATE(ID);
create index IDX_SANGYO_PROCEDURE_ON_LABEL_TEMPLATE on SANGYO_PROCEDURE (LABEL_TEMPLATE_ID);
