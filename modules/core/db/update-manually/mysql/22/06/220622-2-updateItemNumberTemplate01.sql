alter table SANGYO_ITEM_NUMBER_TEMPLATE add constraint FK_SANGYO_ITEM_NUMBER_TEMPLATE_ON_ITEM_NUMBER_PARAMETER_TEMPLATE foreign key (ITEM_NUMBER_PARAMETER_TEMPLATE_ID) references SANGYO_ITEM_NUMBER_PARAMETER_TEMPLATE(ID);
create index IDX_SANGYO_ITEM_NUMBER_TEMPLATE_ON_ITEM_NUMBER_PARAMETER_TEMPLATE on SANGYO_ITEM_NUMBER_TEMPLATE (ITEM_NUMBER_PARAMETER_TEMPLATE_ID);
