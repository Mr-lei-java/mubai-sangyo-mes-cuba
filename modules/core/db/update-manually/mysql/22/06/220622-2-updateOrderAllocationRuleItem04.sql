alter table SANGYO_ORDER_ALLOCATION_RULE_ITEM add constraint FK_SANGYO_ORDER_ALLOCATION_RULE_ITEM_ON_CUSTOMER_DESCRIPTIVE_PARAMETER foreign key (CUSTOMER_DESCRIPTIVE_PARAMETER_ID) references SANGYO_CUSTOMER_DESCRIPTIVE_PARAMETERS(ID);
create index IDX_SANGYO_ORDER_ALLOCATION_RULE_ITEM_ON_CUSTOMER_DESCRIPTIVE_PARAMETER on SANGYO_ORDER_ALLOCATION_RULE_ITEM (CUSTOMER_DESCRIPTIVE_PARAMETER_ID);
